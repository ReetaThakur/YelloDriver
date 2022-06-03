//package com.app.yellodriver.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
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
//import com.mapbox.mapboxsdk.maps.Image;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.style.layers.LineLayer;
//import com.mapbox.mapboxsdk.style.layers.Property;
//import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
//import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
//import com.mapbox.services.android.navigation.ui.v5.camera.DynamicCamera;
//import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCamera;
//import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCameraUpdate;
//import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionView;
//import com.mapbox.services.android.navigation.ui.v5.map.NavigationMapboxMap;
//import com.mapbox.services.android.navigation.ui.v5.voice.NavigationSpeechPlayer;
//import com.mapbox.services.android.navigation.ui.v5.voice.SpeechAnnouncement;
//import com.mapbox.services.android.navigation.ui.v5.voice.SpeechPlayerProvider;
//import com.mapbox.services.android.navigation.ui.v5.voice.VoiceInstructionLoader;
//import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
//import com.mapbox.services.android.navigation.v5.milestone.BannerInstructionMilestone;
//import com.mapbox.services.android.navigation.v5.milestone.Milestone;
//import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
//import com.mapbox.services.android.navigation.v5.milestone.VoiceInstructionMilestone;
//import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
//import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.util.List;
//import java.util.Locale;
//
//import okhttp3.Cache;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import timber.log.Timber;
//
//import static android.graphics.Color.parseColor;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.color;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
//import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
//
//
//public class MapNavigationFragment extends BaseFragment implements OnMapReadyCallback,
//        MapboxMap.OnMapLongClickListener, ProgressChangeListener, MilestoneEventListener, OffRouteListener, PermissionsListener {
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
//    private static final double DEFAULT_ZOOM = 16.0;
//    private static final double DEFAULT_TILT = 0d;
//    private static final double DEFAULT_BEARING = 0d;
//    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
//    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
//
//
//    private static final String ORIGIN_ICON_ID = "origin-icon-id";
//    private static final String DESTINATION_ICON_ID = "destination-icon-id";
//    private static final String ROUTE_LAYER_ID = "route-layer-id";
//    private static final String ROUTE_LINE_SOURCE_ID = "route-source-id";
//    private static final String ICON_LAYER_ID = "icon-layer-id";
//    private static final String ICON_SOURCE_ID = "icon-source-id";
//    private static final float LINE_WIDTH = 6f;
//    private static final String ORIGIN_COLOR = "#2096F3";
//    private static final String DESTINATION_COLOR = "#F84D4D";
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
//
//    private FloatingActionButton fabRecentre;
//    private ImageView imgDirectionIcon;
//    private TextView tvPrimeInstructions;
//    private TextView tvSecInstructions;
//    private TextView tvNextDistance;
//    private MapboxMap mapboxMap;
//    private TextView tvArrivalTime;
//    private TextView tvTotDistanceAndETA;
//
//    @Override
//    protected int defineLayoutResource() {
//        getActivity().setTheme(R.style.NavigationViewLight);
//        return R.layout.custom_navigation;
//    }
//
//    @Override
//    protected void initializeComponent(View view) {
//
//        context = getActivity();
//
//        Mapbox.getInstance(context, getString(R.string.mapbox_access_token));
//
//        Locale locale = new Locale("en");
//
//        mapView = view.findViewById(R.id.mapView);
//        instructionView = view.findViewById(R.id.instructionView);
//        mapView.onCreate(null);
//        mapView.getMapAsync(this);
//
//        //imgDirectionIcon = findViewById(R.id.imgdirectionIcon);
//        //tvPrimeInstructions = findViewById(R.id.tvprimaryinstructions);
//        //tvNextDistance = findViewById(R.id.tvnextdistance);
//        imgDirectionIcon = view.findViewById(R.id.ivnextdirection);//for bottom sheet
//        tvPrimeInstructions = view.findViewById(R.id.tvprimeinstructions);//for bottom sheet
//        tvSecInstructions = view.findViewById(R.id.tvsecondaryinstructions);
//        tvNextDistance = view.findViewById(R.id.tvremainingstepdistance);//for bottom sheet
//
//        tvArrivalTime = view.findViewById(R.id.tvarrivaltime);
//        tvTotDistanceAndETA = view.findViewById(R.id.tvtotaldistETA);
//
//        fabRecentre = view.findViewById(R.id.fabRecentre);
//        fabRecentre.setVisibility(View.GONE);
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
//        mapboxMap.setStyle(new Style.Builder().fromUri(context.getString(R.string.navigation_guidance_day)), style -> {
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
//            initLayers(style);
//
//            // For voice instructions
//            initializeSpeechPlayer();
//            mapLoaded = true;
//            //destination = Point.fromLngLat(72.545257, 23.037244);
//            //destination = Point.fromLngLat(70.7877, 22.2915);//kotecha
//            destination = Point.fromLngLat(70.7881, 22.2996);//balbhavan
//            if (destination != null) {
//                navigateToDestination(destination);
//            }
//        });
//    }
//
//
//    /**
//     * Add the route and marker icon layers to the map
//     */
//    private void initLayers(@NonNull Style loadedMapStyle) {
//// Add the LineLayer to the map. This layer will display the directions route.
//        loadedMapStyle.addLayer(new LineLayer(ROUTE_LAYER_ID, ROUTE_LINE_SOURCE_ID).withProperties(
//                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
//                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
//                PropertyFactory.lineWidth(LINE_WIDTH),
//                PropertyFactory.lineGradient(interpolate(linear(), lineProgress(),
//
//// This is where the gradient color effect is set. 0 -> 1 makes it a two-color gradient
//// See LineGradientActivity for an example of a 2+ multiple color gradient line.
//                        stop(0f, color(parseColor(ORIGIN_COLOR))),
//                        stop(1f, color(parseColor(DESTINATION_COLOR)))
//                ))));
//
//// Add the SymbolLayer to the map to show the origin and destination pin markers
//        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
//                iconImage(match(get("originDestination"), literal("origin"),
//                        stop("origin", ORIGIN_ICON_ID),
//                        stop("destination", DESTINATION_ICON_ID))),
//                iconIgnorePlacement(true),
//                iconAllowOverlap(true),
//                iconOffset(new Float[] {0f, -4f})));
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
//   /* @Override
//    protected  void onDetachedFromWindow() {
//        navigation.onDestroy();
//        super.onDetachedFromWindow();
//    }*/
//
//    @Override
//    public void onDestroy() {
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
//            Log.i("legDuration", "" + routeProgress.durationRemaining());
//
//            //if (null != routeProgress.bannerInstruction()) {
//
//            //tvNextDistance.setText(formatDistance(routeProgress.bannerInstruction().getRemainingStepDistance()));
//            TextView stepText = instructionView.findViewById(R.id.stepDistanceText);
//            tvNextDistance.setText(stepText.getText());
//
//            //Log.i("legProgress",""+formatDistance(routeProgress.currentLeg().steps().get(0).distance()));
//            //}
//
//            int secondstoDestination = (int) routeProgress.durationRemaining();
//            int sec = secondstoDestination % 60;
//            int hr = secondstoDestination / 60;
//            int min = hr % 60;
//            hr = hr / 60;
//
//            String arrivalTime = "Arrival in ";
//            if (hr != 0) {
//                arrivalTime += hr + "hr(s)";
//            }
//            if (min != 0) {
//                arrivalTime += min + "min(s)";
//            }
//            if (sec != 0) {
//                arrivalTime += sec + "second(s)";
//            }
//
//            tvArrivalTime.setText(arrivalTime);
//        }
//
//    }
//
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
//            try {
//                Drawable drawable = getResources().getDrawable(getResources().getIdentifier(imageUrl, "drawable", context.getPackageName()));
//
//                Glide.with(MapNavigationFragment.this).load(drawable).into(imgDirectionIcon);
//            } catch (Resources.NotFoundException e) {
//                e.printStackTrace();
//            }
//
//            String directionPrimaryText = primary.text();
//
//            if (!directionPrimaryText.isEmpty()) {
//                tvPrimeInstructions.setText(directionPrimaryText);
//            }
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
//
//        }
//    }
//
//    @Override
//    public void userOffRoute(Location location) {
//        calculateRouteWith(destination, true);
//    }
//
//    /*
//     * Activity lifecycle methods
//     */
//
//
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
//
//    @SuppressLint("MissingPermission")
//    private void initializeLocationEngine(Style loadedMapStyle) {
//        if (PermissionsManager.areLocationPermissionsGranted(context)) {
//            locationEngine = LocationEngineProvider.getBestLocationEngine(context);
//            LocationEngineRequest request = buildEngineRequest();
//
//            locationEngine.requestLocationUpdates(request, callback, null);
//
//            /*----customisation for icons----*/
//            // Create and customize the LocationComponent's options
//            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(context)
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
//                    LocationComponentActivationOptions.builder(context, loadedMapStyle)
//                            .locationComponentOptions(customLocationComponentOptions)
//                            .build();
//
//            // Activate with options
//            locationComponent.activateLocationComponent(locationComponentActivationOptions);
//            /*-----customisation ends-------*/
//            fabRecentre.setVisibility(View.VISIBLE);
//
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(getActivity());
//        }
//    }
//
//
//    private void initializeNavigation(MapboxMap mapboxMap) {
//
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
//    /*private void adjustMapPaddingForNavigation() {
//        Resources resources = getResources();
//        int mapViewHeight = mapView.getHeight();
//        //int bottomSheetHeight = (int) resources.getDimension(R.dimen.component_navigation_bottomsheet_height);
//        int bottomSheetHeight = 80;
//        int topPadding = mapViewHeight - (bottomSheetHeight * BOTTOMSHEET_PADDING_MULTIPLIER);
//        navigationMap.retrieveMap().setPadding(ZERO_PADDING, topPadding, ZERO_PADDING, ZERO_PADDING);
//    }*/
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
//    public void navigateToDestination(Point destination) {
//        this.destination = destination;
//        if (mapLoaded && lastLocation != null) calculateRouteWith(destination, false);
//    }
//
//    private void quickStartNavigation() {
//        // Transition to navigation state
//
//
//        // Show the InstructionView
//        instructionView.setVisibility(View.VISIBLE);
//
//        //adjustMapPaddingForNavigation();
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
//            //Log.i("TotalDistanceNDuration",":"+route.distance()+""+route.duration());
//            //Todo: Format it later
//            tvTotDistanceAndETA.setText(route.distance() + ":" + route.duration());
//            if (isOffRoute) {
//                navigation.startNavigation(route);
//            } else quickStartNavigation();
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
//        Toast.makeText(context, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
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
//            Toast.makeText(context, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    private static class MapboxNavigationViewLocationCallback implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<MapNavigationFragment> activityWeakReference;
//
//        MapboxNavigationViewLocationCallback(MapNavigationFragment activity) {
//            this.activityWeakReference = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onSuccess(LocationEngineResult result) {
//            MapNavigationFragment activity = activityWeakReference.get();
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