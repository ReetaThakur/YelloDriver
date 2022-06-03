package com.app.yellodriver.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.CapturePaymentMutation;
import com.app.yellodriver.CreatePaymentMutation;
import com.app.yellodriver.CreateStripeTokenMutation;
import com.app.yellodriver.PlanQuery;
import com.app.yellodriver.PurchaseBoardingPassMutation;
import com.app.yellodriver.R;
import com.app.yellodriver.StripeTerminal.NavigationListener;
import com.app.yellodriver.StripeTerminal.TerminalEventListener;
import com.app.yellodriver.StripeTerminal.TokenProvider;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelStripe.ModelCapturePayment;
import com.app.yellodriver.model.ModelStripe.ModelStripeToken;
import com.app.yellodriver.model.ModelStripe.StripeViewModel;
import com.app.yellodriver.model.ModelUserOnboarding.ModelCreatePayment.ModelCreatePayment;
import com.app.yellodriver.model.ModelUserOnboarding.ModelPlan.ModelPlan;
import com.app.yellodriver.model.ModelUserOnboarding.ModelPurchaseBoardingPass;
import com.app.yellodriver.model.ModelUserOnboarding.UserOnboardingViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.YLog;
import com.developer.kalert.KAlertDialog;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.stripe.stripeterminal.Terminal;
import com.stripe.stripeterminal.callable.Callback;
import com.stripe.stripeterminal.callable.Cancelable;
import com.stripe.stripeterminal.callable.DiscoveryListener;
import com.stripe.stripeterminal.callable.PaymentIntentCallback;
import com.stripe.stripeterminal.callable.PaymentMethodCallback;
import com.stripe.stripeterminal.callable.ReaderCallback;
import com.stripe.stripeterminal.callable.ReaderDisplayListener;
import com.stripe.stripeterminal.log.LogLevel;
import com.stripe.stripeterminal.model.external.ConnectionStatus;
import com.stripe.stripeterminal.model.external.DeviceType;
import com.stripe.stripeterminal.model.external.DiscoveryConfiguration;
import com.stripe.stripeterminal.model.external.PaymentIntent;
import com.stripe.stripeterminal.model.external.PaymentIntentParameters;
import com.stripe.stripeterminal.model.external.PaymentMethod;
import com.stripe.stripeterminal.model.external.Reader;
import com.stripe.stripeterminal.model.external.ReaderDisplayMessage;
import com.stripe.stripeterminal.model.external.ReaderInputOptions;
import com.stripe.stripeterminal.model.external.TerminalException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;
import rebus.permissionutils.SmartCallback;

public class UserOnBoardingStepThreeFragment extends BaseFragment implements NavigationListener, DiscoveryListener, ReaderDisplayListener {

    private FrameLayout flMain;
    private CarouselView carouselView;

    private UserOnboardingViewModel viewModel;
    private StripeViewModel stripeViewModel;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private ArrayList<ModelPlan.Data.YtPlan> planArrayList = new ArrayList<>();
    private String secretToken;
    private String orderId;

    private boolean isSimulate = false;
    public List<? extends Reader> readers = new ArrayList<>();
    public Cancelable discoveryTask;
    private String onBoardUserId;
    private String planId;
    private int price;
    private PaymentIntent paymentIntent;
    private String paymentId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    protected void initializeComponent(View view) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            onBoardUserId = bundle.getString(Constants.INTENT_KEY_ON_BOARD_USER_ID);
        }

        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
        Button btnCancel = view.findViewById(R.id.toolbar_btnCancel);

        carouselView = view.findViewById(R.id.fragment_user_onboarding_step_three_CarouselView);

        btnCancel.setVisibility(View.VISIBLE);

        tvName.setText(R.string.lbl_user_boarding);

        viewModel = ViewModelProviders.of((HomeActivity) mActivity).get(UserOnboardingViewModel.class);
        viewModel.liveDataPlan.observe(getViewLifecycleOwner(), observerPlanList);

        stripeViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(StripeViewModel.class);
        stripeViewModel.liveDataStripeToken.observe(getViewLifecycleOwner(), observerStripeToken);
        stripeViewModel.liveDataPurchaseBoardingPass.observe(getViewLifecycleOwner(), observerPurchasePass);
        stripeViewModel.liveDataCreatePayment.observe(getViewLifecycleOwner(), observerPayment);
        stripeViewModel.liveDataCapturePayment.observe(getViewLifecycleOwner(), observerCapturePayment);

        getAllPlanList();

        imgBack.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_user_onboarding_step_three;
    }

    private void getAllPlanList() {

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        PlanQuery planQuery = new PlanQuery();
        viewModel.getPlanList(planQuery);
    }

    Observer<ArrayList<ModelPlan.Data.YtPlan>> observerPlanList = listPlans -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            planArrayList = listPlans;

            carouselView.setSize(planArrayList.size());
            planArrayList.get(0).setSelected(true);

            carouselView.setCarouselViewListener(new CarouselViewListener() {
                @Override
                public void onBindView(View view, int position) {
                    // Example here is setting up a full image carousel
                    TextView txtPlanName = view.findViewById(R.id.row_select_plan_tvPlanName);
                    TextView txtPlanPrice = view.findViewById(R.id.row_select_plan_tvPlanPrice);
//                    TextView txtPlanDetails = view.findViewById(R.id.row_select_plan_tvActiveUser);

                    TextView txtValidity = view.findViewById(R.id.row_select_plan_tvValidity);
                    TextView txtUnlimited = view.findViewById(R.id.row_select_plan_tvUnlimited);

                    Button btnSelect = view.findViewById(R.id.row_select_plan_btnSelect);
                    LinearLayout llMain = view.findViewById(R.id.row_select_plan_llMain);
                    flMain = view.findViewById(R.id.row_select_plan_FlMain);

                    txtValidity.setText(YelloDriverApp.getInstance().getString(R.string.hours_validity, Integer.parseInt(planArrayList.get(position).getValidityDays()) * 24));

                    if (planArrayList.get(position).getUnlimitedTrips()) {
                        txtUnlimited.setText(YelloDriverApp.getInstance().getString(R.string.unlimited_ride));
                    } else {
                        txtUnlimited.setText(YelloDriverApp.getInstance().getString(R.string.limited_rides, planArrayList.get(position).getTotalTrips()));
                    }

//                imageView.setImageDrawable(getResources().getDrawable(images[position]));
                    txtPlanName.setText(planArrayList.get(position).getTitle());
//                    txtPlanDetails.setText(planArrayList.get(position).getDescription());
                    txtPlanPrice.setText("$" + planArrayList.get(position).getPrice());

                    if (planArrayList.get(position).isSelected()) {
                        flMain.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_plan_selected));
                    } else {
                        flMain.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_plan_deselect));
                    }

                    btnSelect.setOnClickListener(view4 -> {
                        YLog.e("Position", position + "");
                        planId = planArrayList.get(position).getId();
                        price = planArrayList.get(position).getPrice();
                        createTokenMutation();
                    });
                }
            });
            // After you finish setting up, show the CarouselView
            carouselView.show();

            carouselView.setCarouselScrollListener(new CarouselScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState, int position) {
//                    Log.e("*** Position", position + "");
//                flMain.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.img_plan_selected));

                    for (int i = 0; i < planArrayList.size(); i++) {
                        planArrayList.get(i).setSelected(false);
                    }

                    planArrayList.get(position).setSelected(true);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    Log.e("*** DX", dx + "");
//                    Log.e("*** DY", dy + "");
                }
            });
        }
    };

    private void createTokenMutation() {
        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        CreateStripeTokenMutation createStripeTokenMutation = new CreateStripeTokenMutation();
        stripeViewModel.createStripeToken(createStripeTokenMutation);
    }

    Observer<ModelStripeToken.Data.CreateDeviceToken> observerStripeToken = createDeviceToken -> {
//
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (createDeviceToken != null) {
            secretToken = createDeviceToken.getSecret();
//            setUpStripe(secretToken);
            checkForLocationPermissionAndConnect();
        }
    };

    Observer<ModelPurchaseBoardingPass.Data.PurchaseBoardingPass> observerPurchasePass = purchaseBoardingPass -> {

        if (purchaseBoardingPass != null) {

            orderId = purchaseBoardingPass.getOrderId();

            CreatePaymentMutation createPaymentMutation = new CreatePaymentMutation(purchaseBoardingPass.getOrderId(), true, Constants.PAYMENT_CURRENCY);
            stripeViewModel.createPayment(createPaymentMutation);
        }
    };

    Observer<ModelCapturePayment.Data.CapturePayment> observerCapturePayment = capturePayments -> {

        if (capturePayments != null) {
            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            Bundle bundle = new Bundle();
            bundle.putString(Constants.INTENT_KEY_ORDER_ID, orderId);
            UserOnBoardingStepFourFragment userOnBoardingStepFourFragment = new UserOnBoardingStepFourFragment();
            userOnBoardingStepFourFragment.setArguments(bundle);

            addFragment(R.id.activity_home_flContainer, UserOnBoardingStepThreeFragment.this, userOnBoardingStepFourFragment, false, false);
        }
    };

    Observer<ModelCreatePayment.Data.CreatePayment> observerPayment = createPayment -> {

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

//                    final PaymentIntentParameters params = new PaymentIntentParameters.Builder()
//                            .setAmount(price)
//                            .setCurrency(Constants.PAYMENT_CURRENCY)
//                            .build();

//                    PaymentIntent intent = // ... Fetch or create the PaymentIntent

//                    Terminal.getInstance().createPaymentIntent(params, createPaymentIntentCallback);

                    customProgressDialog = new CustomProgressDialog(mActivity);
                    customProgressDialog.showDialog();

//                    Map<String, String> map = new HashMap();
//                    map.put("client_secret", createPayment.getClientSecret());

                    paymentId = createPayment.getPaymentId();
                    Terminal.getInstance().retrievePaymentIntent(createPayment.getClientSecret(), createPaymentIntentCallback);
                }
            });
        }
    };

    private void setUpStripe(String secretToken) {

        YLog.e("PAYMENT", "secret token " + secretToken);
        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        try {
            Terminal.initTerminal(mActivity, LogLevel.VERBOSE, new TokenProvider(secretToken),
                    new TerminalEventListener());

            final DiscoveryConfiguration config = new DiscoveryConfiguration(0, DeviceType.CHIPPER_2X, isSimulate);

            if (discoveryTask == null && Terminal.getInstance().getConnectedReader() == null) {
                Terminal.getInstance().discoverReaders(config, this, discoveryCallback);
            }

        } catch (TerminalException e) {
            throw new RuntimeException("Location services are required in order to initialize " +
                    "the Terminal.", e);
        }
    }

    private void checkForLocationPermissionAndConnect() {

        if (BluetoothAdapter.getDefaultAdapter() != null &&
                !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            BluetoothAdapter.getDefaultAdapter().enable();
        }

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
                        checkForLocationPermissionAndConnect();
                    } else if (somePermissionsDeniedForever) {
                        YLog.e("Permission", "Denied Forever");
                        showDialogForDenyForever();
                    }
                }
            }).ask(UserOnBoardingStepThreeFragment.this);
        }
    }

    private void checkForGpsLocation() {

        final LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
//        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGpsEnabled) {
            setUpStripe(secretToken);
        } else {
            showGpsLocationDialog();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.handleResult(UserOnBoardingStepThreeFragment.this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.LOCATION_REQUEST_SETTINGS) {
            checkForLocationPermissionAndConnect();
        }

        if (requestCode == Constants.GPS_REQUEST) {
            checkForGpsLocation();
        }
    }

    final Callback discoveryCallback = new Callback() {
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
        createTokenMutation();
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
        PurchaseBoardingPassMutation purchaseBoardingPassMutation = new PurchaseBoardingPassMutation(planId, onBoardUserId);
        stripeViewModel.purchaseBoardingPass(purchaseBoardingPassMutation);
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

//            addEvent("Processed payment", "terminal.processPayment");
//            try {
//                ApiClient.capturePaymentIntent(paymentIntent.getId());
//                addEvent("Captured PaymentIntent", "backend.capturePaymentIntent");
//                completeFlow();
//            } catch (IOException e) {
//                Log.e("StripeExample", e.getMessage(), e);
//                completeFlow();
//            }
            completeFlow(true, "");
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            UserOnBoardingStepThreeFragment.this.onFailure(e);
        }
    };

    @NotNull
    private final PaymentIntentCallback cancelPaymentIntentCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent paymentIntent) {
//            addEvent("Canceled PaymentIntent", "terminal.cancelPaymentIntent");
//            final FragmentActivity activity = activityRef.get();
//            if (activity instanceof NavigationListener) {
            onCancelCollectPaymentMethod();
//            }
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            UserOnBoardingStepThreeFragment.this.onFailure(e);
        }
    };

    @NotNull
    private final PaymentIntentCallback collectPaymentMethodCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent paymentIntent) {
//            addEvent("Collected PaymentMethod", "terminal.collectPaymentMethod");
            Terminal.getInstance().processPayment(paymentIntent, processPaymentCallback);
//            viewModel.collectTask = null;
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            UserOnBoardingStepThreeFragment.this.onFailure(e);
        }
    };

    @NotNull
    private final PaymentIntentCallback createPaymentIntentCallback = new PaymentIntentCallback() {
        @Override
        public void onSuccess(@NotNull PaymentIntent intent) {
            YLog.e("PAYMENT", "onSuccess createPaymentIntentCallback");

            paymentIntent = intent;
//            addEvent("Created PaymentIntent", "terminal.createPaymentIntent");
            Terminal.getInstance().collectPaymentMethod(paymentIntent, UserOnBoardingStepThreeFragment.this, collectPaymentMethodCallback);
//        viewModel.collectTask = Terminal.getInstance().collectPaymentMethod(
//                    paymentIntent, EventFragment.this, collectPaymentMethodCallback);
        }

        @Override
        public void onFailure(@NotNull TerminalException e) {
            UserOnBoardingStepThreeFragment.this.onFailure(e);
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

//        addEvent(options.toString(), "listener.onRequestReaderInput");
    }

    @Override
    public void onRequestReaderDisplayMessage(@NotNull ReaderDisplayMessage message) {
        YLog.e("PAYMENT", "onRequestReaderDisplayMessage " + message.toString());

//        addEvent(message.toString(), "listener.onRequestReaderDisplayMessage");
    }

    private void completeFlow(boolean isSuccess, String message) {
        YLog.e("PAYMENT", "completeFlow ");

        if (isSuccess) {

            CapturePaymentMutation capturePaymentMutation = new CapturePaymentMutation(paymentId);
            stripeViewModel.capturePayment(capturePaymentMutation);

            if (BluetoothAdapter.getDefaultAdapter() != null &&
                    !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                BluetoothAdapter.getDefaultAdapter().disable();
            }

            if (Terminal.isInitialized()) {
                if (discoveryCallback != null) {
                    Terminal.getInstance().disconnectReader(discoveryCallback);
                }
            }

        } else {
            // Some error message
            final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                    .setContentText(message)
                    .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
            pDialog.show();
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);

            if (BluetoothAdapter.getDefaultAdapter() != null &&
                    !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                BluetoothAdapter.getDefaultAdapter().disable();
            }

            if (Terminal.isInitialized()) {
                if (discoveryCallback != null) {
                    Terminal.getInstance().disconnectReader(discoveryCallback);
                }
            }
        }
    }

    private void onFailure(@NotNull TerminalException e) {
        YLog.e("PAYMENT", "completeFlow " + e.getErrorMessage());

        completeFlow(false, e.toString());
    }
}