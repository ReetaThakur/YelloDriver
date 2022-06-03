package com.app.yellodriver.fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Mutation;
import com.app.yellodriver.AcceptRideMutation;
import com.app.yellodriver.BoardingPassAmountQuery;
import com.app.yellodriver.CapturePaymentMutation;
import com.app.yellodriver.CreatePaymentMutation;
import com.app.yellodriver.CreateStripeTokenMutation;
import com.app.yellodriver.DriverArrivedMutation;
import com.app.yellodriver.ExtendBoardingPassMutation;
import com.app.yellodriver.GetCancelReasonQuery;
import com.app.yellodriver.GiveRatingToRiderMutation;
import com.app.yellodriver.InsertSosRequestMutation;
import com.app.yellodriver.InsertSosRequestRideMutation;
import com.app.yellodriver.IsVehicleOnlineQuery;
import com.app.yellodriver.MyProfileQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.RideCompleteMutation;
import com.app.yellodriver.RideRejectMutation;
import com.app.yellodriver.RideStartByQRMutation;
import com.app.yellodriver.RideStatusCheckQuery;
import com.app.yellodriver.SetVehicleBookedMutation;
import com.app.yellodriver.SetVehicleOnlineMutation;
import com.app.yellodriver.StripeTerminal.NavigationListener;
import com.app.yellodriver.StripeTerminal.TerminalEventListener;
import com.app.yellodriver.StripeTerminal.TokenProvider;
import com.app.yellodriver.UploadRideImageMutation;
import com.app.yellodriver.VehicleLocationByRideQuery;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.customview.SwipeView;
import com.app.yellodriver.model.ModelDriverArrived.ModelDriverArrived;
import com.app.yellodriver.model.ModelExtendPass.ExtendPassViewModel;
import com.app.yellodriver.model.ModelExtendPass.ModelBoardingPassAmount;
import com.app.yellodriver.model.ModelExtendPass.ModelExtendBoardingPass;
import com.app.yellodriver.model.ModelHome.HomeViewModel;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.model.ModelRateRider.RateRiderModel;
import com.app.yellodriver.model.ModelRateRider.RateRiderViewModel;
import com.app.yellodriver.model.ModelRideAccept.RideAcceptModel;
import com.app.yellodriver.model.ModelRideComplete.RideCompleteModel;
import com.app.yellodriver.model.ModelRideReject.RideRejectModel;
import com.app.yellodriver.model.ModelRideReject.RideRejectViewModel;
import com.app.yellodriver.model.ModelRideRequest.RideRequestModel;
import com.app.yellodriver.model.ModelRideStart.RideStartModel;
import com.app.yellodriver.model.ModelRideStart.RideStartViewModel;
import com.app.yellodriver.model.ModelRideStatus.ModelRideStatus;
import com.app.yellodriver.model.ModelStripe.ModelCapturePayment;
import com.app.yellodriver.model.ModelStripe.ModelStripeToken;
import com.app.yellodriver.model.ModelUploadRIde.ModelUploadRide;
import com.app.yellodriver.model.ModelUserOnboarding.ModelCreatePayment.ModelCreatePayment;
import com.app.yellodriver.model.ModelVehicleLocation.ModelVehicleLocation;
import com.app.yellodriver.model.ModelVehicleStatus.OnlineVehicleModel;
import com.app.yellodriver.model.ModelVehicleStatus.VehicleStatusModel;
import com.app.yellodriver.model.ModelVehicleStatus.VehicleStatusViewModel;
import com.app.yellodriver.model.StoredRiderInfo;
import com.app.yellodriver.model.StoredRoute;
import com.app.yellodriver.service.ApiService;
import com.app.yellodriver.service.FloatingButtonService;
import com.app.yellodriver.service.ForegroundService;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.OnBackPressed;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.app.yellodriver.util.listener.OnSwipeCompleteListener;
import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.WalkingOptions;
import com.mapbox.api.directions.v5.models.BannerInstructions;
import com.mapbox.api.directions.v5.models.BannerText;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directions.v5.models.VoiceInstructions;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.mapbox.navigation.base.internal.VoiceUnit;
import com.mapbox.navigation.base.internal.route.RouteUrl;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.trip.model.RouteLegProgress;
import com.mapbox.navigation.base.trip.model.RouteProgress;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.Rounding;
import com.mapbox.navigation.core.arrival.ArrivalObserver;
import com.mapbox.navigation.core.directions.session.RoutesRequestCallback;
import com.mapbox.navigation.core.internal.formatter.MapboxDistanceFormatter;
import com.mapbox.navigation.core.reroute.RerouteController;
import com.mapbox.navigation.core.reroute.RerouteState;
import com.mapbox.navigation.core.trip.session.BannerInstructionsObserver;
import com.mapbox.navigation.core.trip.session.OffRouteObserver;
import com.mapbox.navigation.core.trip.session.RouteProgressObserver;
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver;
import com.mapbox.navigation.ui.camera.NavigationCamera;
import com.mapbox.navigation.ui.camera.NavigationCameraUpdate;
import com.mapbox.navigation.ui.instruction.InstructionView;
import com.mapbox.navigation.ui.map.NavigationMapboxMap;
import com.mapbox.navigation.ui.voice.NavigationSpeechPlayer;
import com.mapbox.navigation.ui.voice.SpeechPlayerProvider;
import com.mapbox.navigation.ui.voice.VoiceInstructionLoader;
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
import com.mapbox.services.android.navigation.v5.route.RouteListener;
import com.stripe.stripeterminal.Terminal;
import com.stripe.stripeterminal.Terminal_Factory;
import com.stripe.stripeterminal.callable.Cancelable;
import com.stripe.stripeterminal.callable.DiscoveryListener;
import com.stripe.stripeterminal.callable.PaymentIntentCallback;
import com.stripe.stripeterminal.callable.ReaderCallback;
import com.stripe.stripeterminal.callable.ReaderDisplayListener;
import com.stripe.stripeterminal.log.LogLevel;
import com.stripe.stripeterminal.model.external.ConnectionStatus;
import com.stripe.stripeterminal.model.external.DeviceType;
import com.stripe.stripeterminal.model.external.DiscoveryConfiguration;
import com.stripe.stripeterminal.model.external.PaymentIntent;
import com.stripe.stripeterminal.model.external.Reader;
import com.stripe.stripeterminal.model.external.ReaderDisplayMessage;
import com.stripe.stripeterminal.model.external.ReaderInputOptions;
import com.stripe.stripeterminal.model.external.TerminalException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;
import okhttp3.Cache;
import okhttp3.RequestBody;
import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;
import rebus.permissionutils.SmartCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Color.rgb;
import static android.os.Looper.getMainLooper;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.navigation.base.TimeFormat.TWELVE_HOURS;

public class HomeFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener,
        RouteProgressObserver, VoiceInstructionsObserver, BannerInstructionsObserver,
        OffRouteObserver, ArrivalObserver, OnBackPressed,
        // Listener for stripe terminal device connection and payment
        NavigationListener, DiscoveryListener, ReaderDisplayListener, RerouteController.RerouteStateObserver {

    private static final int FIRST = 0;
    private static final double DEFAULT_ZOOM = 16.0;
    private static final double DEFAULT_TILT = 0d;
    private static final double DEFAULT_BEARING = 0d;
    private static final int TWO_SECONDS_IN_MILLISECONDS = 2000;
    private static final String COMPONENT_NAVIGATION_INSTRUCTION_CACHE = "component-navigation-instruction-cache";
    private static final long TEN_MEGABYTE_CACHE_SIZE = 10 * 1024 * 1024;
    private static final double BEARING_TOLERANCE = 90d;
    private static final double PADDING_FROM_BOTTOM = 180;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 500;

    private MapView mapView;
    private AppCompatButton btnSos;
    private AppCompatButton btnRideSos;
    private TrafficPlugin trafficPlugin;
    public MapboxMap mapboxMap;
    private SwipeView swipeView;
    private TextView txtOnline;
    private ImageView imgOnline;
    private ImageView imgArrived;
    private LinearLayout llOnline;
    private LinearLayout llOnlineOffline;
    private BottomSheetBehavior rideDetailsBehavior, behaviorRideAsComplete, startRidebehavior;
    private LinearLayout actionSheetStartRide, actionSheetRideDetails, actionSheetRideAsComplete;
    //private TextView txtAcceptRide;
    private Button btnMarkasComplete;
    private Button btnExtendPass;
    private Button btnRideDetailDecline, btnRideDetailAccept;
    private Button btnRideStart;
    private LinearLayout llStartRideContactview;
    private LinearLayout llStartRidePickUpDetails;
    private RelativeLayout rlOnlineOffline;
    private ImageView imgStartRideDownarrow;
    private ImageView imgMarkascompletearrow;
    private Reader reader;
    // Ride Request Bottom Sheet
    private TextView txtRideUserName;
    private TextView txtRidePickup;
    private TextView txtRideRate;
    private ImageView imgRideUser;

    // Start Ride Bottom Sheet
    private TextView txtStartRideUserName;
    private TextView txtStartRidePickup;
    private TextView txtStartRideRate;
    private ImageView imgStartRideUser;
    private ImageView imgStartRideCallUser;

    private InstructionView instructionView;
    //private DirectionsRoute route;
    private MapboxNavigation navigation;
    private NavigationMapboxMap navigationMap;
    private NavigationSpeechPlayer speechPlayer;
    //private Location lastLocation;
    public boolean isMapLoaded = false;
    private Point destination = null;
    private Point source = null;
    private LocationComponent locationComponent;
    private Location updatedLocation;
    private double etaNumber = 0.0;

    private ImageView imgDirectionIconMarkComplete;
    private TextView tvPrimeInstructions;

    private TextView tvNextDistanceMarkComplete;
    private TextView tvArrivalTimeMarkComplete;
    private TextView tvTotDistanceAndETAMarkAsCmplte;

    private HomeViewModel homeViewmodel;
    private ExtendPassViewModel extendPassViewModel;

    private ArrayList<RideRequestModel.Data.YtRideRequest> listRideRequest = new ArrayList<>();

    private CustomProgressDialog customProgressDialog;
    private VehicleStatusViewModel vehicleStatusViewModel;
    private double driverLongitude;
    private double driverLatitude;
    private Button btnRideCancel;

    private ImageView imgStartRideDircetionIco;
    private TextView txtStartRideInstructions;
    private TextView txtStartRideTotDistance;

    private ImageView imgArrowSecStartRide;
    private TextView txtDirectionSecStartRide;
    private LinearLayout llStartRideSec;

    //private FusedLocationProviderClient fusedLocationClient;
    //private Location knownLocation;
    private TextView txtStartRideNxtDistance;
    private TextView tvStartLocationMarkAsCmplte;
    private TextView tvEndLocationMarkAsCmplte;
    private TextView tvUserNameMarkAsCmplte;
    private TextView tvUserRateMarkAsCmplte;
    private ImageView imgusrnamemarkascmplte;
    private ImageView imgMarkascompleteArrowSec;
    private TextView txtMarkascompleteSec;
    private LinearLayout llMarkAsCompleteSec;

    private ImageView fabMapRecentre;
    private TextView tvConfirmCodeMarkAsCmplte;
    private Activity mActivity;
    private boolean preventClosing = false;
    private ImageView ivPlayVoiceInstructions;

    private Bitmap routeBitmapImage;
    private String rideRequestId;
    private String rideId;

    private String secretToken;
    public Cancelable discoveryTask;
    private PaymentIntent paymentIntent;
    private String paymentId;
    private String orderId;
    private boolean isPaymentSimulate = false;
    private int extendByDays;
    public List<? extends Reader> readers = new ArrayList<>();

    private String serialNumber;

    private ModelBoardingPassAmount.Data.YtBoardingPassByPk ytBoardingPassByPk;

    private ReplayRouteLocationEngine routeReplayLocationEngine;
    private boolean isAutoDriveRequired = false;
    private AppCompatButton googleMapNavigationButton;
    private boolean floatingBubble;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        instructionView = view.findViewById(R.id.fragment_home_instructionView);
        googleMapNavigationButton = view.findViewById(R.id.fragment_home_google_maps);
        //Important to set for distance unit
        instructionView.setDistanceFormatter(new MapboxDistanceFormatter.Builder(YelloDriverApp.getInstance())
                .unitType(DirectionsCriteria.METRIC)
                .roundingIncrement(Rounding.INCREMENT_FIFTY)
                .build());
        fabMapRecentre = view.findViewById(R.id.fabMapRecentre);
        ivPlayVoiceInstructions = view.findViewById(R.id.ivplayinstruction);

        if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ALLOW_VOICE_PLAY, false)) {
            ivPlayVoiceInstructions.setImageResource(R.drawable.ic_volume_on);
        } else {
            ivPlayVoiceInstructions.setImageResource(R.drawable.ic_volume_off);
        }

        googleMapNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoogleMapsNavigation();
            }
        });

        //fabMapRecentre.setVisibility(View.GONE);
        manageViewClick();
        manageOnlineOfflineSwipe();
        manageBottomSheetCallback();
        registerAllBroadcast();
        setUpViewModel();
        // Get Data from notification
        if (getArguments() != null) {
            YLog.e("RIDE_REQUEST", "Received");
            if (getArguments().getBoolean(Constants.FLOATING_BUTTON_CLICK)) {
                floatingBubble = getArguments().getBoolean(Constants.FLOATING_BUTTON_CLICK);
            }
            if (getArguments().getParcelableArrayList(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION) != null) {
                YLog.e("RIDE_REQUEST", "Received");
                listRideRequest = getArguments().getParcelableArrayList(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION);
                rideRequestReceived(listRideRequest);
            }
        }
        if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
            googleMapNavigationButton.setVisibility(View.VISIBLE);
        }
        askForSystemOverlayPermission();
    }


    private void askForSystemOverlayPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(YelloDriverApp.getCurrentActivity().getApplicationContext())) {
            KAlertDialog askAlertDlg = new KAlertDialog(mActivity)
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.app_name))
                    .setContentText(getString(R.string.floating_button_permission))
                    .setConfirmText(YelloDriverApp.getInstance().getString(R.string.ok));
            askAlertDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    askAlertDlg.dismiss();
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + YelloDriverApp.getCurrentActivity().getPackageName()));
                    startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);

                }
            });
            askAlertDlg.show();

        }

    }

    private void startGoogleMapsNavigation() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(destination.latitude()) + "," + String.valueOf(destination.longitude()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            googleMapNavigationButton.setVisibility(View.VISIBLE);
            try {
                startActivity(mapIntent);
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } catch (Exception ex) {
                Log.e("HOMEFRAGMENT", ex.toString());
            }
        } else {
            KAlertDialog askAlertDlg = new KAlertDialog(mActivity)
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.app_name))
                    .setContentText(YelloDriverApp.getInstance().getString(R.string.no_maps_navigator))
                    .setConfirmText(YelloDriverApp.getInstance().getString(R.string.ok));
            askAlertDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    kAlertDialog.dismiss();
                }
            });
            askAlertDlg.show();
        }
    }

    private void setUpAllDataOnMapReady() {

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        homeViewmodel.getProfileData(new MyProfileQuery(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "")));
    }

    private void setUpViewModel() {

        homeViewmodel = ViewModelProviders.of((HomeActivity) mActivity).get(HomeViewModel.class);
        vehicleStatusViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(VehicleStatusViewModel.class);
        extendPassViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(ExtendPassViewModel.class);

        homeViewmodel.mutableLiveDataUserProfile.observe(getViewLifecycleOwner(), observerProfile);
//      homeViewmodel.mutableLiveDataRideRequest.observe(((HomeActivity) mActivity), observerRideRequestSubscription);

        homeViewmodel.mutableLiveDataAcceptRide.observe(getViewLifecycleOwner(), observerAcceptRideRequest);
        homeViewmodel.mutableLiveDataCompleteRide.observe(getViewLifecycleOwner(), observerRideCompletion);
        homeViewmodel.mutableLiveDataVehicleLocation.observe(getViewLifecycleOwner(), observerVehicleLocation);
        homeViewmodel.mutableLiveDataUploadRideRoute.observe(getViewLifecycleOwner(), observerRideRoute);
        homeViewmodel.mutableLiveDataRideStatus.observe(getViewLifecycleOwner(), observerRideStatus);

        homeViewmodel.mutableLiveDataCheckRideStatus.observe(getViewLifecycleOwner(), observerCheckRideStatus);

        vehicleStatusViewModel.mutableApoloResponseSetStatus.observe(getViewLifecycleOwner(), observerSetVehicleStatus);
        vehicleStatusViewModel.mutableApoloResponseIsOnline.observe(getViewLifecycleOwner(), observerGetVehicleStatus);

        extendPassViewModel.mutableLiveDataPassAmount.observe(getViewLifecycleOwner(), observerExtendPassAmount);
        homeViewmodel.liveDataStripeToken.observe(getViewLifecycleOwner(), observerStripeToken);
        homeViewmodel.liveDataExtendsBoardingPass.observe(getViewLifecycleOwner(), observerExtendsPass);
        homeViewmodel.liveDataCreatePayment.observe(getViewLifecycleOwner(), observerPayment);
        homeViewmodel.liveDataCapturePayment.observe(getViewLifecycleOwner(), observerCapturePayment);

        homeViewmodel.mutableLiveDataDriverArrived.observe(getViewLifecycleOwner(), observerDriverArrived);
    }

    private void manageViewClick() {
        imgStartRideCallUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String mobileToCall = view.getTag() + "";
//                if ("null".equalsIgnoreCase(mobileToCall)) {
//                    mobileToCall = "9898012345";
//                }

                StoredRiderInfo riderInfo = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_INFO, new StoredRiderInfo());
                if (riderInfo != null) {
                    if (riderInfo.getRiderMobile() != null) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", riderInfo.getRiderMobile(), null));
                        mActivity.startActivity(intent);
                    }
                }

            }
        });

        fabMapRecentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start  animate camera and redirect
                locationComponent = mapboxMap.getLocationComponent();
                locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
                // Set the component's render mode
                locationComponent.setRenderMode(RenderMode.GPS);
//                 END  animate camera and redirect
            }
        });

        ivPlayVoiceInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check the last status
                if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ALLOW_VOICE_PLAY, false)) {
                    //allowed so turn off voice now
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_ALLOW_VOICE_PLAY, false);
                    ivPlayVoiceInstructions.setImageResource(R.drawable.ic_volume_off);
                    if (null != speechPlayer) {
                        speechPlayer.setMuted(true);
                    }
                } else {
                    //Not allowed so turn ON voice now
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_ALLOW_VOICE_PLAY, true);
                    ivPlayVoiceInstructions.setImageResource(R.drawable.ic_volume_on);
                    if (null != speechPlayer) {
                        speechPlayer.setMuted(false);
                    }
                }
            }
        });

        imgArrived.setOnClickListener(view -> {

            customProgressDialog = new CustomProgressDialog(mActivity);
            customProgressDialog.showDialog();

            DriverArrivedMutation driverArrivedMutation = new DriverArrivedMutation(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, "")
                    , Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID, ""));
            homeViewmodel.driverArrived(driverArrivedMutation);
        });
    }

    private void captureScreen(ArrayList<ModelVehicleLocation.Data.YtVehicleLocation> alVehicleLocation) {
        ArrayList<LatLng> newLatLngs = new ArrayList<>();

        for (int i = 0; i < alVehicleLocation.size(); i++) {
            String[] arrLocation = Utils.getLatLongFromString(alVehicleLocation.get(i).getLocation());
            LatLng locationLatLan = new LatLng(Double.parseDouble(arrLocation[1]), Double.parseDouble(arrLocation[0]));
            newLatLngs.add(locationLatLan);
        }

        List<Point> routeCoordinates = new ArrayList<>();
        for (LatLng latLng : newLatLngs) {
            routeCoordinates.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
        }

        GeoJsonSource lineSource = new GeoJsonSource("route-line-layer-source", LineString.fromLngLats(routeCoordinates), new GeoJsonOptions().withLineMetrics(true));


        LineLayer lineLayer = new LineLayer("route-line-layer", "route-line-layer-source")
                .withProperties(PropertyFactory.lineColor(ColorUtils.colorToRgbaString(ContextCompat.getColor(mActivity
                        , R.color.colorAccent))),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
//                        PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
                        PropertyFactory.lineWidth(5f),
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineGradient(
                                com.mapbox.mapboxsdk.style.expressions.Expression.interpolate(
                                        com.mapbox.mapboxsdk.style.expressions.Expression.linear(),
                                        com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress(),
                                        com.mapbox.mapboxsdk.style.expressions.Expression.stop(0.5f, com.mapbox.mapboxsdk.style.expressions.Expression.rgb(255, 252, 0)),
                                        com.mapbox.mapboxsdk.style.expressions.Expression.stop(1.0f, com.mapbox.mapboxsdk.style.expressions.Expression.rgb(0, 128, 0))

                                )
                        )
                )
                .withSourceLayer("route-line-layer-source");


        StoredRoute storedRoute = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ONGOING_ROUTE, new StoredRoute());

        LatLng startLatLang = new LatLng(storedRoute.getStartPoint().latitude(), storedRoute.getStartPoint().longitude());
        LatLng endLatLang = new LatLng(storedRoute.getEndPoint().latitude(), storedRoute.getEndPoint().longitude());


        Style.Builder builder1 = new Style.Builder()
                .fromUri(YelloDriverApp.getInstance().getString(R.string.map_style))
                .withSource(lineSource)
                .withLayer(lineLayer);


//        LatLngBounds latLngBounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;
        //addMarkerIconsToMap(mapboxMap.getStyle(), startLatLang, endLatLang);

        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(startLatLang) // Northeast
                .include(endLatLang) // Southwest
                .build();


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 200);
        mapboxMap.easeCamera(cameraUpdate, 1000);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                MapSnapshotter.Options mapOptions = new MapSnapshotter.Options(500, 500)
                        .withCameraPosition(cameraUpdate.getCameraPosition(mapboxMap))
                        .withRegion(latLngBounds)
//                        .withRegion(latLngBounds)
//                .withLogo(false)
                        .withStyleBuilder(builder1);

                new MapSnapshotter(mActivity, mapOptions).start(new MapSnapshotter.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(MapSnapshot snapshot) {
//                img.setImageBitmap(snapshot.getBitmap());
                        routeBitmapImage = addMarker(snapshot, startLatLang, endLatLang);
                        //routeBitmapImage = snapshot.getBitmap();

                        //saveImage(getContext(),routeBitmapImage,"trip_image");
                        String onGoingRideId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, "");

                        UploadRideImageMutation uploadRideImageMutation = new UploadRideImageMutation(onGoingRideId, "image/png", onGoingRideId);
                        homeViewmodel.uploadRouteImage(uploadRideImageMutation);
                        googleMapNavigationButton.setVisibility(View.INVISIBLE);
                    }
                }, new MapSnapshotter.ErrorHandler() {
                    @Override
                    public void onError(String error) {
                        YLog.e("ERROR ", error);
                    }
                });
            }
        }, 1500);
    }

    private Bitmap addMarker(MapSnapshot snapshot, LatLng startLatLang, LatLng endLatLng) {
        Canvas canvas = new Canvas(snapshot.getBitmap());
        Bitmap startMarker = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_yellow_circle, null);
        Bitmap endMarker = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_green_circle, null);
        Bitmap startMarkerResized = Bitmap.createScaledBitmap(startMarker, 20, 20, true);
        Bitmap endMarkerResized = Bitmap.createScaledBitmap(endMarker, 20, 20, true);
        // Dom toren
        PointF startMarkerLocation = snapshot.pixelForLatLng(startLatLang);
        canvas.drawBitmap(startMarkerResized,
                /* Subtract half of the width so we center the bitmap correctly */
                startMarkerLocation.x - startMarkerResized.getWidth() / 2,
                /* Subtract half of the height so we align the bitmap bottom correctly */
                startMarkerLocation.y - startMarkerResized.getHeight() / 2,
                null
        );

        PointF endMarkerLocation = snapshot.pixelForLatLng(endLatLng);
        canvas.drawBitmap(endMarkerResized,
                /* Subtract half of the width so we center the bitmap correctly */
                endMarkerLocation.x - endMarkerResized.getWidth() / 2,
                /* Subtract half of the height so we align the bitmap bottom correctly */
                endMarkerLocation.y - endMarkerResized.getHeight() / 2,
                null
        );
        return snapshot.getBitmap();
    }

    private void addMarkerIconsToMap(@NonNull Style loadedMapStyle, LatLng startLatLang, LatLng endLatLang) {
        loadedMapStyle.addImage("icon-id", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_yellow_circle)));

        loadedMapStyle.addSource(new GeoJsonSource("source-id",
                FeatureCollection.fromFeatures(new Feature[]{
                        Feature.fromGeometry(Point.fromLngLat(startLatLang.getLongitude(), startLatLang.getLatitude())),
                        Feature.fromGeometry(Point.fromLngLat(endLatLang.getLongitude(), endLatLang.getLatitude())),
                })));

        loadedMapStyle.addLayer(new SymbolLayer("layer-id",
                "source-id").withProperties(
                iconImage("icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        Log.d("RIDE_IMAGE", "Invoked");
        FileOutputStream foStream;
        String root = context.getExternalFilesDir(null).getAbsolutePath();
        if (root != null) {
            Log.d("ROOT_PATH", root);
        }
        File myDir = new File(root + "/" + "routes");
        myDir.mkdirs();
        File file = new File(myDir, imageName + ".png");
        if (file.exists()) {
            Log.i("file exists", "" + imageName);
            file.delete();
        } else {
            Log.i("file does not exists", "" + imageName);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception");
            e.printStackTrace();
        }
    }

    private void manageOnlineOfflineSwipe() {
        swipeView.setOnSwipeCompleteListener_forward_reverse(new OnSwipeCompleteListener() {

            @Override
            public void onSwipe_Forward(SwipeView swipe__view) {

                imgOnline.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.drawable_circle_online));
                swipeView.setText(YelloDriverApp.getInstance().getString(R.string.you_are_online));
                Input<Boolean> isOnline = new Input<>(true, true);
//                getDriverLocation();
                driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

                //PASS (longitude,latitude)
                Input<Object> curPoint = new Input<>("(" + driverLongitude + "," + driverLatitude + ")", true);
                vehicleStatusViewModel.setUserStatus(new SetVehicleOnlineMutation(isOnline, curPoint));

                new Handler().postDelayed(() -> {
                    txtOnline.setText(YelloDriverApp.getInstance().getString(R.string.you_are_online));
                    llOnline.setVisibility(View.VISIBLE);
                    llOnlineOffline.setVisibility(View.GONE);
                }, 1000);

            }

            @Override
            public void onSwipe_Reverse(SwipeView swipe__view) {

                swipeView.setText(YelloDriverApp.getInstance().getString(R.string.you_are_offline));
                imgOnline.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.drawable_circle_offline));

                Input<Boolean> isOnline = new Input<>(false, true);

//                getDriverLocation();
                //PASS (longitude,latitude)
                driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

                Input<Object> curPoint = new Input<>("(" + driverLongitude + "," + driverLatitude + ")", true);
                vehicleStatusViewModel.setUserStatus(new SetVehicleOnlineMutation(isOnline, curPoint));

                new Handler().postDelayed(() -> {
                    txtOnline.setText(YelloDriverApp.getInstance().getString(R.string.you_are_offline));
                    llOnline.setVisibility(View.VISIBLE);
                    llOnlineOffline.setVisibility(View.GONE);
                }, 1000);
            }
        });
    }

    private void manageBottomSheetCallback() {
        // callback for Start-Cancel ride sheet
        startRidebehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btn_bottom_sheet.setText("Close Sheet");
                        imgStartRideDownarrow.setVisibility(View.GONE);
                        llStartRidePickUpDetails.setVisibility(View.VISIBLE);
                        adjustMapPaddingForNavigation(200, 14);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btn_bottom_sheet.setText("Expand Sheet");
                        llStartRidePickUpDetails.setVisibility(View.GONE);
                        imgStartRideDownarrow.setVisibility(View.VISIBLE);

                        adjustMapPaddingForNavigation(100, 16);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        behaviorRideAsComplete.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btn_bottom_sheet.setText("Close Sheet");
                        imgMarkascompletearrow.setVisibility(View.GONE);

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btn_bottom_sheet.setText("Expand Sheet");
                        imgMarkascompletearrow.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void calculateDistance(Point origin, Point destination) {
        Double bearing = 0d;
        if (null != updatedLocation) {
            bearing = Float.valueOf(updatedLocation.getBearing()).doubleValue();
        }

        MapboxDirections client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .addBearing(bearing, BEARING_TOLERANCE)
                .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED)
                .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
                .accessToken(YelloDriverApp.getInstance().getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if (response.body() == null) {
                    YLog.e("***", "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    YLog.e("***", "No routes found");
                    return;
                }

                // Retrieve the directions route from the API response
                DirectionsRoute currentRoute = response.body().routes().get(0);

                etaNumber = currentRoute.distance() / 60;

                YLog.e("*** Distance", currentRoute.distance() + "");
                YLog.e("*** Duration", currentRoute.duration() + "");
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {

                Log.e(TAG, "Error: " + throwable.getMessage());

            }
        });

    }

    @Override
    protected void initializeComponent(View view) {
        btnSos = view.findViewById(R.id.btnSos);
        btnRideSos = view.findViewById(R.id.fragment_home_btnSos);

        //        instructionView = view.findViewById(R.id.fragment_home_instructionView);

        swipeView = view.findViewById(R.id.swipeView);

        rlOnlineOffline = view.findViewById(R.id.rlOnlineOffline);
        txtOnline = view.findViewById(R.id.txtOnline);
        imgOnline = view.findViewById(R.id.imgOnline);
        llOnline = view.findViewById(R.id.llOnline);
        llOnlineOffline = view.findViewById(R.id.llOnlineOffline);

        actionSheetRideDetails = view.findViewById(R.id.actionSheetRideDetails);
        rideDetailsBehavior = BottomSheetBehavior.from(actionSheetRideDetails);
        rideDetailsBehavior.setHideable(true);//Important to add
        rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        actionSheetRideAsComplete = view.findViewById(R.id.actionSheetRideAsComplete);
        behaviorRideAsComplete = BottomSheetBehavior.from(actionSheetRideAsComplete);
        behaviorRideAsComplete.setHideable(true);
        behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_HIDDEN);

        actionSheetStartRide = view.findViewById(R.id.actionSheetStartRide);
        startRidebehavior = BottomSheetBehavior.from(actionSheetStartRide);
        startRidebehavior.setHideable(true);
        startRidebehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        btnMarkasComplete = view.findViewById(R.id.btnMarkasComplete);
        btnExtendPass = view.findViewById(R.id.btnExtendPass);
        btnRideDetailDecline = view.findViewById(R.id.bntRideDetailDecline);
        btnRideDetailAccept = view.findViewById(R.id.bntRideDetailAccept);
        btnRideStart = view.findViewById(R.id.btnRideStart);
        btnRideCancel = view.findViewById(R.id.btnRideCancel);

        txtRideUserName = view.findViewById(R.id.ride_details_txtUserName);
        txtRidePickup = view.findViewById(R.id.ride_details_txtPickup);
        txtRideRate = view.findViewById(R.id.ride_details_txtStar);
        imgRideUser = view.findViewById(R.id.ride_details_imgUser);

        /*---Start ride bottom sheet views----*/
        txtStartRideNxtDistance = view.findViewById(R.id.tvremaindisttorider);
        txtStartRideTotDistance = view.findViewById(R.id.tvtotaldisttorider);
        txtStartRideInstructions = view.findViewById(R.id.tvinstructtorider);
        imgStartRideDircetionIco = view.findViewById(R.id.imgdirecttorider);
        txtStartRideUserName = view.findViewById(R.id.start_ride_txtUserName);
        txtStartRidePickup = view.findViewById(R.id.start_ride_txtPickup);
        txtStartRideRate = view.findViewById(R.id.start_ride_txtStar);
        imgStartRideUser = view.findViewById(R.id.start_ride_imgUser);
        imgArrived = view.findViewById(R.id.start_ride_imgArrived);
        imgStartRideCallUser = view.findViewById(R.id.start_ride_imgCall);
        llStartRideContactview = view.findViewById(R.id.llRiderContactview);
        llStartRidePickUpDetails = view.findViewById(R.id.llStartRidePickUpDetails);
        imgStartRideDownarrow = view.findViewById(R.id.imgStartRideDownarrow);

        imgArrowSecStartRide = view.findViewById(R.id.ivnextdirectionStartRide);
        txtDirectionSecStartRide = view.findViewById(R.id.tvprimeinstructionsStartRide);
        llStartRideSec = view.findViewById(R.id.llStartRideSec);

        /*---Start ride bottom sheet end----*/

        imgMarkascompletearrow = view.findViewById(R.id.imgMarkascompletearrow);
        imgDirectionIconMarkComplete = view.findViewById(R.id.ivnextdirection);//for bottom sheet
        tvPrimeInstructions = view.findViewById(R.id.tvprimeinstructions);//for bottom sheet

        tvNextDistanceMarkComplete = view.findViewById(R.id.tvremainingstepdistance);//for bottom sheet
        tvArrivalTimeMarkComplete = view.findViewById(R.id.tvarrivaltime);
        tvTotDistanceAndETAMarkAsCmplte = view.findViewById(R.id.tvtotaldistETA);
        tvStartLocationMarkAsCmplte = view.findViewById(R.id.tvstartlocationcmplte);
        tvEndLocationMarkAsCmplte = view.findViewById(R.id.tvendlocationcmplte);
        tvUserNameMarkAsCmplte = view.findViewById(R.id.tvusrnamemarkascmplte);
        tvUserRateMarkAsCmplte = view.findViewById(R.id.tvusrratemarkascmplte);
        imgusrnamemarkascmplte = view.findViewById(R.id.imgusrnamemarkascmplte);
        tvConfirmCodeMarkAsCmplte = view.findViewById(R.id.tvconfirmcodemarkascmplte);
        imgMarkascompleteArrowSec = view.findViewById(R.id.ivnextdirectionMarkCompleteSec);
        txtMarkascompleteSec = view.findViewById(R.id.tvprimeinstructionsMarkCompleteSec);
        llMarkAsCompleteSec = view.findViewById(R.id.llMarkCompleteSec);

        llStartRideContactview.setOnClickListener(this);
        llStartRidePickUpDetails.setOnClickListener(this);
        imgMarkascompletearrow.setOnClickListener(this);
        btnSos.setOnClickListener(this);
        btnRideSos.setOnClickListener(this);
        llOnline.setOnClickListener(this);
        btnRideStart.setOnClickListener(this);
        btnRideCancel.setOnClickListener(this);
        btnRideDetailAccept.setOnClickListener(this);
        btnRideDetailDecline.setOnClickListener(this);
        btnMarkasComplete.setOnClickListener(this);
        btnExtendPass.setOnClickListener(this);

        // click event for show-dismiss bottom sheet
        view.findViewById(R.id.imgDrawer).setOnClickListener(this);
        // click event for show-dismiss bottom sheet
        view.findViewById(R.id.imgNotification).setOnClickListener(this);
    }

    private void registerAllBroadcast() {

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(YelloDriverApp.getInstance()).registerReceiver(broadCastRideRequest,
                new IntentFilter(Constants.INTENT_KEY_ACTION_RIDE_REQUEST_SUBSCRIPTION));

//        LocalBroadcastManager.getInstance(YelloDriverApp.getInstance()).registerReceiver(broadCastLocationUpdate,
//                new IntentFilter(Constants.INTENT_KEY_ACTION_LOCATION_UPDATE));
    }


    private void unregisterAllBroadcast() {
        if (broadCastRideRequest != null) {
            LocalBroadcastManager.getInstance(YelloDriverApp.getInstance()).unregisterReceiver(broadCastRideRequest);
        }
//        if (broadCastLocationUpdate != null) {
//            LocalBroadcastManager.getInstance(YelloDriverApp.getInstance()).unregisterReceiver(broadCastLocationUpdate);
//        }
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_home;
    }

    private final BroadcastReceiver broadCastRideRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            YLog.e("receiver", "Got message: ");

            if (intent != null) {
                if (intent.getAction() != null) {
                    YLog.e("SUBSCRIPTION", intent.getAction().toString());
                    if (intent.getAction().equals(Constants.INTENT_KEY_ACTION_RIDE_REQUEST_SUBSCRIPTION)) {
                        if (intent.getParcelableArrayListExtra(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION) != null) {
                            if (intent.getParcelableArrayListExtra(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION).size() > 0) {
                                listRideRequest = intent.getParcelableArrayListExtra(Constants.INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION);
                                rideRequestReceived(listRideRequest);
                            } else {
                                if (listRideRequest.size() > 0 && rideDetailsBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                    declineOrCancelTrip();

                                    final KAlertDialog pDialog = new KAlertDialog(mActivity, KAlertDialog.ERROR_TYPE)
                                            .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                                            .setContentText(YelloDriverApp.getInstance().getString(R.string.ride_cancel_by_user))
                                            .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                                    pDialog.show();
                                    pDialog.setCancelable(true);
                                    pDialog.setCanceledOnTouchOutside(true);
                                }
                                YLog.e(TAG, "Getting empty response from ride subscription so ignore it.");
                            }
                        }
                    }
                }
            }
        }
    };

//    private BroadcastReceiver broadCastLocationUpdate = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if (intent != null) {
//                if (intent.getAction() != null) {
//                    if (intent.getAction().equals(Constants.INTENT_KEY_ACTION_LOCATION_UPDATE)) {
//                        if (intent.getParcelableExtra(Constants.INTENT_KEY_DATA_LOCATION_UPDATE) != null) {
//                            updatedLocation = intent.getParcelableExtra(Constants.INTENT_KEY_DATA_LOCATION_UPDATE);
//
//                            updateLocation(updatedLocation);
//                        }
//                    }
//                }
//            }
//        }
//    };

    private void rideRequestReceived(ArrayList<RideRequestModel.Data.YtRideRequest> listRideRequest) {
        if (listRideRequest != null) {
            if (listRideRequest.size() > 0) {

                rideDetailsBehavior.setHideable(false);//Important to add
                rideDetailsBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                preventClosing = true;
                txtRideUserName.setText(listRideRequest.get(0).getRide().getUser().getFullName());
                txtRidePickup.setText(listRideRequest.get(0).getRide().getStartAddress().getLine1());

                rideRequestId = listRideRequest.get(0).getId();
                rideId = listRideRequest.get(0).getRideId();

                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_RIDE_ID, rideId);
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_RIDE_REQUEST_ID, rideRequestId);

                //get riderequestid and rideID
//                btnRideDetailDecline.setTag(listRideRequest.get(0).getId() + "##" + listRideRequest.get(0).getRideId());
//                btnRideDetailAccept.setTag(listRideRequest.get(0).getId() + "##" + listRideRequest.get(0).getRideId());

                DecimalFormat df = new DecimalFormat("#.#");
                String rounded = df.format(listRideRequest.get(0).getRide().getUser().getAverageRate());
//                txtRideRate.setText(rounded);

                if (listRideRequest.get(0).getRide().getUser().getProfilePhotoFileId() != null) {
                    ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), imgRideUser, listRideRequest.get(0).getRide().getUser().getProfilePhotoFileId(), R.drawable.ic_place_holder_user);
                }
                if (!rounded.equals("0")) {
                    txtRideRate.setVisibility(View.VISIBLE);
                    txtRideRate.setText(rounded);
                } else {
                    txtRideRate.setVisibility(View.GONE);
                }

                //            ImageLoader.loadImage(mActivity,imgRideUser,);imgRideUser;

                String lat = null;
                String lon = null;
                Point driverSource = null;
                Point riderDestination = null;

                String[] location = Utils.getLatLongFromString(listRideRequest.get(0).getRide().getStartLocation());
                if (location != null && location.length > 0) {
                    if (location[0] != null) {
                        lat = location[0];
                    }
                    if (location[1] != null) {
                        lon = location[1];
                    }
                }

                if (lat != null && lon != null) {
                    riderDestination = Point.fromLngLat(Double.valueOf(lat), Double.valueOf(lon));
                }

//                getDriverLocation();

                driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

                driverSource = Point.fromLngLat(driverLongitude, driverLatitude);

                calculateDistance(driverSource, riderDestination);
            }
        } else {

        }
    }

    Observer<List<OnlineVehicleModel.Data.YtVehicle>> observerGetVehicleStatus = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (listContent != null && listContent.size() > 0) {

                String myVehicleId = listContent.get(0).getId();
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_VEHICLE_ID, myVehicleId);
                boolean isVehicleOnline = listContent.get(0).getOnline();
                YLog.e("Vehicle Received Status", isVehicleOnline + "");


                Input<String> inputSlug = new Input<>(Constants.CANCEL_SLUG_NAME, true);
                homeViewmodel.getTripCancelReason(new GetCancelReasonQuery(inputSlug));

                if (isVehicleOnline) {

                    swipeView.setText(YelloDriverApp.getInstance().getString(R.string.you_are_online));
                    imgOnline.setImageDrawable(ContextCompat.getDrawable(YelloDriverApp.getInstance(), R.drawable.drawable_circle_online));

                    swipeView.swipe_reverse = true;
                    swipeView.setreverse_180();
                    swipeView.invalidate();

                    new Handler().postDelayed(() -> {
                        txtOnline.setText(YelloDriverApp.getInstance().getString(R.string.you_are_online));
                        llOnline.setVisibility(View.VISIBLE);
                        llOnlineOffline.setVisibility(View.GONE);
                    }, 1000);

                    if (!Utils.isServiceRunning(ForegroundService.class)) {
                        startForegroundService();
                    }

                    if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_NAV_TO_RIDER, false)) {
                        //Pre-Ride Navigation is on going, setup the changes

                        // start ride status check
                        homeViewmodel.checkRideStatus(new RideStatusCheckQuery(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDE_ID, "")));
                    } else if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
                        //Main Navigation is on going, setup the changes

                        // start ride status check
                        homeViewmodel.checkRideStatus(new RideStatusCheckQuery(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDE_ID, "")));
                    }

                } else {

                    swipeView.setText(YelloDriverApp.getInstance().getString(R.string.you_are_offline));
                    imgOnline.setImageDrawable(ContextCompat.getDrawable(YelloDriverApp.getInstance(), R.drawable.drawable_circle_offline));

                    swipeView.setreverse_0();
                    swipeView.invalidate();

                    new Handler().postDelayed(() -> {
                        txtOnline.setText(YelloDriverApp.getInstance().getString(R.string.you_are_offline));
                        llOnline.setVisibility(View.VISIBLE);
                        llOnlineOffline.setVisibility(View.GONE);
                    }, 1000);

                    if (Utils.isServiceRunning(ForegroundService.class)) {
                        stopForegroundService();
                    }

                    // If user is offline send them local notificaton
                    generateLocaleNotification(YelloDriverApp.getInstance().getString(R.string.please_go_online));

                }

            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };

    Observer<List<VehicleStatusModel.Data.Update_yt_vehicle.Returning>> observerSetVehicleStatus = listContent -> {

        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            if (listContent != null) {
                boolean userOnlineStatus = false;
                for (int q = 0; q < listContent.size(); q++) {
                    userOnlineStatus = listContent.get(q).getOnline();
                    YLog.e("New Status set", userOnlineStatus + "");
                }

                if (userOnlineStatus) {
                    if (!Utils.isServiceRunning(ForegroundService.class)) {
                        startForegroundService();
                    }
                } else {
                    if (Utils.isServiceRunning(ForegroundService.class)) {
                        stopForegroundService();
                    }
                }

            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };

    Observer<MyProfileModel.Data.YtUser> observerProfile = userContentList -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            vehicleStatusViewModel.getUserStatus(new IsVehicleOnlineQuery());

            if (userContentList != null) {

                //listUser = userContentList;
                if (mActivity != null) {
                    ((HomeActivity) mActivity).setProfileData(userContentList);
                }
            } else {

            }
        }
    };

    Observer<RideAcceptModel.Data.AcceptRide> observerAcceptRideRequest = acceptRideRequestResponse -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (acceptRideRequestResponse != null) {
                //Hide this sheet after Accept ride for now it is hidden instantly
                rideDetailsBehavior.setHideable(true);//Important to add
                rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


                YLog.e("Ride Accept", "Ride Accept");
                Input<Boolean> isBooked = new Input<>(true, true);
                String vehicleId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID, "");
                vehicleStatusViewModel.setBookedStatus(new SetVehicleBookedMutation(isBooked, vehicleId));

                if (listRideRequest != null) {
                    if (listRideRequest.size() > 0) {

                        RideRequestModel.Data.YtRideRequest currentRideInfo = listRideRequest.get(0);

                        /*PRE-RIDE ROUTE INFORMATION SECTION*/
                        if (currentRideInfo.getRide().getStartLocation() != null) {

                            String slat = null;
                            String slon = null;

                            String[] startlocation = Utils.getLatLongFromString(currentRideInfo.getRide().getStartLocation());
                            if (null != startlocation && startlocation.length > 0) {
                                if (startlocation[0] != null) {
                                    slat = startlocation[0];
                                }
                                if (startlocation[1] != null) {
                                    slon = startlocation[1];
                                }
                            }

                            Point riderPickupPoint = null;
                            if (slat != null && slon != null) {
                                riderPickupPoint = Point.fromLngLat(Double.valueOf(slat), Double.valueOf(slon));
                            }


                            String elat = null;
                            String elon = null;

                            String[] endlocation = Utils.getLatLongFromString(currentRideInfo.getRide().getEndLocation());
                            if (null != endlocation && endlocation.length > 0) {
                                if (endlocation[0] != null) {
                                    elat = endlocation[0];
                                }
                                if (endlocation[1] != null) {
                                    elon = endlocation[1];
                                }
                            }

                            Point riderDestinationPoint = null;
                            if (elat != null && elon != null) {
                                riderDestinationPoint = Point.fromLngLat(Double.valueOf(elat), Double.valueOf(elon));
                            }


                            driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                            driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

                            Point myLocationSource = Point.fromLngLat(driverLongitude, driverLatitude);

                            String addrsStart = currentRideInfo.getRide().getStartAddress().getLine1();
                            String addrsEnd = currentRideInfo.getRide().getEndAddress().getLine1();

                            StoredRoute routeToRider = new StoredRoute(riderPickupPoint, riderDestinationPoint, myLocationSource, "ASSIGNED", addrsStart, addrsEnd);
                            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_ONGOING_ROUTE, routeToRider);

                            /*RIDER INFORMATION SECTION*/
                            String riderName = currentRideInfo.getRide().getUser().getFullName();
                            String riderUserId = currentRideInfo.getRide().getUserId();
                            String riderMobile = currentRideInfo.getRide().getUser().getMobile() + "";
                            String imageUrl = currentRideInfo.getRide().getUser().getProfilePhotoFileId();
                            String boardingPassId = currentRideInfo.getRide().getBoarding_pass_id();

                            DecimalFormat df = new DecimalFormat("#.#");
                            String rounded = df.format(currentRideInfo.getRide().getUser().getAverageRate());

                            StoredRiderInfo riderInfo = new StoredRiderInfo(riderUserId, riderName, rounded, imageUrl, riderMobile, boardingPassId);
                            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_RIDER_INFO, riderInfo);
                            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_BOARDING_PASS_ID, boardingPassId);

                            /*RIDE STATUS VALUES*/
                            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_RIDER_USER_ID, riderUserId);

                            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_CURRENT_RIDE_ID, currentRideInfo.getRideId());

                            //Do this to update bottom sheet
                            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAV_TO_RIDER, true);

                            renderPreRideNavigation(routeToRider, riderInfo);

                            // start ride status subscription here and stop subscription here..
                            homeViewmodel.getRideRequestSubscription(listRideRequest.get(0).getRideId());
                        }
                    }
                }
            } else {
                YLog.e("Error", "Response null while accepting ride");
                rideDetailsBehavior.setHideable(true);//Important to add
                rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    };

    Observer<RideRejectModel.Data.RejectRide> observerApoloResponseRejection = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (listContent != null) {
                /*IMPORTANT TO REMOVE UPDATES*/
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAV_TO_RIDER, false);

                String recordId = listContent.getId();
                YLog.e("Rejected", recordId + "");
                SuccessDialog(YelloDriverApp.getInstance().getString(R.string.msg_riderejectsuccess));

                rideDetailsBehavior.setHideable(true);//Important to add
                rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                preventClosing = false;

            } else {

                Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    };

    Observer<ModelUploadRide.Data.UploadRideRouteMap> observerRideRoute = uploadRideRouteMap -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            submitRideImageToAWS(uploadRideRouteMap, routeBitmapImage);
            YLog.e("FILE_ID", uploadRideRouteMap.getFileUploadId());
        }
    };

    Observer<List<ModelRideStatus.Data.YtRide>> observerRideStatus = listRideStatus -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (listRideStatus != null && listRideStatus.size() > 0) {

                if (listRideStatus.get(0).getStatus().equals(Constants.RIDE_STATUS_CANCELLED)) {
                    // if get status is canceled from server about this ride then cancel navigation flow and stop subscription here...
                    declineOrCancelTrip();

                    final KAlertDialog pDialog = new KAlertDialog(mActivity, KAlertDialog.ERROR_TYPE)
                            .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                            .setContentText(YelloDriverApp.getInstance().getString(R.string.ride_cancel_by_user))
                            .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                    pDialog.show();
                    pDialog.setCancelable(true);
                    pDialog.setCanceledOnTouchOutside(true);
                }
            }
        }
    };

    Observer<List<ModelRideStatus.Data.YtRide>> observerCheckRideStatus = listRideStatusCheck -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (listRideStatusCheck != null && listRideStatusCheck.size() > 0) {

                if (listRideStatusCheck.get(0).getStatus().equals(Constants.RIDE_STATUS_CANCELLED)) {
                    // if get status is canceled from server about this ride then cancel navigation flow and stop subscription here...
                    declineOrCancelTrip();

                    final KAlertDialog pDialog = new KAlertDialog(mActivity, KAlertDialog.ERROR_TYPE)
                            .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                            .setContentText(YelloDriverApp.getInstance().getString(R.string.previous_ride_cancel))
                            .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                    pDialog.show();
                    pDialog.setCancelable(true);
                    pDialog.setCanceledOnTouchOutside(true);
                } else {
                    if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_NAV_TO_RIDER, false)) {
                        //Pre-Ride Navigation is on going, setup the changes
                        StoredRoute preNavRoute = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ONGOING_ROUTE, new StoredRoute());
                        StoredRiderInfo riderInfo = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_INFO, new StoredRiderInfo());
                        renderPreRideNavigation(preNavRoute, riderInfo);
                    } else if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
                        //Main Navigation is on going, setup the changes
                        StoredRoute mainNavRoute = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ONGOING_ROUTE, new StoredRoute());
                        StoredRiderInfo riderInfo = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_INFO, new StoredRiderInfo());
                        renderMainRideNavigation(mainNavRoute, riderInfo);
                    }
                }
            }
        }
    };

    Observer<ModelDriverArrived.Data.VehicleArrived> observerDriverArrived = vehicleArrived -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (vehicleArrived != null) {

            }
        }
    };

    Observer<ArrayList<ModelVehicleLocation.Data.YtVehicleLocation>> observerVehicleLocation = alVehicleLocations -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (alVehicleLocations != null && alVehicleLocations.size() > 0) {
                captureScreen(alVehicleLocations);
            }
        }
    };

    Observer<RideCompleteModel.Data.CompleteRide> observerRideCompletion = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }
            Log.d("RESPONSE_RIDE", "Received");
            YLog.e("ONGOING_RIDE", String.valueOf(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, true)));
            if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, true)) {
                if (listContent != null) {
                    btnRideSos.setVisibility(View.GONE);
                    /*IMPORTANT TO REMOVE UPDATES*/
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAV_TO_RIDER, false);

                    // Manage this flag to make sure ride is running and in it's complete state don't forgot to
                    // set his value false
                    //Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);

                    /*Mark the vehicle is NOT Booked*/
                    Input<Boolean> isBooked = new Input<>(false, true);
                    String vehicleId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID, "");
                    vehicleStatusViewModel.setBookedStatus(new SetVehicleBookedMutation(isBooked, vehicleId));

                    String recordId = listContent.getId();
                    YLog.e("Completed", recordId + "");

                    try {
                        // Manage this flag to make sure ride is running and in it's complete state don't forgot to
                        // set his value false
                        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);

                        navigation.stopTripSession();
                        navigationMap.hideRoute();
                        navigationMap.clearMarkers();
                        ivPlayVoiceInstructions.setVisibility(View.GONE);

                        locationComponent = mapboxMap.getLocationComponent();
                        locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
                        // Set the component's render mode
                        locationComponent.setRenderMode(RenderMode.GPS);

                        mapboxMap.easeCamera(CameraUpdateFactory.tiltTo(0));

                        // cancel ride status subscription here after ride complete..
                        homeViewmodel.cancelRideStatusSubscription();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    behaviorRideAsComplete.setHideable(true);
                    behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_HIDDEN);
                    RatingDialog();

                    // Making async request to generate map capture image
                    Input<Object> onGoingRideId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, ""), true);
                    Input<Object> driverUserId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, ""), true);

                    VehicleLocationByRideQuery vehicleLocationByRideQuery = new VehicleLocationByRideQuery(driverUserId, onGoingRideId);
                    homeViewmodel.getVehicleLocationByRide(vehicleLocationByRideQuery);



            /*rideDetailsBehavior.setHideable(true);//Important to add
            rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);*/

                } else {

                    //Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                navigation.stopTripSession();
                navigationMap.hideRoute();
                navigationMap.clearMarkers();
                ivPlayVoiceInstructions.setVisibility(View.GONE);

                locationComponent = mapboxMap.getLocationComponent();
                locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
                // Set the component's render mode
                locationComponent.setRenderMode(RenderMode.GPS);

                mapboxMap.easeCamera(CameraUpdateFactory.tiltTo(0));
                behaviorRideAsComplete.setHideable(true);
                behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    };

    private void renderPreRideNavigation(StoredRoute routeToRider, StoredRiderInfo riderInfo) {

        //Show this sheet after the ride is accepted
        startRidebehavior.setHideable(false);
        startRidebehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        txtStartRidePickup.setText(routeToRider.getStartAddrs());

        txtStartRideUserName.setText(riderInfo.getRiderName());
//        imgStartRideCallUser.setTag(riderInfo.getRiderMobile());

        if (riderInfo.getRiderImage() != null) {
            ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), imgStartRideUser, riderInfo.getRiderImage(), R.drawable.ic_place_holder_user);
        }
        if (!riderInfo.getRiderRating().equals("0")) {
            txtStartRideRate.setVisibility(View.VISIBLE);
            txtStartRideRate.setText(riderInfo.getRiderRating());
        } else {
            txtStartRideRate.setVisibility(View.GONE);
        }

        //acceptRideRequest.getRide().getUser().getProfile_photo();
        // ImageLoader.loadImage(mActivity,imgStartRideUser,);

        Point riderPickupPoint = null;

        riderPickupPoint = routeToRider.getStartPoint();

        driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
        driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

        Point myLocationSource = Point.fromLngLat(driverLongitude, driverLatitude);

        navigateToDestination(myLocationSource, riderPickupPoint);
    }

    private void renderMainRideNavigation(StoredRoute storedNavigationRoute, StoredRiderInfo storedRiderInfo) {

        behaviorRideAsComplete.setHideable(false);
        behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //btnMarkasComplete.setTag(recordId + "");

        String strtRideAddr = storedNavigationRoute.getStartAddrs();
        String endRideAddr = storedNavigationRoute.getEndAddrs();

        tvUserNameMarkAsCmplte.setText(storedRiderInfo.getRiderName());
        tvUserRateMarkAsCmplte.setText(storedRiderInfo.getRiderRating());

        if (storedRiderInfo.getRiderImage() != null) {
            ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), imgusrnamemarkascmplte, storedRiderInfo.getRiderImage(), R.drawable.ic_place_holder_user);
        }
        if (!storedRiderInfo.getRiderRating().equals("0")) {
            tvUserRateMarkAsCmplte.setVisibility(View.VISIBLE);
            tvUserRateMarkAsCmplte.setText(storedRiderInfo.getRiderRating());
        } else {
            tvUserRateMarkAsCmplte.setVisibility(View.GONE);
        }

        String confirmCode = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_QR_CONFIRM_CODE, "");
        tvConfirmCodeMarkAsCmplte.setText(confirmCode);

        tvStartLocationMarkAsCmplte.setText(strtRideAddr + "");
        tvEndLocationMarkAsCmplte.setText(endRideAddr + "");

        Point riderDestinationPoint = storedNavigationRoute.getEndPoint();
        Point riderPickUpPoint = storedNavigationRoute.getStartPoint();

        //getDriverLocation();
        driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
        driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

        Point myLocationSource = Point.fromLngLat(driverLongitude, driverLatitude);

        StoredRoute routeToDestination = new StoredRoute(riderPickUpPoint, riderDestinationPoint, myLocationSource, "INPROGRESS", strtRideAddr, endRideAddr);
        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_ONGOING_ROUTE, routeToDestination);

        navigateToDestination(myLocationSource, riderDestinationPoint);
    }


    private void acceptRide() {

        if (listRideRequest != null && listRideRequest.size() > 0) {

            String driverUserId = listRideRequest.get(0).getDriverUserId() == null ? "" : listRideRequest.get(0).getDriverUserId();
//            String etaUnit = listRideRequest.get(0).getEtaUnit() == null ? "" : listRideRequest.get(0).getEtaUnit();
            String etaUnit = YelloDriverApp.getInstance().getString(R.string.minutes);
            String id = listRideRequest.get(0).getId() == null ? "" : listRideRequest.get(0).getId();
            String rideId = listRideRequest.get(0).getRideId() == null ? "" : listRideRequest.get(0).getRideId();
            String userId = listRideRequest.get(0).getRide().getUserId() == null ? "" : listRideRequest.get(0).getRide().getUserId();
//            double etaNumber = listRideRequest.get(0).getEtaNumber() == null ? 0.0 : listRideRequest.get(0).getEtaNumber();
            double etaNumberValue = etaNumber;

            customProgressDialog = new CustomProgressDialog(mActivity);
            customProgressDialog.showDialog();

            Mutation mutationAcceptRide = new AcceptRideMutation(driverUserId, etaUnit, id, rideId, userId, etaNumberValue);
            homeViewmodel.acceptRide(mutationAcceptRide);
        }
    }

    private void rideRejectReqServer(String rideReqId, String rideId) {

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        RideRejectViewModel rideRejectViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(RideRejectViewModel.class);
        rideRejectViewModel.mutableApoloResponse.observe(((HomeActivity) mActivity), observerApoloResponseRejection);

        rideRejectViewModel.setRejectRide(new RideRejectMutation(rideReqId, rideId));
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        checkForLocationPermission();
    }

    private void checkForLocationPermission() {
        boolean granted = PermissionUtils.isGranted(mActivity, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.ACCESS_COARSE_LOCATION);
        if (granted) {
            checkForGpsLocation();
        } else {
            PermissionManager.Builder()
                    .permission(PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.ACCESS_COARSE_LOCATION)
                    .key(Constants.LOCATION_REQUEST)
                    .askAgain(true)
                    .askAgainCallback(new AskAgainCallback() {
                        @Override
                        public void showRequestPermission(UserResponse response) {
                            showDialogForDenyForever();
                        }
                    }).callback(new SmartCallback() {
                @Override
                public void result(boolean allPermissionsGranted, boolean somePermissionsDeniedForever) {
                    if (allPermissionsGranted) {
                        checkForLocationPermission();
                    } else if (somePermissionsDeniedForever) {
                        YLog.e("Permission", "Denied Forever");
                        showDialogForDenyForever();
                    }
                }
            }).ask(HomeFragment.this);
        }
    }

    private void checkForGpsLocation() {

        final LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
//        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//        if (isNetworkEnabled && isGpsEnabled) {
        if (isGpsEnabled) {
            onMapReadyCallback(mapboxMap);
        } else {
            showGpsLocationDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.handleResult(HomeFragment.this, requestCode, permissions, grantResults);
    }

    private void showGpsLocationDialog() {
        if (mActivity == null) return;
        new AlertDialog.Builder(mActivity, R.style.StyleCommonDialog)
                .setTitle(YelloDriverApp.getInstance().getString(R.string.gps_permission_needed))
                .setMessage(YelloDriverApp.getInstance().getString(R.string.gps_permission_needed_explaination))
                .setPositiveButton(YelloDriverApp.getInstance().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.GPS_REQUEST);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogForDenyForever() {
        if (mActivity == null) return;
        new AlertDialog.Builder(mActivity, R.style.StyleCommonDialog)
                .setTitle(YelloDriverApp.getInstance().getString(R.string.location_permission_needed))
                .setMessage(YelloDriverApp.getInstance().getString(R.string.location_permission_explanation))
                .setPositiveButton(YelloDriverApp.getInstance().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, Constants.LOCATION_REQUEST_SETTINGS);
                    }
                })
                .setCancelable(false)
                .show();

    }

    private void onMapReadyCallback(MapboxMap mapboxMap) {
//        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//
        mapboxMap.setStyle(new Style.Builder().fromUri(YelloDriverApp.getInstance().getString(R.string.map_style)), style -> {
            navigationMap = new NavigationMapboxMap(mapView, mapboxMap, getViewLifecycleOwner(), true);

            trafficPlugin = new TrafficPlugin(mapView, mapboxMap, style);
//            // Enable the traffic view by default
            trafficPlugin.setVisibility(true);

            // For navigation logic / processing
            initializeNavigation(mapboxMap);
            // navigationMap.updateCameraTrackingMode(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS);
            //navigationMap.updateLocationLayerRenderMode(RenderMode.GPS);

            LocationComponentOptions locationComponentOptions =
                    LocationComponentOptions.builder(YelloDriverApp.getInstance())
                            .pulseEnabled(true)
                            .pulseColor(ContextCompat.getColor(YelloDriverApp.getInstance(), R.color.colorPrimary))
                            .pulseAlpha(.4f)
                            .elevation(3)
                            .accuracyAlpha(.6f)
                            .accuracyColor(Color.TRANSPARENT)
                            .gpsDrawable(R.drawable.img_car)
                            .foregroundDrawable(R.drawable.img_car)
                            .bearingDrawable(R.drawable.img_car)
//                            .pulseInterpolator(new BounceInterpolator())
                            .build();

            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(YelloDriverApp.getInstance(), style)
                    .locationComponentOptions(locationComponentOptions)
                    .build();

            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);

            // For voice instructions
            initializeSpeechPlayer();
            isMapLoaded = true;

            setUpAllDataOnMapReady();
        });
    }

    private void setRenderModeNormal() {
        if (locationComponent != null) {
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }
    }

    private void setRenderModeGPS() {
        if (locationComponent != null) {
            locationComponent.setRenderMode(RenderMode.GPS);
        }
    }

    private void setRenderModeRecenter() {
        if (locationComponent != null) {
            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }
    }

    private void initializeSpeechPlayer() {
        String language = "en";
        /*String devicelng = Locale.getDefault().getDisplayLanguage();
        if (devicelng.equals("Deutsch")) {
            language = "de";
        }*/
        Cache cache = new Cache(new File(YelloDriverApp.getInstance().getCacheDir(), COMPONENT_NAVIGATION_INSTRUCTION_CACHE),
                TEN_MEGABYTE_CACHE_SIZE);
        VoiceInstructionLoader voiceInstructionLoader = new VoiceInstructionLoader(YelloDriverApp.getInstance(),
                YelloDriverApp.getInstance().getString(R.string.mapbox_access_token), cache);
        SpeechPlayerProvider speechPlayerProvider = new SpeechPlayerProvider(YelloDriverApp.getInstance(), language, true,
                voiceInstructionLoader);
        speechPlayer = new NavigationSpeechPlayer(speechPlayerProvider);
        if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ALLOW_VOICE_PLAY, false)) {
            speechPlayer.setMuted(false);
        } else {
            speechPlayer.setMuted(true);
        }
    }

    private void initializeNavigation(MapboxMap mapboxMap) {

        routeReplayLocationEngine = new ReplayRouteLocationEngine();

        NavigationOptions.Builder optionsBuilder = new NavigationOptions.Builder(YelloDriverApp.getInstance())
                .accessToken(YelloDriverApp.getInstance().getString(R.string.mapbox_access_token))
                .isFromNavigationUi(false)
                .distanceFormatter(new MapboxDistanceFormatter.Builder(YelloDriverApp.getInstance())
                        .unitType(VoiceUnit.METRIC)
                        .roundingIncrement(Rounding.INCREMENT_FIFTY)
                        .build())
                .timeFormatType(TWELVE_HOURS);

        if (isAutoDriveRequired) {
            optionsBuilder.locationEngine(routeReplayLocationEngine);
        }

        NavigationOptions navigationOptions = optionsBuilder.build();

        navigation = new MapboxNavigation(navigationOptions);
        navigation.registerOffRouteObserver(this);
        navigation.registerBannerInstructionsObserver(this);
        navigation.registerRouteProgressObserver(this);
        navigation.registerVoiceInstructionsObserver(this);
        navigation.registerArrivalObserver(this);

        navigationMap.addProgressChangeListener(navigation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.QR_SCAN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data) {
                    String dataFromQR = data.getStringExtra("qrcodedata");
//                    Toast.makeText(mActivity, dataFromQR, Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject qrObject = new JSONObject(dataFromQR);

                        String confirmCode = qrObject.getString("confirmation_code");
                        String rideId = qrObject.getString("ride_id");
                        if (rideId.equalsIgnoreCase(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, ""))) {
                            sendQRConfirmCodeToServer(confirmCode, rideId);
                        } else {
                            Toast.makeText(mActivity, "Ride does not match", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (requestCode == Constants.DECLINE_TRIP_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                declineOrCancelTrip();
            }
        }

        if (requestCode == Constants.LOCATION_REQUEST_SETTINGS) {
            checkForLocationPermission();
        }

        if (requestCode == Constants.GPS_REQUEST) {
            checkForGpsLocation();
        }

        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                    //Permission is not available. Display error text.
                    //getActivity().finish();
                }
            }
        }
    }

    private void declineOrCancelTrip() {
        if (startRidebehavior != null) {
            startRidebehavior.setHideable(true);
            startRidebehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        if (rideDetailsBehavior != null) {
            rideDetailsBehavior.setHideable(true);
            rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        if (behaviorRideAsComplete != null) {
            behaviorRideAsComplete.setHideable(true);
            behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        /*IMPORTANT TO REMOVE UPDATES*/
        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAV_TO_RIDER, false);
        // Manage this flag to make sure ride is running and in it's complete state don't forgot to
        // set his value false
        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);

        googleMapNavigationButton.setVisibility(View.INVISIBLE);
        btnRideSos.setVisibility(View.GONE);
        Input<Boolean> isBooked = new Input<>(false, true);
        String vehicleId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID, "");
        vehicleStatusViewModel.setBookedStatus(new SetVehicleBookedMutation(isBooked, vehicleId));

        try {

            setRenderModeNormal();
            navigationMap.clearMarkers();
            navigationMap.hideRoute();
            navigation.stopTripSession();
            ivPlayVoiceInstructions.setVisibility(View.GONE);
            mapboxMap.easeCamera(CameraUpdateFactory.tiltTo(0));

            homeViewmodel.cancelRideStatusSubscription();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendQRConfirmCodeToServer(String confirmCode, String rideId) {

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        RideStartViewModel rideStartViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(RideStartViewModel.class);
        rideStartViewModel.mutableApoloResponse.observe(((HomeActivity) mActivity), observerApoloResponseQRsend);

        /*"confirmation_code": "RC-6",
        "ride_id": "ee32528f-9576-4950-94ac-eb4d23a23cbe",
        "vehicle_id": "32a27fe5-d705-406d-bded-153bbb8f5a91"*/

        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_QR_CONFIRM_CODE, confirmCode + "");
        String vehicleId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID, "");

        rideStartViewModel.sendQRToStartRide(new RideStartByQRMutation(confirmCode, rideId, vehicleId));
    }

    Observer<RideStartModel.Data.StartRide> observerApoloResponseQRsend = listContent -> {

        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (listContent != null) {

            startRidebehavior.setHideable(true);
            startRidebehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            setRenderModeNormal();
            //Clear Pre-ride navigation route
            navigation.stopTripSession();
            navigationMap.hideRoute();
            navigationMap.clearMarkers();
            mapboxMap.easeCamera(CameraUpdateFactory.tiltTo(0));

            String recordId = listContent.getId();
            YLog.e("Start Ride Done", recordId + "");
            SuccessDialog(YelloDriverApp.getInstance().getString(R.string.msg_ridestartsuccess));
            //Do this to update bottom sheet
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAV_TO_RIDER, false);

            // Manage this flag to make sure ride is running and in it's complete state don't forgot to
            // set his value false
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_IS_RIDE_ONGOING, true);


            StoredRoute storedNavigationRoute = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ONGOING_ROUTE, new StoredRoute());
            StoredRiderInfo storedRiderInfo = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_INFO, new StoredRiderInfo());

            /*----Start navigation from start location to end location----*/
            if (storedNavigationRoute.getEndPoint() != null) {

                //RideRequestModel.Data.YtRideRequest rideReqInfo = listRideRequest.get(0);
                floatingBubble = false;
                renderMainRideNavigation(storedNavigationRoute, storedRiderInfo);

            }

            /*------------*/
        } else {
            //emptyNotifyview.setVisibility(View.VISIBLE);
            //rvMyNotifs.setVisibility(View.GONE);
            Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };

    public void RatingDialog() {
        Dialog rankDialog = new Dialog(mActivity, R.style.FullHeightDialog);
        rankDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rankDialog.setContentView(R.layout.ratingbar_dialog);
        rankDialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(4);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);

        StoredRiderInfo riderInfo = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_INFO, null);
        text.setText(riderInfo.getRiderName() + "");
        Button btnRatingLeave = (Button) rankDialog.findViewById(R.id.btnRatingLeave);
        btnRatingLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankDialog.dismiss();
            }
        });
        Button btnRatingSubmit = (Button) rankDialog.findViewById(R.id.btnRatingSubmit);
        btnRatingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRatingToServer(ratingBar.getRating());
                rankDialog.dismiss();
            }
        });

        rankDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SuccessDialog(YelloDriverApp.getInstance().getString(R.string.ride_has_been_completed_successfully));
            }
        });

        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    private void sendRatingToServer(double givenRating) {

        RateRiderViewModel rateRiderViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(RateRiderViewModel.class);
        rateRiderViewModel.mutableApoloResponse.observe(((HomeActivity) mActivity), observerApoloResponseRating);

        Input<String> fromUserType = new Input<>("driver", true);

        Input<Object> fromUserId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, ""), true);
        Input<String> toUserType = new Input<>("rider", true);
        Input<Object> toUserId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_USER_ID, ""), true);
        Input<Object> rideId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, ""), true);
        Input<Double> givenRate = new Input<>(givenRating, true);

        rateRiderViewModel.setRiderRating(new GiveRatingToRiderMutation(fromUserType, fromUserId, toUserType, toUserId, rideId, givenRate));
    }

    Observer<RateRiderModel.Data.Insert_yt_rating_one> observerApoloResponseRating = listContent -> {
        if (listContent != null) {

            String recordId = listContent.getId();
            YLog.e("New Rating set", recordId + "");

        } else {
            //emptyNotifyview.setVisibility(View.VISIBLE);
            //rvMyNotifs.setVisibility(View.GONE);
            //Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };

    public void SuccessDialog(String message) {
        Dialog rideCompletedDialog = new Dialog(mActivity, R.style.FullHeightDialog);
        rideCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rideCompletedDialog.setContentView(R.layout.ride_success_dialog);

        TextView txtView = rideCompletedDialog.findViewById(R.id.success_dialog_txtMsg);
        txtView.setText(message);

        rideCompletedDialog.setCancelable(true);

        //now that the dialog is set up, it's time to show it
        rideCompletedDialog.show();

        new Handler().postDelayed(() -> {
            try {
                if (null != rideCompletedDialog)
                    rideCompletedDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMarkasComplete:
                //String onGoingRideId = view.getTag() + "";
//                String onGoingRideId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, "");
//                markRideAsCompleteToServer(onGoingRideId);

                if (Utils.isServiceRunning(FloatingButtonService.class)) {
                    Intent serviceIntent = new Intent(YelloDriverApp.getInstance(), FloatingButtonService.class);
                    YelloDriverApp.getInstance().stopService(serviceIntent);
                    floatingBubble = false;
                }

                customProgressDialog = new CustomProgressDialog(mActivity);
                customProgressDialog.showDialog();

                String onGoingRideId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, "");
                markRideAsCompleteToServer(onGoingRideId);

                break;

            case R.id.btnExtendPass:

                customProgressDialog = new CustomProgressDialog(mActivity);
                customProgressDialog.showDialog();

                String boardingPassId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_BOARDING_PASS_ID);
                BoardingPassAmountQuery boardingPassAmountQuery = new BoardingPassAmountQuery(boardingPassId);
                extendPassViewModel.getPassAmount(boardingPassAmountQuery);

                break;

            case R.id.bntRideDetailDecline:

                btnRideSos.setVisibility(View.GONE);
                rideDetailsBehavior.setHideable(true);//Important to add
                rideDetailsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

//                String cancelTag = view.getTag() + "";
//
//                if ("null".equalsIgnoreCase(cancelTag)) {
//                    cancelTag = "db7d0c8e-6a78-4792-b55a-926c6467c618##739e693e-9840-4815-a654-16630de66843";
//                }
//
//                String reqNId[] = cancelTag.split("##");

                rideRejectReqServer(rideRequestId, rideId);
                break;

            case R.id.llOnline:
                llOnline.setVisibility(View.GONE);
                llOnlineOffline.setVisibility(View.VISIBLE);

                new Handler().postDelayed(() -> {
                    llOnline.setVisibility(View.VISIBLE);
                    llOnlineOffline.setVisibility(View.GONE);
                }, 5000);

                break;
            case R.id.bntRideDetailAccept:
                if (driverLatitude != 0) {

                    /*Testing code for accept Ride*/
                /*Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAVTORIDER, true);
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAVTODESTINATION, false);
                source = Point.fromLngLat(70.7877, 22.2915);//kotecha
                destination = Point.fromLngLat(70.7881, 22.2996);//balbhavan
                navigateToDestination(source, destination);*/
                    /*Testing code ENDS*/

                    //Todo: Enable this for live accept
                    acceptRide();
                } else {
                    Toast.makeText(mActivity, "Location not available", Toast.LENGTH_SHORT).show();
                    driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                    driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);
                }
                break;
            case R.id.imgNotification:
                addFragment(R.id.activity_home_flContainer, HomeFragment.this, new MyNotificationsFragment(), false, false);
                break;
            case R.id.imgDrawer:
                ((HomeActivity) mActivity).openMenuDrawer();
                break;
            case R.id.llRiderContactview:
                startRidebehavior.setHideable(false);
                startRidebehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.imgMarkascompletearrow:
                behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_EXPANDED);
                imgMarkascompletearrow.setVisibility(View.GONE);
                break;

            case R.id.btnRideStart:
                /*startRidebehavior.setHideable(true);
                startRidebehavior.setState(BottomSheetBehavior.STATE_HIDDEN);*/

                /*Testing code to start actual ride*/
                /*Point driveSource = Point.fromLngLat(72.600227, 23.028521); // Ahmedabad railway station
                Point riderDestination = Point.fromLngLat(72.590390, 23.029480); // Isckon temple ahmedabad
                //Do this to update bottom sheet
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAVTORIDER, false);
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAVTODESTINATION, true);

                behaviorRideAsComplete.setHideable(false);
                behaviorRideAsComplete.setState(BottomSheetBehavior.STATE_COLLAPSED);
                navigateToDestination(driveSource,riderDestination);*/
                /*Testing code to start actual ride ENDS*/

                /*LIVE CODE for QR code navigation*/
                Fragment qrInfoFragment = new QRInfoFragment();
                qrInfoFragment.setTargetFragment(HomeFragment.this, Constants.QR_SCAN_REQUEST);
                addFragment(R.id.activity_home_flContainer, HomeFragment.this, qrInfoFragment, false, false);

                break;

            case R.id.btnRideCancel:
                //startRidebehavior.setHideable(true);
                //startRidebehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                Fragment declineTripFragment = new DeclineTripFragment();
                declineTripFragment.setTargetFragment(HomeFragment.this, Constants.DECLINE_TRIP_REQUEST);
                addFragment(R.id.activity_home_flContainer, HomeFragment.this, declineTripFragment, false, false);

                break;

            case R.id.btnSos:
                SOSDialog();
                break;
            case R.id.fragment_home_btnSos:
                SOSDialog();
                break;

            /*case R.id.imgStartRideDownarrow:
                startRidebehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;*/
        }
    }

    public void renewPassDialog() {
        Dialog renewPassDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        renewPassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        renewPassDialog.setContentView(R.layout.renew_pass_dialog);

        TextView txtPrice = renewPassDialog.findViewById(R.id.renewPass_txtPrice);

        RadioGroup rbGroup = renewPassDialog.findViewById(R.id.renewPass_rbGroup);
        RadioButton rbOneDay = renewPassDialog.findViewById(R.id.renewPass_rbOneDay);
        RadioButton rbTwoDay = renewPassDialog.findViewById(R.id.renewPass_rbTwoDay);
        RadioButton rbThreeDay = renewPassDialog.findViewById(R.id.renewPass_rbThreeDay);

        Button btnSubmit = renewPassDialog.findViewById(R.id.renewPassDialogBtnSubmit);
        Button btnNotNow = renewPassDialog.findViewById(R.id.renewPassDialogBtnNotNow);

        if (ytBoardingPassByPk != null) {
            txtPrice.setText(YelloDriverApp.getInstance().getString(R.string.pass_plan_amount, ytBoardingPassByPk.getPlan().getExtensionPrice1Day()));
            extendByDays = 1;
        }

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.renewPass_rbOneDay:
                        txtPrice.setText(YelloDriverApp.getInstance().getString(R.string.pass_plan_amount, ytBoardingPassByPk.getPlan().getExtensionPrice1Day()));
                        extendByDays = 1;
                        break;
                    case R.id.renewPass_rbTwoDay:
                        txtPrice.setText(YelloDriverApp.getInstance().getString(R.string.pass_plan_amount, ytBoardingPassByPk.getPlan().getExtensionPrice2Day()));
                        extendByDays = 2;
                        break;
                    case R.id.renewPass_rbThreeDay:
                        txtPrice.setText(YelloDriverApp.getInstance().getString(R.string.pass_plan_amount, ytBoardingPassByPk.getPlan().getExtensionPrice3Day()));
                        extendByDays = 3;
                        break;
                }
            }
        });

        rbGroup.check(R.id.renewPass_rbOneDay);

        btnSubmit.setOnClickListener(view -> {

            renewPassDialog.dismiss();
            createTokenMutation();
        });

        btnNotNow.setOnClickListener(view -> {
            renewPassDialog.dismiss();
        });

        renewPassDialog.setCancelable(true);

        //now that the dialog is set up, it's time to show it
        renewPassDialog.show();
    }

    private void markRideAsCompleteToServer(String onGoingRideId) {

        String rideId = onGoingRideId;
//        String routeMapFileId = "175824fe-f5fc-48cd-b16a-beeb84ec96cb";
        homeViewmodel.completeRide(new RideCompleteMutation(rideId));
    }

//    private void markRideAsCompleteToServer(String onGoingRideId, String routeMapFileId) {
//
//        String rideId = onGoingRideId;
//        String routeMapFileId = "175824fe-f5fc-48cd-b16a-beeb84ec96cb";
//        homeViewmodel.completeRide(new RideCompleteMutation(rideId, routeMapFileId));
//    }


    public void startForegroundService() {
        Intent serviceIntent = new Intent(YelloDriverApp.getInstance(), ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Yello Driver app notification");
        serviceIntent.putExtra("activity_background", true);
        ContextCompat.startForegroundService(YelloDriverApp.getInstance(), serviceIntent);
    }

    public void stopForegroundService() {
        Intent serviceIntent = new Intent(YelloDriverApp.getInstance(), ForegroundService.class);
        YelloDriverApp.getInstance().stopService(serviceIntent);

        unregisterAllBroadcast();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(YelloDriverApp.getCurrentActivity().getApplicationContext())) {
                if (!Utils.isServiceRunning(FloatingButtonService.class)) {
                    YelloDriverApp.getCurrentActivity().startService(new Intent(getActivity(), FloatingButtonService.class).putExtra("activity_background", true));
                }

            }

        }

    }

    public void navigateToDestination(Point source, Point destination) {
        this.destination = destination;
        this.source = source;

        updatedLocation = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LOCATION);

        if (!Utils.isServiceRunning(FloatingButtonService.class)) {
            mActivity.startService(new Intent(mActivity, FloatingButtonService.class).putExtra("activity_background", true));
        }

        if (isMapLoaded && updatedLocation != null) calculateRouteWith(source, destination, false);
    }

    @Nullable
    private CameraUpdate cameraOverheadUpdate() {

        updatedLocation = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LOCATION);

        if (updatedLocation == null) {
            return null;
        }
        CameraPosition cameraPosition = buildCameraPositionFrom(updatedLocation, DEFAULT_BEARING);
        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }

    @NonNull
    private CameraPosition buildCameraPositionFrom(Location location, double bearing) {
        return new CameraPosition.Builder()
                .zoom(DEFAULT_ZOOM)
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .bearing(bearing)
                .tilt(DEFAULT_TILT)
                .padding(0, 0, 0, PADDING_FROM_BOTTOM)
                .build();
    }

    private void calculateRouteWith(Point source, Point destination, boolean isOffRoute) {

        updatedLocation = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LOCATION);

        ArrayList<Point> cordinates = new ArrayList<>();
        cordinates.add(source);
        cordinates.add(destination);

//        Double bearing = Float.valueOf(updatedLocation.getBearing()).doubleValue();

        /*RouteOptions routeOptions = RouteOptions.builder()
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .language("en")
                .steps(true)
                .voiceInstructions(true)
                .bannerInstructions(true)
                .voiceUnits("metric")
                .accessToken(YelloDriverApp.getInstance().getString(R.string.mapbox_access_token))
                .baseUrl("")
                .user("")
                .coordinates(null)
                .build();*/

        RoutesRequestCallback routesRequestCallback = new RoutesRequestCallback() {
            @Override
            public void onRoutesReady(@NotNull List<? extends DirectionsRoute> list) {
                handleRoute(list, isOffRoute);
            }

            @Override
            public void onRoutesRequestFailure(@NotNull Throwable throwable, @NotNull RouteOptions routeOptions) {
                YLog.e("Route Error", throwable.toString());
            }

            @Override
            public void onRoutesRequestCanceled(@NotNull RouteOptions routeOptions) {
                YLog.e("Route Cancel", "Route Cancel");
            }
        };

        RouteOptions routeOptions = new RouteOptions() {
            @NonNull
            @Override
            public String baseUrl() {
                return "https://api.mapbox.com";
            }

            @NonNull
            @Override
            public String user() {
                return "mapbox";
            }

            @NonNull
            @Override
            public String profile() {
                return RouteUrl.PROFILE_DRIVING_TRAFFIC;
            }

            @NonNull
            @Override
            public List<Point> coordinates() {
                return cordinates;
            }

            @Nullable
            @Override
            public Boolean alternatives() {
                return null;
            }

            @Nullable
            @Override
            public String language() {
                return "en";
            }

            @Nullable
            @Override
            public String radiuses() {
                return null;
            }

            @Nullable
            @Override
            public String bearings() {
                return null;
            }

            @Nullable
            @Override
            public Boolean continueStraight() {
                return null;
            }

            @Nullable
            @Override
            public Boolean roundaboutExits() {
                return null;
            }

            @Nullable
            @Override
            public String geometries() {
                return null;
            }

            @Nullable
            @Override
            public String overview() {
                return null;
            }

            @Nullable
            @Override
            public Boolean steps() {
                return true;
            }

            @Nullable
            @Override
            public String annotations() {
                return null;
            }

            @Nullable
            @Override
            public String exclude() {
                return null;
            }

            @Nullable
            @Override
            public Boolean voiceInstructions() {
                return true;
            }

            @Nullable
            @Override
            public Boolean bannerInstructions() {
                return true;
            }

            @Nullable
            @Override
            public String voiceUnits() {
                return "metric";
            }

            @NonNull
            @Override
            public String accessToken() {
                return YelloDriverApp.getInstance().getString(R.string.mapbox_access_token);
            }

            @NonNull
            @Override
            public String requestUuid() {
                return "";
            }

            @Nullable
            @Override
            public String approaches() {
                return null;
            }

            @Nullable
            @Override
            public String waypointIndices() {
                return null;
            }

            @Nullable
            @Override
            public String waypointNames() {
                return null;
            }

            @Nullable
            @Override
            public String waypointTargets() {
                return null;
            }

            @Nullable
            @Override
            public WalkingOptions walkingOptions() {
                return null;
            }

            @NonNull
            @Override
            public Builder toBuilder() {
                return builder();
            }
        };
//        RouteOptions routeOptions = RouteOptions.builder()
//                .accessToken(YelloDriverApp.getInstance().getString(R.string.mapbox_access_token))
////                .geometries(RouteUrl.GEOMETRY_POLYLINE6)
////                .overview(DirectionsCriteria.OVERVIEW_FULL)
//                .profile(RouteUrl.PROFILE_DRIVING_TRAFFIC)
//                .coordinates(cordinates)
//                .baseUrl("https://api.mapbox.com")
////                .language("en")
////                .bearings(bearing.toString())
////                .bannerInstructions(true)
////                .voiceInstructions(true)
////                .steps(true)
////                .voiceUnits("metric")
//                .requestUuid("")
//                .user("")
//                .build();

        navigation.requestRoutes(routeOptions, routesRequestCallback);
//        cordinates.clear();


//        NavigationRoute.builder(YelloDriverApp.getInstance())
//                .accessToken(YelloDriverApp.getInstance().getString(R.string.mapbox_access_token))
//                .origin(source, bearing, BEARING_TOLERANCE)
//                .destination(destination)
//                .voiceUnits("metric")
//                //.routeOptions(routeOptions)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
//                        handleRoute(response, isOffRoute);
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable throwable) {
//                        YLog.e("Error", throwable.toString());
//                    }
//                });
    }

    private void initDynamicCamera(DirectionsRoute directionsRoute) {
        navigationMap.startCamera(directionsRoute);
    }

    private void handleRoute(List<? extends DirectionsRoute> routes, boolean isOffRoute) {
//        List<DirectionsRoute> routes = null;
        if (routes != null && !routes.isEmpty()) {
            YLog.e("Route Handle", "Route Handle");
            btnRideSos.setVisibility(View.VISIBLE);
            DirectionsRoute route = routes.get(FIRST);
            navigationMap.drawRoute(route);
            Log.d("ROUTE_DISTANCE", String.valueOf(route.distance()));

            if (isOffRoute) {
                if (ActivityCompat.checkSelfPermission(YelloDriverApp.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(YelloDriverApp.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                setRenderModeGPS();
                navigation.startTripSession();
                ivPlayVoiceInstructions.setVisibility(View.GONE);
            } else {

                quickStartNavigation(route);
            }

            //Todo: Format it later
            if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
                String arrivalDistNTime = Utils.getArrivalByDistanceDuration(route.distance(), route.duration());
                tvTotDistanceAndETAMarkAsCmplte.setText(arrivalDistNTime);

            } else if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_NAV_TO_RIDER, false)) {
                double totalDistance = route.distance() / 1000;
                txtStartRideTotDistance.setText(String.format("%.2f", totalDistance) + "KM");

                StoredRoute preNavRoute = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ONGOING_ROUTE, new StoredRoute());
                preNavRoute.setDistanceMeters(route.distance());
                preNavRoute.setDurationSeconds(route.duration());
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_ONGOING_ROUTE, preNavRoute);
            }
        }
    }

    private void quickStartNavigation(DirectionsRoute route) {
        // Transition to navigation state
        // Show the InstructionView
        //instructionView.setVisibility(View.VISIBLE);

        adjustMapPaddingForNavigation(100, 16);
        // Updates camera with last location before starting navigating,
        // making sure the route information is updated
        // by the time the initial camera tracking animation is fired off
        // Alternatively, NavigationMapboxMap#startCamera could be used here,
        // centering the map camera to the beginning of the provided route

        updatedLocation = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LOCATION);

        navigationMap.resumeCamera(updatedLocation);

        if (ActivityCompat.checkSelfPermission(YelloDriverApp.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(YelloDriverApp.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (isAutoDriveRequired) {
            setSimuationInfo(route);
        }

        setRenderModeGPS();

        navigation.startTripSession();
        ivPlayVoiceInstructions.setVisibility(View.GONE);
        // TODO remove example usage
        navigationMap.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS);
        CameraUpdate cameraUpdate = cameraOverheadUpdate();
        if (cameraUpdate != null) {
            NavigationCameraUpdate navUpdate = new NavigationCameraUpdate(cameraUpdate);
            navigationMap.retrieveCamera().update(navUpdate);
        }
        if (!floatingBubble) {
            startGoogleMapsNavigation();
        }


    }

    @SuppressLint("MissingPermission")
    private void setSimuationInfo(DirectionsRoute route) {
        // Start for auto simulate for testing purpose
        routeReplayLocationEngine.assign(route);

        long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
        long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .build();

        LocationEngineCallback<LocationEngineResult> callback = new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {

            }

            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        };
        routeReplayLocationEngine.requestLocationUpdates(request, callback, getMainLooper());
        routeReplayLocationEngine.getLastLocation(callback);
        routeReplayLocationEngine.run();
        // End for auto simulate for testing purpose
    }

    private void adjustMapPaddingForNavigation(double mapBottomPadding, double mapZoom) {
        //Resources resources = getResources();
        //int mapViewHeight = mapView.getHeight();a
        //int bottomSheetHeight = (int) resources.getDimension(R.dimen.component_navigation_bottomsheet_height);
        //int bottomSheetHeight = 180;
        //int topPadding = mapViewHeight - (bottomSheetHeight * BOTTOMSHEET_PADDING_MULTIPLIER);
        //navigationMap.retrieveMap().setPadding(ZERO_PADDING, ZERO_PADDING,ZERO_PADDING,topPadding);

        CameraPosition position = new CameraPosition.Builder()
                .padding(0, 0, 0, mapBottomPadding)
                .zoom(mapZoom)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), TWO_SECONDS_IN_MILLISECONDS);

        new Handler().postDelayed(() -> setRenderModeRecenter(), 3000);
    }

    @Override
    public void onRouteProgressChanged(@NotNull RouteProgress routeProgress) {

        // location not received in new method
        // Cache "snapped" Locations for re-route Directions API requests
//        updateLocation(location);

        // Update InstructionView data from RouteProgress
        instructionView.updateDistanceWith(routeProgress);

        if (null != routeProgress) {
            /*
            double driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
            double driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);
            double dLng = destination.longitude() - driverLongitude;
            double heading = Math.atan2(Math.sin(dLng) * Math.cos(destination.latitude()),
                    Math.cos(driverLatitude) * Math.sin(destination.latitude()) - Math.sin(driverLatitude) * Math.cos(destination.latitude()) * Math.cos(dLng));
            double heading_degree = (Math.toDegrees(heading) >= -180 && Math.toDegrees(heading) < 180) ?
                    Math.toDegrees(heading) : ((((Math.toDegrees(heading) + 180) % 360) + 360) % 360 + -180);
            Log.d("HEADING",String.valueOf(heading_degree));
            ValueAnimator animator = ValueAnimator.ofFloat(0, (float) (1 * heading_degree));
            animator.setDuration(
                    // Multiplying by 1000 to convert to milliseconds
                    1 * 1 * 1000);
            animator.setInterpolator(new LinearInterpolator());
            animator.setStartDelay(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // Use the animation number in a new camera position and then direct the map camera to move to the new position
                    mapboxMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(driverLatitude, driverLongitude))
                                    .bearing(heading_degree)
                                    .build()));
                }
            });
            animator.start();

             */

            //YLog.i("legDuration", "" + routeProgress.durationRemaining());

            //if (null != routeProgress.bannerInstruction()) {

            //tvNextDistance.setText(formatDistance(routeProgress.bannerInstruction().getRemainingStepDistance()));
            TextView stepText = instructionView.findViewById(R.id.stepDistanceText);

            if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
                tvNextDistanceMarkComplete.setText(stepText.getText());

                String arrivalTime = DateUtils.getRemainingDurationFromRoute((int) routeProgress.getDurationRemaining());

                tvArrivalTimeMarkComplete.setText(arrivalTime);
            } else if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_NAV_TO_RIDER, false)) {
                txtStartRideNxtDistance.setText(stepText.getText());
            }
        }
    }


    @Override
    public void onFinalDestinationArrival(@NotNull RouteProgress routeProgress) {
        YLog.e("Arrival", "You Have arrived");
    }

    @Override
    public void onNextRouteLegStart(@NotNull RouteLegProgress routeLegProgress) {

    }

    @Override
    public void onNewVoiceInstructions(@NotNull VoiceInstructions voiceInstructions) {
        speechPlayer.play(voiceInstructions);
    }

    @Override
    public void onNewBannerInstructions(@NotNull BannerInstructions bannerInstructions) {

        // Update InstructionView banner instructions
        instructionView.updateBannerInstructionsWith(bannerInstructions);

        BannerText primary = bannerInstructions.primary();
        String imageUrl = "direction_" + primary.type() + "_" + primary.modifier();
        imageUrl = imageUrl.replace(' ', '_');
        String directionPrimaryText = primary.text();

//        BannerText secondary = bannerInstructions.secondary();

//        if (secondary != null) {
//            if (secondary.type() != null) {
//                if (secondary.modifier() != null) {
//                    String imageUrlSec = "direction_" + secondary.type() + "_" + secondary.modifier();
//                    imageUrlSec = imageUrlSec.replace(' ', '_');
//                    String directionSecText = secondary.text();
//                    if (directionSecText != null) {
//
//                        llMarkAsCompleteSec.setVisibility(View.VISIBLE);
//                        llStartRideSec.setVisibility(View.VISIBLE);
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                llMarkAsCompleteSec.setVisibility(View.GONE);
//                                llStartRideSec.setVisibility(View.GONE);
//                            }
//                        }, 5000);
//
//                        txtMarkascompleteSec.setText(directionSecText);
//                        txtDirectionSecStartRide.setText(directionSecText);
//                        try {
//                            Drawable drawable = mActivity.getResources().getDrawable(getResources().getIdentifier(imageUrlSec, "drawable", YelloDriverApp.getInstance().getPackageName()));
////                Glide.with(this).load(drawable).into(imgDirectionIconMarkComplete);
//                            imgMarkascompleteArrowSec.setImageDrawable(drawable);
//                            imgArrowSecStartRide.setImageDrawable(drawable);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }

        if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false)) {
            try {
                Drawable drawable = mActivity.getResources().getDrawable(getResources().getIdentifier(imageUrl, "drawable", YelloDriverApp.getInstance().getPackageName()));
//                Glide.with(this).load(drawable).into(imgDirectionIconMarkComplete);
                imgDirectionIconMarkComplete.setImageDrawable(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!directionPrimaryText.isEmpty()) {
                tvPrimeInstructions.setText(directionPrimaryText);
            }
            /*if (null != secondary) {
                String directionSecondaryText = secondary.text();
                tvSecInstructions.setVisibility(View.VISIBLE);
                tvSecInstructions.setText(directionSecondaryText + "");
            } else {
//                tvSecInstructions.setVisibility(View.GONE);
            }*/
        } else if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_NAV_TO_RIDER, false)) {
            try {
                Drawable drawable = mActivity.getResources().getDrawable(getResources().getIdentifier(imageUrl, "drawable", YelloDriverApp.getInstance().getPackageName()));
//                Glide.with(this).load(drawable).into(imgStartRideDircetionIco);
                imgStartRideDircetionIco.setImageDrawable(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!directionPrimaryText.isEmpty()) {
                txtStartRideInstructions.setText(directionPrimaryText + "");
            }
                /*if (null != secondary) {
                    String directionSecondaryText = secondary.text();
                    tvSecInstructions.setVisibility(View.VISIBLE);
                    tvSecInstructions.setText(directionSecondaryText);
                } else {
//                tvSecInstructions.setVisibility(View.GONE);
                }*/
        }
    }

    @Override
    public void onOffRouteStateChanged(boolean b) {
        if (b) {
            YLog.e("OFFROUTE", "detected");
            driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
            driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

            Point offRouteSource = Point.fromLngLat(driverLongitude, driverLatitude);
            calculateRouteWith(offRouteSource, destination, true);
        }
    }

    private void SOSDialog() {
        KAlertDialog askAlertDlg = new KAlertDialog(mActivity)
                .setTitleText(YelloDriverApp.getInstance().getString(R.string.lbl_sos_title))
                .setContentText(YelloDriverApp.getInstance().getString(R.string.lbl_sos_desc))
                .setConfirmText(YelloDriverApp.getInstance().getString(R.string.submit))
                .setCancelText(YelloDriverApp.getInstance().getString(R.string.cancel));
        askAlertDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                askAlertDlg.dismiss();

                boolean isRideOngoing = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);

                Input<Object> driverUserId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, ""), true);
//                Input<Object> inputUpdatedLocation = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LOCATION), true);

                driverLatitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LATITUDE, 0d);
                driverLongitude = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.INTENT_KEY_LONGITUDE, 0d);

                String strLocation = "(" + driverLongitude + "," + driverLatitude + ")";
                Input<Object> stringLocation = new Input<>(strLocation, true);
                Input<String> note = new Input<>(" TEST NOTES ", true);

                if (isRideOngoing) {

                    Input<Object> userId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_USER_ID, ""), true);
                    Input<Object> rideId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, ""), true);

                    InsertSosRequestRideMutation insertSupportRequestMutation = new InsertSosRequestRideMutation(driverUserId, stringLocation, note, rideId, userId);
                    homeViewmodel.insertSOS(insertSupportRequestMutation);
                } else {
                    InsertSosRequestMutation insertSupportRequestMutation = new InsertSosRequestMutation(driverUserId, stringLocation, note);
                    homeViewmodel.insertSOS(insertSupportRequestMutation);
                }
            }
        });
        askAlertDlg.show();
    }

    private void submitRideImageToAWS(ModelUploadRide.Data.UploadRideRouteMap uploadRideRouteMap, Bitmap bitmap) {
        //Defining retrofit api service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.dummy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//        File fileDuplicate = new File(file.getPath());    // create new file on device
//init array with file length
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray = new byte[0];

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestFile = RequestBody.create(byteArray);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        ApiService service = retrofit.create(ApiService.class);
        Call<Void> call = service.uploadProfileData(uploadRideRouteMap.getUploadUrl(), requestFile);
        //calling the api
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                YLog.e("Response", response.toString());

//                String onGoingRideId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, "");
//                markRideAsCompleteToServer(onGoingRideId, uploadRideRouteMap.getFileUploadId());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (customProgressDialog != null) {
                    customProgressDialog.dismissDialog();
                }
                YLog.e("Response", t.toString());
            }
        });
    }


    private void checkForLocationPermissionAndConnect() {

        if (BluetoothAdapter.getDefaultAdapter() != null &&
                !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            BluetoothAdapter.getDefaultAdapter().enable();
        }

        boolean granted = PermissionUtils.isGranted(mActivity, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.ACCESS_COARSE_LOCATION);
        if (granted) {
            final LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
//        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGpsEnabled) {
                setUpStripe(secretToken);
            }
        }
    }

    private void setUpStripe(String secretToken) {

        YLog.e("PAYMENT", "secret token " + secretToken);
        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();
        final DiscoveryConfiguration config = new DiscoveryConfiguration(5, DeviceType.CHIPPER_2X, isPaymentSimulate);
        if (config == null) {
            YLog.e("PAYMENT", "DiscoveryConfig is null");
        }

        try {
            if (Terminal.isInitialized()) {
                Log.d("TERMINAL", "initialized");

                Terminal.getInstance().clearCachedCredentials();
                Terminal.cleanupTerminal$terminalsdk_release();
                Terminal.initTerminal(mActivity, LogLevel.VERBOSE, new TokenProvider(secretToken),
                        new TerminalEventListener());

                //final DiscoveryConfiguration config = new DiscoveryConfiguration(0, DeviceType.CHIPPER_2X, isPaymentSimulate);
                if (discoveryTask == null && Terminal.getInstance().getConnectedReader() == null) {
                    Terminal.getInstance().discoverReaders(config, this, discoveryCallback);
                }
                //Terminal.getInstance().discoverReaders(config, this, discoveryCallback);
                //onSelectPaymentWorkflow();
                //onConnectReader();
                //createTokenMutation();


            } else {
                Terminal.initTerminal(mActivity, LogLevel.VERBOSE, new TokenProvider(secretToken),
                        new TerminalEventListener());

                //final DiscoveryConfiguration config = new DiscoveryConfiguration(0, DeviceType.CHIPPER_2X, isPaymentSimulate);
                if (discoveryTask == null && Terminal.getInstance().getConnectedReader() == null) {
                    YLog.e("PAYMENT", "Reader does not exist");
                    Terminal.getInstance().discoverReaders(config, this, discoveryCallback);
                }
            }


        } catch (TerminalException e) {
            throw new RuntimeException("Location services are required in order to initialize " +
                    "the Terminal.", e);
        }
    }

    private void createTokenMutation() {
        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        CreateStripeTokenMutation createStripeTokenMutation = new CreateStripeTokenMutation();
        homeViewmodel.createStripeToken(createStripeTokenMutation);
    }

    Observer<ModelBoardingPassAmount.Data.YtBoardingPassByPk> observerExtendPassAmount = ytBoardingPassByPk -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            this.ytBoardingPassByPk = ytBoardingPassByPk;

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (ytBoardingPassByPk != null) {
                renewPassDialog();
            }
        }
    };

    Observer<ModelStripeToken.Data.CreateDeviceToken> observerStripeToken = createDeviceToken -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (createDeviceToken != null) {
                secretToken = createDeviceToken.getSecret();
//            setUpStripe(secretToken);
                checkForLocationPermissionAndConnect();
            }
        }
    };

    Observer<ModelExtendBoardingPass.Data.ExtendBoardingPass> observerExtendsPass = extendBoardingPass -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (extendBoardingPass != null) {

                orderId = extendBoardingPass.getOrderId();
                Log.d("ORDER_ID", orderId);
                CreatePaymentMutation createPaymentMutation = new CreatePaymentMutation(extendBoardingPass.getOrderId(), true, Constants.PAYMENT_CURRENCY);
                homeViewmodel.createPayment(createPaymentMutation);
            }
        }
    };

    Observer<ModelCapturePayment.Data.CapturePayment> observerCapturePayment = capturePayments -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (capturePayments != null) {
                if (customProgressDialog != null) {
                    customProgressDialog.dismissDialog();
                }

                final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.NORMAL_TYPE)
                        .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                        .setContentText(YelloDriverApp.getInstance().getString(R.string.boarding_pass_extended_success))
                        .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                pDialog.show();
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
            }
        }
    };

    Observer<ModelCreatePayment.Data.CreatePayment> observerPayment = createPayment -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (createPayment != null) {
                final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.NORMAL_TYPE)
                        .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                        .setContentText(YelloDriverApp.getInstance().getString(R.string.please_use_card))
                        .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                pDialog.show();
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);

                pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        pDialog.dismissWithAnimation();

                        customProgressDialog = new CustomProgressDialog(mActivity);
                        customProgressDialog.showDialog();

                        paymentId = createPayment.getPaymentId();
                        Log.e("PAYMENT_ID", paymentId);
                        Terminal.getInstance().retrievePaymentIntent(createPayment.getClientSecret(), createPaymentIntentCallback);
                    }
                });
            }
        }
    };


    final com.stripe.stripeterminal.callable.Callback discoveryCallback = new com.stripe.stripeterminal.callable.Callback() {
        @Override
        public void onSuccess() {
            YLog.e("PAYMENT", "discoveryCallback success");
            discoveryTask = null;
//            viewModel.discoveryTask = null;
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            discoveryTask = null;
            YLog.e("PAYMENT", "discoveryCallback failure");
            onCancelDiscovery();
        }


    };

    /**
     * Callback function called when discovery has been canceled by the [DiscoveryFragment]
     */
    @Override
    public void onCancelDiscovery() {
//        navigateTo(TerminalFragment.TAG, new TerminalFragment());
        YLog.e("PAYMENT", "onCancelDiscovery");
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
            Utils.showCommonErrorDialog(getResources().getString(R.string.stripe_unavailable));
        }
        //createTokenMutation();
    }

    /**
     * Callback function called once discovery has been selected by the [TerminalFragment]
     */
    @Override
    public void onRequestDiscovery(boolean isSimulated) {
        YLog.e("PAYMENT", "onREquestDiscovery " + isSimulated);
//        navigateTo(DiscoveryFragment.TAG, DiscoveryFragment.newInstance(isSimulated));
        createTokenMutation();
    }

    /**
     * Callback function called to exit the payment workflow
     */
    @Override
    public void onRequestExitWorkflow() {
        YLog.e("PAYMENT", "onRequestExitWorkflow");
        if (Terminal.getInstance().getConnectionStatus() == ConnectionStatus.CONNECTED) {
//            navigateTo(ConnectedReaderFragment.TAG, new ConnectedReaderFragment());
        } else {
//            navigateTo(TerminalFragment.TAG, new TerminalFragment());
        }
    }

    /**
     * Callback function called to start a payment by the [PaymentFragment]
     */
    @Override
    public void onRequestPayment(int amount, @NotNull String currency) {
        YLog.e("PAYMENT", "onRequestPayment" + amount + " , " + currency);
//        navigateTo(EventFragment.TAG, EventFragment.requestPayment(amount, currency));
    }

    /**
     * Callback function called once the payment workflow has been selected by the
     * [ConnectedReaderFragment]
     */
    @Override
    public void onSelectPaymentWorkflow() {
        YLog.e("PAYMENT", "onSelectPaymentWorkflow");
//        navigateTo(PaymentFragment.TAG, new PaymentFragment());
    }

    /**
     * Callback function called once the read card workflow has been selected by the
     * [ConnectedReaderFragment]
     */
    @Override
    public void onSelectReadReusableCardWorkflow() {
        YLog.e("PAYMENT", "onSelectReadReusableCardWorkflow");
//        navigateTo(EventFragment.TAG, EventFragment.readReusableCard());
    }

    /**
     * Callback function called once the update reader workflow has been selected by the
     * [ConnectedReaderFragment]
     */
    @Override
    public void onSelectUpdateWorkflow() {
        YLog.e("PAYMENT", "onSelectUpdateWorkflow");
//        navigateTo(UpdateReaderFragment.TAG, new UpdateReaderFragment());
    }

    // Terminal event callbacks

    /**
     * Callback function called when collect payment method has been canceled
     */
    @Override
    public void onCancelCollectPaymentMethod() {
        YLog.e("PAYMENT", "onCancelCollectPaymentMethod");

//        navigateTo(ConnectedReaderFragment.TAG, new ConnectedReaderFragment());
    }

    /**
     * Callback function called on completion of [Terminal.connectReader]
     */
    @Override
    public void onConnectReader() {
        YLog.e("PAYMENT", "onConnectReader");

//        navigateTo(ConnectedReaderFragment.TAG, new ConnectedReaderFragment());
        // Purchase Boarding pass here and initiate payment here once connected to device successfully.
        String boardingPassId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_BOARDING_PASS_ID, "");
        ExtendBoardingPassMutation extendBoardingPassMutation = new ExtendBoardingPassMutation(extendByDays, boardingPassId);
        homeViewmodel.extendsBoardingPass(extendBoardingPassMutation);
    }

    @Override
    public void onDisconnectReader() {
        YLog.e("PAYMENT", "onDisconnectReader");

//        navigateTo(TerminalFragment.TAG, new TerminalFragment());
    }

    @Override
    public void onUpdateDiscoveredReaders(@NotNull List<? extends Reader> list) {

        YLog.e("PAYMENT", "onUpdateDiscoveredReaders");
        readers = list;
        serialNumber = readers.get(0).getSerialNumber();
        this.reader = readers.get(0);

        Terminal.getInstance().connectReader(readers.get(0), new ReaderCallback() {
            @Override
            public void onSuccess(@NotNull Reader reader) {
                onConnectReader();
            }

            @Override
            public void onFailure(@NotNull TerminalException e) {
                onCancelDiscovery();
            }
        });


    }

    @NotNull
    private final PaymentIntentCallback processPaymentCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent paymentIntent) {
            YLog.e("PAYMENT", "onSuccess processPaymentCallback");

            completeFlow(true, "");
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            HomeFragment.this.onFailure(e);
        }
    };

    @NotNull
    private final PaymentIntentCallback cancelPaymentIntentCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent paymentIntent) {
            onCancelCollectPaymentMethod();
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            HomeFragment.this.onFailure(e);
        }
    };

    @NotNull
    private final PaymentIntentCallback collectPaymentMethodCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent paymentIntent) {
            Terminal.getInstance().processPayment(paymentIntent, processPaymentCallback);
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            HomeFragment.this.onFailure(e);
        }
    };

    @NotNull
    private final PaymentIntentCallback createPaymentIntentCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent intent) {
            YLog.e("PAYMENT", "onSuccess createPaymentIntentCallback");

            paymentIntent = intent;
            Terminal.getInstance().collectPaymentMethod(paymentIntent, HomeFragment.this, collectPaymentMethodCallback);
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            HomeFragment.this.onFailure(e);
        }
    };

//    @NotNull
//    private final PaymentMethodCallback reusablePaymentMethodCallback = new PaymentMethodCallback() {
//        @Override
//        public void onSuccess(@NotNull PaymentMethod paymentMethod) {
////            addEvent("Created PaymentMethod: ${paymentMethod.id}", "terminal.readReusableCard");
//            completeFlow();
//        }
//
//        @Override
//        public void onFailure(@NotNull TerminalException e) {
//            UserOnBoardingStepThreeFragment.this.onFailure(e);
//        }
//    };

    @Override
    public void onRequestReaderInput(@NotNull ReaderInputOptions options) {
        YLog.e("PAYMENT", "onRequestReaderInput " + options.toString());
    }

    @Override
    public void onRequestReaderDisplayMessage(@NotNull ReaderDisplayMessage message) {
        YLog.e("PAYMENT", "onRequestReaderDisplayMessage " + message.toString());
    }

    private void completeFlow(boolean isSuccess, String message) {
        YLog.e("PAYMENT", "completeFlow ");

        if (isSuccess) {

            CapturePaymentMutation capturePaymentMutation = new CapturePaymentMutation(paymentId);
            homeViewmodel.capturePayment(capturePaymentMutation);

            if (BluetoothAdapter.getDefaultAdapter() != null &&
                    !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                BluetoothAdapter.getDefaultAdapter().disable();
            }

            if (Terminal.isInitialized()) {
                if (discoveryCallback != null) {
                    Terminal.getInstance().disconnectReader(discoveryCallback);
                    //Terminal.cleanupTerminal$terminalsdk_release();
                    //Terminal.getInstance().clearCachedCredentials();
                }
            }

        } else {

            if (BluetoothAdapter.getDefaultAdapter() != null &&
                    !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                BluetoothAdapter.getDefaultAdapter().disable();
            }

            if (Terminal.isInitialized()) {
                if (discoveryCallback != null) {
                    Terminal.getInstance().disconnectReader(discoveryCallback);
                    Terminal.cleanupTerminal$terminalsdk_release();
                    Terminal.getInstance().clearCachedCredentials();
                }
            }

            // Some error message
            final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                    .setContentText(message)
                    .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
            pDialog.show();
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);
        }
    }

    private void onFailure(@NotNull TerminalException e) {
        YLog.e("PAYMENT", "completeFlow " + e.getErrorMessage());
        completeFlow(false, e.toString());
    }


    public void generateLocaleNotification(String contentText) {

        Random random = new Random();
        int randomNo = random.nextInt();

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, randomNo, intent, 0);

        String channelId = YelloDriverApp.getInstance().getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mActivity, channelId)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(YelloDriverApp.getInstance().getString(R.string.app_name))
                        .setContentText(contentText)
                        .setColor(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    YelloDriverApp.getInstance().getString(R.string.notification_channel_offline),
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (preventClosing) {
//            Toast.makeText(mActivity, "Bottom sheet is open", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            //getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
    }


    @Override
    public void onRerouteStateChanged(@NotNull RerouteState rerouteState) {
        YLog.e("REROUTE", "Changed");

    }
}