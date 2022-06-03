//package com.app.yellodriver.activity;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.app.yellodriver.R;
//import com.app.yellodriver.YelloDriverApp;
//import com.app.yellodriver.util.YLog;
//import com.bumptech.glide.Glide;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.mapbox.android.core.location.LocationEngine;
//import com.mapbox.android.core.location.LocationEngineCallback;
//import com.mapbox.android.core.location.LocationEngineProvider;
//import com.mapbox.android.core.location.LocationEngineRequest;
//import com.mapbox.android.core.location.LocationEngineResult;
//import com.mapbox.android.core.permissions.PermissionsListener;
//import com.mapbox.android.core.permissions.PermissionsManager;
//import com.mapbox.api.directions.v5.DirectionsCriteria;
//import com.mapbox.api.directions.v5.WalkingOptions;
//import com.mapbox.api.directions.v5.models.BannerInstructions;
//import com.mapbox.api.directions.v5.models.BannerText;
//import com.mapbox.api.directions.v5.models.DirectionsResponse;
//import com.mapbox.api.directions.v5.models.DirectionsRoute;
//import com.mapbox.api.directions.v5.models.RouteOptions;
//import com.mapbox.geojson.Point;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.camera.CameraPosition;
//import com.mapbox.mapboxsdk.camera.CameraUpdate;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.geometry.LatLngBounds;
//import com.mapbox.mapboxsdk.location.LocationComponent;
//import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
//import com.mapbox.mapboxsdk.location.LocationComponentOptions;
//import com.mapbox.mapboxsdk.location.modes.CameraMode;
//import com.mapbox.mapboxsdk.location.modes.RenderMode;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.navigation.base.trip.model.RouteProgress;
//import com.mapbox.navigation.core.MapboxNavigation;
//import com.mapbox.navigation.core.trip.session.OffRouteObserver;
//import com.mapbox.navigation.ui.camera.DynamicCamera;
//import com.mapbox.navigation.ui.camera.NavigationCamera;
//import com.mapbox.navigation.ui.camera.NavigationCameraUpdate;
//import com.mapbox.navigation.ui.instruction.InstructionView;
//import com.mapbox.navigation.ui.map.NavigationMapboxMap;
//import com.mapbox.navigation.ui.voice.NavigationSpeechPlayer;
//import com.mapbox.navigation.ui.voice.SpeechPlayerProvider;
//import com.mapbox.navigation.ui.voice.VoiceInstructionLoader;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.text.NumberFormat;
//import java.util.List;
//import java.util.Locale;
//
//import okhttp3.Cache;
//
//// import android.support.annotation.NonNull;
//// import android.support.annotation.Nullable;
//
//public class MapboxNavigationView extends AppCompatActivity implements OnMapReadyCallback,
//        MapboxMap.OnMapLongClickListener, OffRouteObserver,PermissionsListener {
//
//
//    private static final int FIRST = 0;
//    private static final int ONE_HUNDRED_MILLISECONDS = 100;
//    private static final int BOTTOMSHEET_PADDING_MULTIPLIER = 4;
//    private static final int TWO_SECONDS_IN_MILLISECONDS = 2000;
//    private static final double BEARING_TOLERANCE = 90d;
//    private static final String LONG_PRESS_MAP_MESSAGE = "Long press the map to select a destination.";
//    private static final String SEARCHING_FOR_GPS_MESSAGE = "Searching for GPS...";
//    private static final String COMPONENT_NAVIGATION_INSTRUCTION_CACHE = "component-navigation-instruction-cache";
//    private static final long TEN_MEGABYTE_CACHE_SIZE = 10 * 1024 * 1024;
//    private static final int ZERO_PADDING = 0;
//    private static final double DEFAULT_ZOOM = 12.0;
//    private static final double DEFAULT_TILT = 0d;
//    private static final double DEFAULT_BEARING = 0d;
//    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
//    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500;
//
//
//    private Context context;
//    private final MapboxNavigationViewLocationCallback callback = new MapboxNavigationViewLocationCallback(this);
//    private LocationEngine locationEngine;
//    private MapboxNavigation navigation;
//    private NavigationSpeechPlayer speechPlayer;
//    private NavigationMapboxMap navigationMap;
//    private Location lastLocation;
//    private DirectionsRoute route;
//    private Point destination = null;
//
//    private InstructionView instructionView;
//    private MapView mapView;
//
//    private boolean mapLoaded = false;
//    //private Button btnRecenter;
//    FloatingActionButton fabRecentre;
//    private ImageView imgDirectionIcon;
//    private TextView tvPrimeInstructions;
//    private TextView tvSecInstructions;
//    private TextView tvNextDistance;
//    //private final Map<String, String> unitStrings = new HashMap<>();
//    private NumberFormat numberFormat;
//    private MapboxMap mapboxMap;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(R.style.MapboxStyleNavigationViewLight);
//        super.onCreate(savedInstanceState);
//        context = this;
//        Mapbox.getInstance(this, "pk.eyJ1IjoiZGFyamlwcmFuYXYiLCJhIjoiY2tiaXZic3VrMGZmbzJycXBjMXlucjNtYyJ9.5iCyIX4olerRgWpoSrX_Xw");
//        setContentView(R.layout.custom_navigation);
//
//
//        Locale locale = new Locale("en");
//        numberFormat = NumberFormat.getNumberInstance(locale);
//
//        //unitStrings.put(UNIT_KILOMETERS, context.getString(com.mapbox.services.android.navigation.R.string.kilometers));
//        //unitStrings.put(UNIT_METERS, context.getString(com.mapbox.services.android.navigation.R.string.meters));
//        //unitStrings.put(UNIT_MILES, context.getString(com.mapbox.services.android.navigation.R.string.miles));
//        //unitStrings.put(UNIT_FEET, context.getString(com.mapbox.services.android.navigation.R.string.feet));
//
//
//        mapView = findViewById(R.id.mapView);
//        instructionView = findViewById(R.id.instructionView);
//        mapView.onCreate(null);
//        mapView.getMapAsync(this);
//
//        //imgDirectionIcon = findViewById(R.id.imgdirectionIcon);
//        //tvPrimeInstructions = findViewById(R.id.tvprimaryinstructions);
//        //tvNextDistance = findViewById(R.id.tvnextdistance);
//        imgDirectionIcon = findViewById(R.id.ivnextdirection);//for bottom sheet
//        tvPrimeInstructions = findViewById(R.id.tvprimeinstructions);//for bottom sheet
//        tvSecInstructions = findViewById(R.id.tvsecondaryinstructions);
//        tvNextDistance = findViewById(R.id.tvremainingstepdistance);//for bottom sheet
//
//        //btnRecenter = findViewById(R.id.btnrecenter);
//        fabRecentre = findViewById(R.id.fabRecentre);
//        fabRecentre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navCameraToMyLocation();
//            }
//        });
//    }
//
//    private void navCameraToMyLocation() {
//        // Start  animate camera and redirect
//        LocationComponent locationComponent = mapboxMap.getLocationComponent();
//        locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
//        // Set the component's render mode
//        locationComponent.setRenderMode(RenderMode.GPS);
//        // END  animate camera and redirect
//    }
//
//    @Override
//    public void onMapReady(@NonNull MapboxMap mapboxMap) {
//        this.mapboxMap = mapboxMap;
//        mapboxMap.setStyle(new Style.Builder().fromUri(context.getString(R.string.map_style)), style -> {
//            navigationMap = new NavigationMapboxMap(mapView, mapboxMap);
//
//            // For Location updates
//            initializeLocationEngine(style);
//
//            // For navigation logic / processing
//            initializeNavigation(mapboxMap);
//            navigationMap.updateCameraTrackingMode(NavigationCamera.NAVIGATION_TRACKING_MODE_NONE);
//            navigationMap.updateLocationLayerRenderMode(RenderMode.GPS);
//
//            // For voice instructions
//            initializeSpeechPlayer();
//            mapLoaded = true;
//            //destination = Point.fromLngLat(72.545257, 23.037244);
//            destination = Point.fromLngLat(70.7877, 22.2915);//kotecha
//            destination = Point.fromLngLat(70.7881, 22.2996);//balbhavan
//            if (destination != null) {
//                navigateToDestination(destination);
//            }
//        });
//    }
//
//    @Override
//    public boolean onMapLongClick(@NonNull LatLng point) {
//        // Only reverse geocode while we are not in navigation
//        return false;
//    }
//    /*
//     * Navigation listeners
//     */
//
//    /*@Override
//    protected  void onDetachedFromWindow() {
//        navigation.onDestroy();
//        super.onDetachedFromWindow();
//    }*/
//
//    @Override
//    protected void onDestroy() {
//        navigation.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onProgressChange(Location location, RouteProgress routeProgress) {
//        // Cache "snapped" Locations for re-route Directions API requests
//        updateLocation(location);
//
//        // Update InstructionView data from RouteProgress
//        instructionView.updateDistanceWith(routeProgress);
//
//
//        if (null != routeProgress) {
//
//            Log.i("legDuration",""+routeProgress.durationRemaining());
//
//            //if (null != routeProgress.bannerInstruction()) {
//
//                //tvNextDistance.setText(formatDistance(routeProgress.bannerInstruction().getRemainingStepDistance()));
//                TextView stepText = instructionView.findViewById(R.id.stepDistanceText);
//                tvNextDistance.setText(stepText.getText());
//                //Log.i("legProgress",""+formatDistance(routeProgress.currentLeg().steps().get(0).distance()));
//            //}
//        }
//
//    }
//
//    /*String smallUnit = "meters";
//    String largeUnit = "kilometers";
//    private static final int LARGE_UNIT_THRESHOLD = 10;
//    private static final int SMALL_UNIT_THRESHOLD = 401;
//
//
//    public SpannableString formatDistance(double distance) {
//        double distanceSmallUnit = TurfConversion.convertLength(distance, UNIT_METERS, smallUnit);
//        double distanceLargeUnit = TurfConversion.convertLength(distance, UNIT_METERS, largeUnit);
//
//        // If the distance is greater than 10 miles/kilometers, then round to nearest mile/kilometer
//        if (distanceLargeUnit > LARGE_UNIT_THRESHOLD) {
//            return getDistanceString(roundToDecimalPlace(distanceLargeUnit, 0), largeUnit);
//            // If the distance is less than 401 feet/meters, round by fifty feet/meters
//        } else if (distanceSmallUnit < SMALL_UNIT_THRESHOLD) {
//            return getDistanceString(roundToClosestIncrement(distanceSmallUnit), smallUnit);
//            // If the distance is between 401 feet/meters and 10 miles/kilometers, then round to one decimal place
//        } else {
//            return getDistanceString(roundToDecimalPlace(distanceLargeUnit, 1), largeUnit);
//        }
//    }
//
//
//    *//**
//     * Returns number rounded to closest specified rounding increment, unless the number is less than
//     * the rounding increment, then the rounding increment is returned
//     *
//     * @param distance to round to closest specified rounding increment
//     * @return number rounded to closest rounding increment, or rounding increment if distance is less
//     *//*
//    int roundingIncrement = 1;
//
//    private String roundToClosestIncrement(double distance) {
//        int roundedNumber = ((int) Math.round(distance)) / roundingIncrement * roundingIncrement;
//
//        return String.valueOf(roundedNumber < roundingIncrement ? roundingIncrement : roundedNumber);
//    }
//
//    *//**
//     * Rounds given number to the given decimal place
//     *
//     * @param distance     to round
//     * @param decimalPlace number of decimal places to round
//     * @return distance rounded to given decimal places
//     *//*
//    private String roundToDecimalPlace(double distance, int decimalPlace) {
//        numberFormat.setMaximumFractionDigits(decimalPlace);
//
//        return numberFormat.format(distance);
//    }
//
//    *//**
//     * Takes in a distance and units and returns a formatted SpannableString where the number is bold
//     * and the unit is shrunked to .65 times the size
//     *
//     * @param distance formatted with appropriate decimal places
//     * @param unit     string from TurfConstants. This will be converted to the abbreviated form.
//     * @return String with bolded distance and shrunken units
//     *//*
//    private SpannableString getDistanceString(String distance, String unit) {
//        SpannableString spannableString = new SpannableString(String.format("%s %s", distance, unitStrings.get(unit)));
//
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, distance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new RelativeSizeSpan(0.65f), distance.length() + 1,
//                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        return spannableString;
//    }*/
//
//    @Override
//    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
//        playAnnouncement(milestone);
//
//        // Update InstructionView banner instructions
//        instructionView.updateBannerInstructionsWith(milestone);
//
//        if (milestone instanceof BannerInstructionMilestone) {
//            BannerInstructionMilestone bannerInstructionMilestone = (BannerInstructionMilestone) milestone;
//            BannerInstructions instructions = bannerInstructionMilestone.getBannerInstructions();
//            BannerText primary = instructions.primary();
//
//            Log.e("*** Prime Modifier", primary.modifier());
//            Log.e("*** Prime Type", primary.type());
//
//            String imageUrl = "direction_" + primary.type() + "_" + primary.modifier();
//
//            Log.e("*** ImageUrl With Space", imageUrl);
//
//            imageUrl = imageUrl.replace(' ', '_');
//
//            Log.e("*** ImageUrl", imageUrl);
//
//            Drawable drawable = getResources().getDrawable(getResources().getIdentifier(imageUrl, "drawable", getPackageName()));
//
//            Glide.with(MapboxNavigationView.this).load(drawable).into(imgDirectionIcon);
//
//            String directionPrimaryText = primary.text();
//
//            if (!directionPrimaryText.isEmpty()) {
//                tvPrimeInstructions.setText(directionPrimaryText);
//            }
//
//
//            BannerText secondary = instructions.secondary();
//            if (null != secondary) {
//                String directionSecondaryText = secondary.text();
//                tvSecInstructions.setVisibility(View.VISIBLE);
//                tvSecInstructions.setText(directionSecondaryText);
//            } else {
//                tvSecInstructions.setVisibility(View.GONE);
//            }
//
//            /*if (!primary.components().isEmpty()) {
//                for (BannerComponents bannercomponent : primary.components()) {
//                    Log.i("Bannercomponent check", "inforeachloop");
//                    Log.i("Bannercomponent check", bannercomponent.imageBaseUrl()+"end");
//                    if (null != bannercomponent.imageBaseUrl() && !bannercomponent.imageBaseUrl().isEmpty()) {
//                        Log.i("Bannercomponent check", "not empty");
//                        String image = bannercomponent.imageBaseUrl();
//
//                        Glide.with(MapboxNavigationView.this).load(image).into(imgDirectionIcon);
//                    }
//
//                }
//            }*/
//        }
//    }
//
//    @Override
//    public void onOffRouteStateChanged(boolean b) {
//        if(b){
//            calculateRouteWith(destination, true);
//        }
//    }
//
//    /*
//     * Activity lifecycle methods
//     */
//    void checkFirstUpdate(Location location) {
//        if (lastLocation == null) {
//            lastLocation = location;
//            moveCameraTo(location);
//            if (destination != null) navigateToDestination(destination);
//            // Allow navigationMap clicks now that we have the current Location
//        }
//    }
//
//    void updateLocation(Location location) {
//        lastLocation = location;
//        navigationMap.updateLocation(location);
//    }
//
//    private void initializeSpeechPlayer() {
//        String language = "en";
//        String devicelng = Locale.getDefault().getDisplayLanguage();
//        if (devicelng.equals("Deutsch")) {
//            language = "de";
//        }
//        Cache cache = new Cache(new File(context.getCacheDir(), COMPONENT_NAVIGATION_INSTRUCTION_CACHE),
//                TEN_MEGABYTE_CACHE_SIZE);
//        VoiceInstructionLoader voiceInstructionLoader = new VoiceInstructionLoader(context,
//                YelloDriverApp.getInstance().getString(R.string.mapbox_access_token), cache);
//        SpeechPlayerProvider speechPlayerProvider = new SpeechPlayerProvider(context, language, true,
//                voiceInstructionLoader);
//        speechPlayer = new NavigationSpeechPlayer(speechPlayerProvider);
//    }
//
//    private PermissionsManager permissionsManager;
//
//    @SuppressLint("MissingPermission")
//    private void initializeLocationEngine(Style loadedMapStyle) {
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            locationEngine = LocationEngineProvider.getBestLocationEngine(context);
//            LocationEngineRequest request = buildEngineRequest();
//            locationEngine.requestLocationUpdates(request, callback, null);
//
//            /*----customisation for icons----*/
//            // Create and customize the LocationComponent's options
//            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
//                    .elevation(3)
//                    .accuracyAlpha(.6f)
//                    .accuracyColor(Color.TRANSPARENT)
//                    .foregroundDrawable(R.drawable.img_car)
//                    .gpsDrawable(R.drawable.img_car)
//                    .bearingDrawable(R.drawable.img_car)
//                    .build();
//
//            // Get an instance of the component
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//            LocationComponentActivationOptions locationComponentActivationOptions =
//                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
//                            .locationComponentOptions(customLocationComponentOptions)
//                            .build();
//
//            // Activate with options
//            locationComponent.activateLocationComponent(locationComponentActivationOptions);
//            /*-----customisation ends-------*/
//
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    /*If we want user location one time only*/
//    @SuppressWarnings({"MissingPermission"})
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
//        // Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//
//            // Create and customize the LocationComponent's options
//            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
//                    .elevation(5)
//                    .accuracyAlpha(.6f)
//                    .accuracyColor(Color.TRANSPARENT)
//                    .foregroundDrawable(R.drawable.img_car)
//                    .gpsDrawable(R.drawable.img_car)
//                    .bearingDrawable(R.drawable.img_car)
//                    .build();
//
//            // Get an instance of the component
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//            LocationComponentActivationOptions locationComponentActivationOptions =
//                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
//                            .locationComponentOptions(customLocationComponentOptions)
//                            .build();
//
//            // Activate with options
//            locationComponent.activateLocationComponent(locationComponentActivationOptions);
//
//            // Activate with options
////            locationComponent.activateLocationComponent(
////                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
//
//            // Enable to make component visible
//            locationComponent.setLocationComponentEnabled(true);
//
//            // Set the component's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
//
//            // Set the component's render mode
//            locationComponent.setRenderMode(RenderMode.GPS);
//            locationComponent.zoomWhileTracking(30);
//
//            // Start  animate camera and redirect
////            CameraPosition position = new CameraPosition.Builder()
////                    .target(new LatLng(locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLongitude())) // Sets the new camera position
////                    .zoom(17) // Sets the zoom
////                    .bearing(90) // Rotate the camera
////                    .tilt(30) // Set the camera tilt
////                    .build(); // Creates a CameraPosition from the builder
////
////            mapboxMap.animateCamera(CameraUpdateFactory
////                    .newCameraPosition(position), 5000);
//            // END  animate camera and redirect
//
//            Point source = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
//                    locationComponent.getLastKnownLocation().getLatitude());
//            Point destination = Point.fromLngLat(72.545257, 23.037244);
//
//            Log.e("*** Lat", locationComponent.getLastKnownLocation().getLatitude() + "");
//            Log.e("*** Lon", locationComponent.getLastKnownLocation().getLongitude() + "");
//
//            //generateRoute(source, destination);
//
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    private void initializeNavigation(MapboxMap mapboxMap) {
//        /*NavigationViewOptions options2 = NavigationViewOptions.builder()
//                .navigationListener(this)
//                .routeListener(this)
//                .feedbackListener(this)
//                .build();*/
//        navigation = new MapboxNavigation(context, YelloDriverApp.getInstance().getString(R.string.mapbox_access_token));
//        navigation.setLocationEngine(locationEngine);
//        navigation.setCameraEngine(new DynamicCamera(mapboxMap));
//        navigation.addProgressChangeListener(this);
//        navigation.addMilestoneEventListener(this);
//        navigation.addOffRouteListener(this);
//        navigationMap.addProgressChangeListener(navigation);
//    }
//
//
//    private void playAnnouncement(Milestone milestone) {
//        if (milestone instanceof VoiceInstructionMilestone) {
//            SpeechAnnouncement announcement = SpeechAnnouncement.builder()
//                    .voiceInstructionMilestone((VoiceInstructionMilestone) milestone)
//                    .build();
//            speechPlayer.play(announcement);
//        }
//    }
//
//    private void moveCameraTo(Location location) {
//        CameraPosition cameraPosition = buildCameraPositionFrom(location, location.getBearing());
//        navigationMap.retrieveMap().animateCamera(
//                CameraUpdateFactory.newCameraPosition(cameraPosition), TWO_SECONDS_IN_MILLISECONDS
//        );
//    }
//
//    private void moveCameraToInclude(Point destination) {
//        LatLng origin = new LatLng(lastLocation);
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(origin)
//                .include(new LatLng(destination.latitude(), destination.longitude()))
//                .build();
//        Resources resources = getResources();
//        //int routeCameraPadding = (int) resources.getDimension(R.dimen.component_navigation_route_camera_padding);
//        int routeCameraPadding = 20;
//        int[] padding = {routeCameraPadding, routeCameraPadding, routeCameraPadding, routeCameraPadding};
//        CameraPosition cameraPosition = navigationMap.retrieveMap().getCameraForLatLngBounds(bounds, padding);
//        navigationMap.retrieveMap().animateCamera(
//                CameraUpdateFactory.newCameraPosition(cameraPosition), TWO_SECONDS_IN_MILLISECONDS
//        );
//    }
//
//    private void moveCameraOverhead() {
//        if (lastLocation == null) {
//            return;
//        }
//        CameraPosition cameraPosition = buildCameraPositionFrom(lastLocation, DEFAULT_BEARING);
//        navigationMap.retrieveMap().animateCamera(
//                CameraUpdateFactory.newCameraPosition(cameraPosition), TWO_SECONDS_IN_MILLISECONDS
//        );
//    }
//
//    @Nullable
//    private CameraUpdate cameraOverheadUpdate() {
//        if (lastLocation == null) {
//            return null;
//        }
//        CameraPosition cameraPosition = buildCameraPositionFrom(lastLocation, DEFAULT_BEARING);
//        return CameraUpdateFactory.newCameraPosition(cameraPosition);
//    }
//
//    @NonNull
//    private CameraPosition buildCameraPositionFrom(Location location, double bearing) {
//        return new CameraPosition.Builder()
//                .zoom(DEFAULT_ZOOM)
//                .target(new LatLng(location.getLatitude(), location.getLongitude()))
//                .bearing(bearing)
//                .tilt(DEFAULT_TILT)
//                .build();
//    }
//
//    private void adjustMapPaddingForNavigation() {
//        Resources resources = getResources();
//        int mapViewHeight = mapView.getHeight();
//        //int bottomSheetHeight = (int) resources.getDimension(R.dimen.component_navigation_bottomsheet_height);
//        int bottomSheetHeight = 180;
//        int topPadding = mapViewHeight - (bottomSheetHeight * BOTTOMSHEET_PADDING_MULTIPLIER);
//        navigationMap.retrieveMap().setPadding(ZERO_PADDING, topPadding, ZERO_PADDING, ZERO_PADDING);
//    }
//
//    private void resetMapAfterNavigation() {
//        navigationMap.removeRoute();
//        navigationMap.clearMarkers();
//        navigation.stopNavigation();
//        moveCameraOverhead();
//    }
//
//    private void removeLocationEngineListener() {
//        if (locationEngine != null) {
//            locationEngine.removeLocationUpdates(callback);
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void addLocationEngineListener() {
//        if (locationEngine != null) {
//            LocationEngineRequest request = buildEngineRequest();
//            locationEngine.requestLocationUpdates(request, callback, null);
//        }
//    }
//
//    private void calculateRouteWith(Point destination, boolean isOffRoute) {
//        Point origin = Point.fromLngLat(lastLocation.getLongitude(), lastLocation.getLatitude());
//        Double bearing = Float.valueOf(lastLocation.getBearing()).doubleValue();
//
//        RouteOptions routeOptions = new RouteOptions() {
//            @Override
//            public String baseUrl() {
//                return null;
//            }
//
//            @Override
//            public String user() {
//                return null;
//            }
//
//            @Override
//            public String profile() {
//                return DirectionsCriteria.PROFILE_DRIVING;
//            }
//
//            @Override
//            public List<Point> coordinates() {
//                return null;
//            }
//
//            @Override
//            public Boolean alternatives() {
//                return null;
//            }
//
//            @Override
//            public String language() {
//                return "en";
//            }
//
//            @Override
//            public String radiuses() {
//                return null;
//            }
//
//            @Override
//            public String bearings() {
//                return null;
//            }
//
//            @Override
//            public Boolean continueStraight() {
//                return null;
//            }
//
//            @Override
//            public Boolean roundaboutExits() {
//                return null;
//            }
//
//            @Override
//            public String geometries() {
//                return null;
//            }
//
//            @Override
//            public String overview() {
//                return null;
//            }
//
//            @Override
//            public Boolean steps() {
//                return true;
//            }
//
//            @Override
//            public String annotations() {
//                return null;
//            }
//
//            @Override
//            public String exclude() {
//                return null;
//            }
//
//            @Override
//            public Boolean voiceInstructions() {
//                return true;
//            }
//
//            @Override
//            public Boolean bannerInstructions() {
//                return true;
//            }
//
//            @Override
//            public String voiceUnits() {
//                return "metric";
//            }
//
//            @Override
//            public String accessToken() {
//                return Mapbox.getAccessToken();
//            }
//
//            @Override
//            public String requestUuid() {
//                return null;
//            }
//
//            @Override
//            public String approaches() {
//                return null;
//            }
//
//            @Override
//            public String waypointIndices() {
//                return null;
//            }
//
//            @Override
//            public String waypointNames() {
//                return null;
//            }
//
//            @Override
//            public String waypointTargets() {
//                return null;
//            }
//
//            @Override
//            public WalkingOptions walkingOptions() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Builder toBuilder() {
//                return null;
//            }
//        };
//
//        NavigationRoute.builder(context)
//                .accessToken(YelloDriverApp.getInstance().getString(R.string.mapbox_access_token))
//                .origin(origin, bearing, BEARING_TOLERANCE)
//                .destination(destination)
//                .routeOptions(routeOptions)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
//                        handleRoute(response, isOffRoute);
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable throwable) {
//                        YLog.e("Error",throwable.toString());
//                    }
//                });
//    }
//
//
//    public void navigateToDestination(Point destination)
//    {
//        this.destination = destination;
//        if(mapLoaded && lastLocation != null) calculateRouteWith(destination, false);
//    }
//
//    private void quickStartNavigation() {
//        // Transition to navigation state
//
//
//        // Show the InstructionView
//        //instructionView.setVisibility(View.VISIBLE);
//
//        adjustMapPaddingForNavigation();
//        // Updates camera with last location before starting navigating,
//        // making sure the route information is updated
//        // by the time the initial camera tracking animation is fired off
//        // Alternatively, NavigationMapboxMap#startCamera could be used here,
//        // centering the map camera to the beginning of the provided route
//        navigationMap.resumeCamera(lastLocation);
//
//        // Start for auto simulate for testing purpose
//        ReplayRouteLocationEngine templocationEngine = new ReplayRouteLocationEngine();
//        ((ReplayRouteLocationEngine) templocationEngine).assign(route);
//        navigation.setLocationEngine(templocationEngine);
//        // End for auto simulate for testing purpose
//
//        navigation.startNavigation(route);
//
//        // Location updates will be received from ProgressChangeListener
//        removeLocationEngineListener();
//
//        // TODO remove example usage
//        navigationMap.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS);
//        CameraUpdate cameraUpdate = cameraOverheadUpdate();
//        if (cameraUpdate != null) {
//            NavigationCameraUpdate navUpdate = new NavigationCameraUpdate(cameraUpdate);
//            navigationMap.retrieveCamera().update(navUpdate);
//        }
//    }
//
//    private void handleRoute(Response<DirectionsResponse> response, boolean isOffRoute) {
//        List<DirectionsRoute> routes = response.body().routes();
//        if (!routes.isEmpty()) {
//            route = routes.get(FIRST);
//            navigationMap.drawRoute(route);
//            if (isOffRoute) {
//                navigation.startNavigation(route);
//            }else quickStartNavigation();
//        }
//    }
//
//
//    @NonNull
//    private LocationEngineRequest buildEngineRequest() {
//        return new LocationEngineRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
//                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
//                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
//                .build();
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            mapboxMap.getStyle(new Style.OnStyleLoaded() {
//                @Override
//                public void onStyleLoaded(@NonNull Style style) {
//                    initializeLocationEngine(style);
//                }
//            });
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    private static class MapboxNavigationViewLocationCallback implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<MapboxNavigationView> activityWeakReference;
//
//        MapboxNavigationViewLocationCallback(MapboxNavigationView activity) {
//            this.activityWeakReference = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onSuccess(LocationEngineResult result) {
//            MapboxNavigationView activity = activityWeakReference.get();
//            if (activity != null) {
//                Location location = result.getLastLocation();
//                if (location == null) {
//                    return;
//                }
//                activity.checkFirstUpdate(location);
//                activity.updateLocation(location);
//            }
//        }
//
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//
//        }
//    }
//
//
//}