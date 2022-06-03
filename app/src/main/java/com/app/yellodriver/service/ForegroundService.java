package com.app.yellodriver.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.InsertVehicleLocationMutation;
import com.app.yellodriver.MyLifecycleHandler;
import com.app.yellodriver.NotificationTypeEnum;
import com.app.yellodriver.R;
import com.app.yellodriver.RideRequestSubscription;
import com.app.yellodriver.UpdateVehicleLocationMutation;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.fragment.HomeFragment;
import com.app.yellodriver.model.ModelRideRequest.RideRequestModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.ObjectSerializer;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import io.paperdb.Paper;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "YelloDriverChannel";

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 15000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 15000;
    //    private boolean isSubscriptionConnected = false;
    private LocationEngine locationEngine;
    private final LocationUpdateCallback locationCallback = new LocationUpdateCallback();
    private RideRequestSubscription rideRequestSubscription;
    private ApolloSubscriptionCall<RideRequestSubscription.Data> apoloRideRequestSubscription;
    private LocationCallback locationGmsCallback;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(@NotNull Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        initializeGmsLocationEngine();
        initializeLocationEngine();

        String input = intent.getStringExtra("inputExtra");

        createNotificationChannel(YelloDriverApp.getInstance().getString(R.string.notification_channel_online));

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(YelloDriverApp.getInstance().getString(R.string.you_are_online))
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

//        userInfoChangedSubscription();
        String driverUserId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");

        Input<Boolean> isAccept = new Input<>(false, true);
        Input<Boolean> isAvailable = new Input<>(true, true);
        Input<Boolean> isRejected = new Input<>(false, true);
        Input<Object> driverUserIdInput = new Input<>(driverUserId, true);
        Input<String> rideStatus = new Input<>("NEW", true);

        rideRequestSubscription = new RideRequestSubscription(isAccept, isAvailable, isRejected, driverUserIdInput, rideStatus);

        getRideRequestSubscription();
        YLog.e("SUBSCRIPTION", "START COMMAND");

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    private void initializeGmsLocationEngine() {
        locationGmsCallback = new LocationCallback() {
            public void onLocationResult(LocationResult p0) {
                super.onLocationResult(p0);
                if (p0 != null && p0.getLastLocation() != null) {
                    Double latitude = p0.getLastLocation().getLatitude();
                    Double longitude = p0.getLastLocation().getLongitude();
                    YLog.e("LATITUDE",String.valueOf(latitude));
                    Location location = new Location(p0.getLastLocation());
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.INTENT_KEY_LOCATION, location);
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.INTENT_KEY_LONGITUDE, location.getLongitude());
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.INTENT_KEY_LATITUDE, location.getLatitude());

                    boolean isRideOngoing = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);

                    // Check if subscription is not connected then retry again every few seconds
                    if (!Paper.book(Constants.PAPER_BOOK_USER).read(Constants.IS_SUBSCRIPTION_CONNECTED, false)) {
                        getRideRequestSubscription();
                    }

                    if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                        updateVehicleLocation(location);

                        if (isRideOngoing) {
                            insertVehicleLocation(location);
                        }
                    }
                }
            }
        };
    }

    public void cancelSubscription() {
        if (apoloRideRequestSubscription != null) {
            apoloRideRequestSubscription.cancel();
        }
    }

    private void stopLocationService()
    {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationGmsCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelSubscription();
        removeLocationEngineListener();
        stopLocationService();
    }

    @Nullable
    @Override
    public IBinder onBind(@NotNull Intent intent) {
        return null;
    }

    private void createNotificationChannel(String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void getRideRequestSubscription() {

        apoloRideRequestSubscription = ApoloCall.getApolloClient(this)
                .subscribe(rideRequestSubscription);

        apoloRideRequestSubscription
                .execute(new ApolloSubscriptionCall.Callback() {
                    @Override
                    public void onResponse(@NotNull Response response) {
                        YLog.e("SUBSCRIPTION", response.toString());

                        if (!Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {

                            if (!response.hasErrors()) {
                                Operation.Data data = (Operation.Data) response.getData();
                                String json = OperationDataJsonSerializer.serialize(data, "  ");
                                Gson gson = new Gson();
                                RideRequestModel rideRequestModel = gson.fromJson(json, RideRequestModel.class);
                                //Log.d("ADDRESS_LINE",rideRequestModel.getData().getYtRideRequest().get(0).getRide().getStartAddress().getLine1());

                                ArrayList<RideRequestModel.Data.YtRideRequest> listContent = rideRequestModel.getData().getYtRideRequest();

                                if (MyLifecycleHandler.isAppForeground()) {
                                    if (MyLifecycleHandler.getCurrentActivity() != null) {

                                        AppCompatActivity activity = MyLifecycleHandler.getCurrentActivity();
                                        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.activity_home_flContainer);

                                        if (fragment instanceof HomeFragment) {
                                            Intent intent = new Intent(Constants.INTENT_KEY_ACTION_RIDE_REQUEST_SUBSCRIPTION);
                                            // You can also include some extra data.
                                            intent.putParcelableArrayListExtra(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION, listContent);
                                            LocalBroadcastManager.getInstance(ForegroundService.this).sendBroadcast(intent);
                                        } else {
                                            Intent rideRequetIntent = new Intent(ForegroundService.this, HomeActivity.class);
                                            rideRequetIntent.putParcelableArrayListExtra(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION, listContent);
                                            rideRequetIntent.putExtra(Constants.INTENT_KEY_NOTIFICATION_TYPE, NotificationTypeEnum.TYPE_RIDE_REQUEST.ordinal());
                                            generateLocaleNotification(rideRequetIntent, YelloDriverApp.getInstance().getString(R.string.content_ride_request)
                                                    , YelloDriverApp.getInstance().getString(R.string.channel_name_ride_request));
                                        }
                                    }
                                } else {
                                    writeToSharedPreferences(listContent);
                                }

                            } else {
                                for (Object error : response.getErrors()) {
                                    //Could not verify JWT: JWTExpired
                                    YLog.e("All errors:", ((Error) error).getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        YLog.e("SUBSCRIPTION", "Failure" + e);
                        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.IS_SUBSCRIPTION_CONNECTED, false);
                        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                            getRideRequestSubscription();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        YLog.e("SUBSCRIPTION", "OnComplete");
                    }

                    @Override
                    public void onTerminated() {
                        YLog.e("SUBSCRIPTION", "OnTerminate");
                        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.IS_SUBSCRIPTION_CONNECTED, false);
                        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                            getRideRequestSubscription();
                        }
                    }

                    @Override
                    public void onConnected() {
                        YLog.e("SUBSCRIPTION", "onConnected");
                        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.IS_SUBSCRIPTION_CONNECTED, true);
                    }
                });
    }

    private void writeToSharedPreferences(ArrayList<RideRequestModel.Data.YtRideRequest> listContent) {
        SharedPreferences sharedPrefs = getSharedPreferences("ride_requests",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(listContent);

        editor.putString("RIDE_REQUEST", json);
        editor.apply();
        createNotificationIntent(listContent);
    }

    private void createNotificationIntent(ArrayList<RideRequestModel.Data.YtRideRequest> listContent) {
        Intent rideRequetIntent = new Intent(ForegroundService.this, HomeActivity.class);
        rideRequetIntent.putParcelableArrayListExtra(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION, listContent);
        rideRequetIntent.putExtra(Constants.INTENT_KEY_NOTIFICATION_TYPE, NotificationTypeEnum.TYPE_RIDE_REQUEST.ordinal());
        generateLocaleNotification(rideRequetIntent, YelloDriverApp.getInstance().getString(R.string.content_ride_request)
                , YelloDriverApp.getInstance().getString(R.string.channel_name_ride_request));
    }


    public void generateLocaleNotification(Intent intent, String contentText, String channelName) {

        Random random = new Random();
        int randomNo = random.nextInt();

//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), randomNo, intent, 0);
        String channelId = YelloDriverApp.getInstance().getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(YelloDriverApp.getInstance().getString(R.string.app_name))
                        .setContentText(contentText)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (notificationManager != null) {
            notificationManager.notify(randomNo/* ID of notification */, notificationBuilder.build());
        }
    }

    private int getNotificationIcon() {

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.mipmap.ic_notification_white : R.mipmap.ic_notification;
        return useWhiteIcon ? R.drawable.ic_notification : R.drawable.ic_notification;
    }


    private void initializeLocationEngine() {
        //locationEngine = LocationEngineProvider.getBestLocationEngine(YelloDriverApp.getInstance());
        //LocationEngineRequest request = buildEngineRequest();

        LocationRequest locationRequest = buildLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationGmsCallback, Looper.getMainLooper());
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationEngine.requestLocationUpdates(request, locationCallback, Looper.myLooper());

         */
    }

    private void removeLocationEngineListener() {
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(locationCallback);
        }
    }

    private LocationRequest buildLocationRequest()
    {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @NonNull
    private LocationEngineRequest buildEngineRequest() {

        return new LocationEngineRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .build();

    }


    class LocationUpdateCallback implements LocationEngineCallback<LocationEngineResult> {

        @Override
        public void onSuccess(LocationEngineResult result) {
            Location location = result.getLastLocation();
            if (location == null) {
                return;
            }

//            Intent intent = new Intent(Constants.INTENT_KEY_ACTION_LOCATION_UPDATE);
//            // You can also include some extra data.
//            intent.putExtra(Constants.INTENT_KEY_DATA_LOCATION_UPDATE, location);
//            LocalBroadcastManager.getInstance(ForegroundService.this).sendBroadcast(intent);

            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.INTENT_KEY_LOCATION, location);
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.INTENT_KEY_LONGITUDE, location.getLongitude());
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.INTENT_KEY_LATITUDE, location.getLatitude());

            boolean isRideOngoing = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);

            // Check if subscription is not connected then retry again every few seconds
            if (!Paper.book(Constants.PAPER_BOOK_USER).read(Constants.IS_SUBSCRIPTION_CONNECTED, false)) {
                getRideRequestSubscription();
            }

            if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                updateVehicleLocation(location);

                if (isRideOngoing) {
                    insertVehicleLocation(location);
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            YLog.e("Location Update failed", exception.toString());
        }
    }

    private void updateVehicleLocation(Location location) {

        String vehicleId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID);
        String stringLocation = Utils.setLatLongFromLocation(location);
        Input<Object> locationObject = new Input<>(stringLocation, true);

        ApoloCall.getApolloClient(this)
                .mutate(new UpdateVehicleLocationMutation(vehicleId, locationObject))
                .enqueue(new ApolloCall.Callback() {
                    @Override
                    public void onResponse(@NotNull Response response) {
                        YLog.e("LOCATION_UPDATE", response.toString());
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        YLog.e("LOCATION_UPDATE", e.toString());
                    }
                });
    }

    public void insertVehicleLocation(Location location) {

        Input<Object> driverUserId = new Input<Object>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID), true);
        Input<Object> riderUserId = new Input<Object>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_USER_ID), true);
        Input<Object> rideId = new Input<Object>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID), true);
        Input<Object> vehicleId = new Input<Object>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID), true);
        String stringLocation = Utils.setLatLongFromLocation(location);
        Input<Object> locationObject = new Input<>(stringLocation, true);

        InsertVehicleLocationMutation insertVehicleLocation = new InsertVehicleLocationMutation(driverUserId, riderUserId, rideId, vehicleId, locationObject);

        ApoloCall.getApolloClient(this)
                .mutate(insertVehicleLocation)
                .enqueue(new ApolloCall.Callback() {
                    @Override
                    public void onResponse(@NotNull Response response) {
                        YLog.e("Location Insert", response.toString());
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        YLog.e("Location Insert Failed", e.toString());
                    }
                });
    }
}