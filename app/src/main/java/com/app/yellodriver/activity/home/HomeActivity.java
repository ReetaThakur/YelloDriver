package com.app.yellodriver.activity.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.CheckDeviceExistQuery;
import com.app.yellodriver.NotificationTypeEnum;
import com.app.yellodriver.PushDeleteRegistrationMutation;
import com.app.yellodriver.PushNewRegistrationMutation;
import com.app.yellodriver.PushUpdateRegistrationMutation;
import com.app.yellodriver.R;
import com.app.yellodriver.SetVehicleOnlineMutation;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.BaseActivity;
import com.app.yellodriver.activity.NotificationDismissActivity;
import com.app.yellodriver.activity.splash.SplashActivity;
import com.app.yellodriver.customview.CircleImageView;
import com.app.yellodriver.fragment.ContactUsFragment;
import com.app.yellodriver.fragment.HomeFragment;
import com.app.yellodriver.fragment.MyDocumentFragment;
import com.app.yellodriver.fragment.MyProfileFragment;
import com.app.yellodriver.fragment.MyTripsFragment;
import com.app.yellodriver.fragment.MyVehicleFragment;
import com.app.yellodriver.fragment.OnBoardOptionFragment;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.model.ModelPushNotify.DeleteDeviceModel;
import com.app.yellodriver.model.ModelPushNotify.FetchDeviceModel;
import com.app.yellodriver.model.ModelPushNotify.InsertDeviceModel;
import com.app.yellodriver.model.ModelPushNotify.PushNotifyViewModel;
import com.app.yellodriver.model.ModelPushNotify.UpdateDeviceModel;
import com.app.yellodriver.model.ModelRideRequest.RideRequestModel;
import com.app.yellodriver.model.ModelUpdateProfile.UpdateProfileModel;
import com.app.yellodriver.model.ModelVehicleStatus.VehicleStatusModel;
import com.app.yellodriver.service.ForegroundService;
import com.app.yellodriver.service.UpdateTokenWorker;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.ObjectSerializer;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private CircleImageView imgProfile;
    private LinearLayout mNavigationView;
    private LinearLayout llUserOnboard;
    private LinearLayout llmyvehicle;
    private LinearLayout llMyProfile;
    private LinearLayout llmenuLogout;
    private LinearLayout llHelpDesk;
    private LinearLayout llMyTrip;
    private LinearLayout llMyDocument;

    private TextView txtName;
    private TextView txtSince;

    private TextView txtRating;
    private RatingBar ratingBar;

    private ArrayList<RideRequestModel.Data.YtRideRequest> listRideRequest = new ArrayList<>();
    private PushNotifyViewModel pushVwModel;
    private boolean isForLogout;
    private boolean floatingBubble = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.CustomRouteTheme);
    }


    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void initializeComponents() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.main_drawer);
        llUserOnboard = findViewById(R.id.llUserOnboard);
        llMyProfile = findViewById(R.id.llMyProfile);
        llmyvehicle = findViewById(R.id.llmyvehicle);
        llMyTrip = findViewById(R.id.llMyTrips);
        llHelpDesk = findViewById(R.id.llHelpDesk);
        llMyDocument = findViewById(R.id.llMyDocument);
        txtName = findViewById(R.id.sideMenu_txtName);
        txtSince = findViewById(R.id.sideMenu_txtSince);
        txtRating = findViewById(R.id.sideMenu_txtRating);
        ratingBar = findViewById(R.id.ratingBar);
        imgProfile = findViewById(R.id.imgUserProfile);

        llUserOnboard.setOnClickListener(this);
        llmyvehicle.setOnClickListener(this);
        llMyProfile.setOnClickListener(this);
        llMyTrip.setOnClickListener(this);
        llHelpDesk.setOnClickListener(this);
        llMyDocument.setOnClickListener(this);

        llmenuLogout = findViewById(R.id.lllogout);
        llmenuLogout.setOnClickListener(this);

        int width = (int) ((getResources().getDisplayMetrics().widthPixels * 0.7) + 0.5f);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
        params.width = width;
        mNavigationView.setLayoutParams(params);
        try {
            checkForNotificationData(getIntent());
        } catch (Exception e) {
            YLog.e("ERROR", e.toString());
        }


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        /*-----------------------*/
        //Todo: Enable if you want to background service for firebase token
        //updateToken();
        /*-----------------------*/
        Utils.getUpdatedToken();
        getFirebaseToken();
    }


    private void checkForNotificationData(Intent intent) {
        // Get Data from notification
        if (intent != null) {
            if (intent.getExtras() != null) {
                YLog.e("RIDE_REQUEST", "Not null");
            }
            if (intent.getExtras() != null) {
                if (intent.getBooleanExtra(Constants.FLOATING_BUTTON_CLICK,false))
                {
                    floatingBubble = intent.getBooleanExtra(Constants.FLOATING_BUTTON_CLICK,false);

                }
                YLog.e("RIDE_REQUEST", String.valueOf(intent.getExtras().getInt(Constants.INTENT_KEY_NOTIFICATION_TYPE)));
                if (intent.getExtras().getInt(Constants.INTENT_KEY_NOTIFICATION_TYPE) != -1) {
                    int enumValue = intent.getExtras().getInt(Constants.INTENT_KEY_NOTIFICATION_TYPE);
                    YLog.e("ENUM_VALUE", String.valueOf(enumValue));
                    if (enumValue >= 0) {

                        NotificationTypeEnum
                                notificationTypeEnum = NotificationTypeEnum.values()[enumValue];
                        if (notificationTypeEnum == NotificationTypeEnum.TYPE_RIDE_REQUEST) {
                            SharedPreferences sharedPrefs = getSharedPreferences("ride_requests", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String json = sharedPrefs.getString("RIDE_REQUEST", "");
                            Type type = new TypeToken<List<RideRequestModel.Data.YtRideRequest>>() {
                            }.getType();
                            listRideRequest = gson.fromJson(json, type);
                        }
                    }
                }


            }

        }

        // Pass Data from notification
        Bundle bundle = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        bundle.putBoolean(Constants.FLOATING_BUTTON_CLICK,floatingBubble);
        //YLog.e("RIDE_REQUEST",listRideRequest.get(0).getRideId());
        if (listRideRequest != null && listRideRequest.size() > 0) {
            YLog.e("RIDE_REQUEST", listRideRequest.get(0).getRideId());
            bundle.putParcelableArrayList(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION, listRideRequest);

        }
        homeFragment.setArguments(bundle);
        replaceFragment(R.id.activity_home_flContainer, getSupportFragmentManager(), homeFragment, false, false);
    }

    private void getFirebaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            YLog.e(TAG, "getInstanceId failed" + task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        YLog.e(TAG, "Firebase Token:" + token);
                        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_FIREBASE_TOKEN, token);
                        sendPushToken();
                    }
                });
    }

    private void sendPushToken() {
        pushVwModel = ViewModelProviders.of(this).get(PushNotifyViewModel.class);
        pushVwModel.insertMutableLivePushNotify.observe((this), observerPushInsertResponse);
        pushVwModel.updateMutableLivePushNotify.observe((this), observerPushUpdateResponse);
        pushVwModel.deleteMutableLivePushNotify.observe((this), observerPushDeleteResponse);
        pushVwModel.getMutableLivePushNotify.observe((this), observerPushGetResponse);
        pushVwModel.mutableApoloSetStatus.observe((this), observerSetVehicleStatus);
        String deviceId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_DEVICE_ID, "");
        String userId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");

         pushVwModel.getPushDeviceId(new CheckDeviceExistQuery(deviceId, userId));
    }


    Observer<List<VehicleStatusModel.Data.Update_yt_vehicle.Returning>> observerSetVehicleStatus = listContent -> {
        if (listContent != null) {
            Log.e("HomeActivity", "User Logged Out");
        }
    };

    Observer<List<FetchDeviceModel.Data.Yt_push_registration>> observerPushGetResponse = listContent -> {
        if (listContent != null) {

            String deviceId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_DEVICE_ID, "");
            String fcmToken = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_FIREBASE_TOKEN, "");
            String userId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");

            if (listContent.size() > 0) {
                YLog.e("DeviceId Found", "Do Update");
                pushVwModel.updatePushDeviceId(new PushUpdateRegistrationMutation(deviceId, fcmToken, userId));
            } else {
                YLog.e("Same DeviceId By other user", "DELETE");
                isForLogout = false;
                pushVwModel.deletePushDeviceId(new PushDeleteRegistrationMutation(deviceId));
            }
        } else {
            YLog.e("DeviceID", "Problem");
        }
    };

    Observer<InsertDeviceModel.Data.Insert_yt_push_registration_one> observerPushInsertResponse = listContent -> {
        if (listContent != null) {
            YLog.e("PushNotification Registered", listContent.getId());
        } else {
            Toast.makeText(this, "Cannot register for Push Notification", Toast.LENGTH_SHORT).show();
        }
    };

    Observer<UpdateDeviceModel.Data.Update_yt_push_registration> observerPushUpdateResponse = listContent -> {
        if (listContent != null) {
            if (listContent.getAffected_rows() > 0) {
                YLog.e("PushNotification", "Updated");
            }
        }
    };

    Observer<DeleteDeviceModel.Data.Delete_yt_push_registration> observerPushDeleteResponse = listContent -> {
        if (listContent != null) {
            if (listContent.getAffected_rows() > 0) {
                YLog.e("PushNotification", "Deleted");
            } else {

            }
        }

        if (isForLogout) {
            FirebaseAuth.getInstance().signOut();

            Paper.book(Constants.PAPER_BOOK_USER).destroy();

            WorkManager.getInstance(HomeActivity.this).cancelAllWorkByTag(TAG);

            stopService(new Intent(HomeActivity.this, ForegroundService.class));

            startActivity(new Intent(HomeActivity.this, SplashActivity.class));
            finish();
        } else {
            //INSERT THE NEW TOKEN
            YLog.e("DeviceId NotFound", "Do Insert");

            String deviceId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_DEVICE_ID, "");
            String fcmToken = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_FIREBASE_TOKEN, "");
            String userId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");
            pushVwModel.insertPushDeviceId(new PushNewRegistrationMutation(deviceId, fcmToken, userId));
        }

    };


    private void updateToken() {

        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(UpdateTokenWorker.class, 45, TimeUnit.MINUTES)
                        .addTag(TAG)
//                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .build();

        WorkManager.getInstance(HomeActivity.this).enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicSyncDataWork);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.llUserOnboard:
                closeDrawer();
                openFragmentFromMenu(R.id.activity_home_flContainer, new OnBoardOptionFragment(), false);
                break;
            case R.id.llMyProfile:
                closeDrawer();
                openFragmentFromMenu(R.id.activity_home_flContainer, new MyProfileFragment(), false);
                break;
            case R.id.llmyvehicle:
                closeDrawer();
                openFragmentFromMenu(R.id.activity_home_flContainer, new MyVehicleFragment(), false);
                break;
            case R.id.llMyTrips:
                closeDrawer();
                openFragmentFromMenu(R.id.activity_home_flContainer, new MyTripsFragment(), false);
                break;
            case R.id.llHelpDesk:
                closeDrawer();
                //                openFragmentFromMenu(R.id.activity_home_flContainer, new HelpDeskFragment(), false);
                openFragmentFromMenu(R.id.activity_home_flContainer, new ContactUsFragment(), false);
                //sendMyNotification("Demo message");
                break;
            case R.id.llMyDocument:
                closeDrawer();
                openFragmentFromMenu(R.id.activity_home_flContainer, new MyDocumentFragment(), false);
                break;

            case R.id.lllogout:

                logout();

                break;

        }
    }

    public void closeDrawer() {
        new Handler().postDelayed(() -> mDrawerLayout.closeDrawers(), 250);
    }

    public void logout() {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        KAlertDialog askAlertDlg = new KAlertDialog(this)
                .setTitleText(YelloDriverApp.getInstance().getString(R.string.app_name))
                .setContentText(YelloDriverApp.getInstance().getString(R.string.do_you_want_to_logout))
                .setConfirmText(YelloDriverApp.getInstance().getString(R.string.yes))
                .setCancelText(YelloDriverApp.getInstance().getString(R.string.not_now));
        askAlertDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                askAlertDlg.dismiss();

                Input<Boolean> isOnline = new Input<>(false, true);

                Double driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                Double driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

                Input<Object> curPoint = new Input<>("(" + driverLongitude + "," + driverLatitude + ")", true);
                pushVwModel.setUserStatus(new SetVehicleOnlineMutation(isOnline, curPoint));


                String deviceId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_DEVICE_ID, "");
                isForLogout = true;
                pushVwModel.deletePushDeviceId(new PushDeleteRegistrationMutation(deviceId));
            }
        });

        askAlertDlg.setCancelClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                askAlertDlg.dismiss();
            }
        });
        askAlertDlg.show();
    }

    public void openMenuDrawer() {
        if (null != mDrawerLayout) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void setProfileData(MyProfileModel.Data.YtUser user) {

        txtName.setText(user.getFullName());
        if (user.getProfilePhoto() != null) {
            if (user.getProfilePhoto().getId() != null) {
                ImageLoader.loadGraphQlImage(HomeActivity.this, imgProfile, user.getProfilePhoto().getId(), R.drawable.ic_place_holder_user);
            }
        }

        txtSince.setText(DateUtils.timeAgo(user.getCreatedAt()));

        if (user.getAverage_rate() != null) {
            ratingBar.setRating(Float.parseFloat(user.getAverage_rate()));
            txtRating.setText(String.format("%.1f", Float.parseFloat(user.getAverage_rate())));
        } else {
            ratingBar.setRating(0f);
        }

//        txtYear.setText(user.getCreatedAt());
    }

    public void setProfileData(UpdateProfileModel.Data.UpdateUser updateYtUser) {

        txtName.setText(updateYtUser.getUser().getFullName());

        if (updateYtUser.getUser().getProfilePhotoFileId() != null) {
            if (updateYtUser.getUser().getProfilePhotoFileId() != null) {
                ImageLoader.loadGraphQlImage(HomeActivity.this, imgProfile, updateYtUser.getUser().getProfilePhotoFileId(), R.drawable.ic_place_holder_user);
            }
        }

        txtSince.setText(DateUtils.timeAgo(updateYtUser.getUser().getCreated_at()));

        if (updateYtUser.getUser().getAverageRate() != null) {
            ratingBar.setRating(Float.parseFloat(updateYtUser.getUser().getAverageRate().toString()));
            txtRating.setText(updateYtUser.getUser().getAverageRate() + "");
        } else {
            ratingBar.setRating(0f);
        }
//        txtYear.setText(user.getCreatedAt());
    }


    private void sendMyNotification(String message) {

        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;

        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("notificationId", randomNumber);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, homeIntent, PendingIntent.FLAG_ONE_SHOT);

        String channelName = "Audio Channel";
        String channelId = "audio_id"; //

        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.applause);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            if (soundUri != null) {
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                // Creating Channel
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(soundUri, audioAttributes);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }


        //PendingIntent launchIntent = getLaunchIntent(randomNumber, getBaseContext());


        //Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        //buttonIntent.putExtra("notificationId", randomNumber);
        //PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);
        PendingIntent dismissIntent = NotificationDismissActivity.getDismissIntent(randomNumber, getBaseContext());

        //notificationBuilder.setContentIntent(launchIntent);
        notificationBuilder.addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent);
        notificationBuilder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent);


        mNotificationManager.notify(randomNumber, notificationBuilder.build());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkForNotificationData(intent);
    }

    @Override
    public void onBackPressed() {
        tellFragments();
        if (allowBackPress) {
            super.onBackPressed();
        }
    }

    boolean allowBackPress;

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof HomeFragment) {
                allowBackPress = ((HomeFragment) f).onBackPressed();
            } else {
                allowBackPress = true;
                break;
            }
        }
    }
}