package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.yellodriver.CreateUserMutation;
import com.app.yellodriver.R;
import com.app.yellodriver.SendOtpMutation;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.customview.OtpView;
import com.app.yellodriver.model.ModelUserOnboarding.ModelCreateUser.ModelCreateUser;
import com.app.yellodriver.model.ModelUserOnboarding.ModelSendOtp.ModelSendOtp;
import com.app.yellodriver.model.ModelUserOnboarding.UserOnboardingViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.listener.OnOtpCompletionListener;
import com.developer.kalert.KAlertDialog;

public class UserOnBoardingStepTwoFragment extends BaseFragment {

    private Button btnNext;
    private OtpView otpView;
    private TextView txtResendOtp;
    private String enteredOTP;
    private String countryCode;

    private UserOnboardingViewModel viewModel;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;

    private ModelSendOtp.Data.SendOtp.UserOtp userOtp;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    protected void initializeComponent(View view) {

        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
        Button btnCancel = view.findViewById(R.id.toolbar_btnCancel);
        btnNext = view.findViewById(R.id.fragment_user_onboarding_step_two_btnNext);
        otpView = view.findViewById(R.id.user_onboarding_step_two_otpView);
        txtResendOtp = view.findViewById(R.id.user_onboarding_step_two_txtResendOtp);

        Bundle bundle = getArguments();

        if (bundle != null) {
            userOtp = bundle.getParcelable(Constants.INTENT_KEY_MODEL_USER_OTP);
            countryCode = bundle.getString(Constants.INTENT_KEY_COUNTRY_CODE);
        }

        btnCancel.setVisibility(View.VISIBLE);

        tvName.setText(R.string.lbl_user_boarding);

        imgBack.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                // do Stuff
                Log.d("onOtpCompleted=>", otp);
                enteredOTP = otp;
                createUser();
            }
        });

        btnNext.setOnClickListener(view12 -> {
            if (otpView.length() == 0) {
                final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                        .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                        .setContentText(YelloDriverApp.getInstance().getString(R.string.enter_6_digit_otp))
                        .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                pDialog.show();
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
            } else {
                createUser();
            }
        });

        txtResendOtp.setOnClickListener(view13 -> {

            customProgressDialog = new CustomProgressDialog(mActivity);
            customProgressDialog.showDialog();

            otpView.setText("");
            SendOtpMutation sendOtpMutation = new SendOtpMutation(Constants.COUNTRY_CODE, userOtp.getMobile());

            viewModel.sendOtp(sendOtpMutation);
        });

        viewModel = ViewModelProviders.of((HomeActivity) mActivity).get(UserOnboardingViewModel.class);
        viewModel.liveDataCreateUser.observe(getViewLifecycleOwner(), observerCreateUser);
        viewModel.liveDataSendOtp.observe(getViewLifecycleOwner(), observerSendOtp);
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_user_onboarding_step_two;
    }

    Observer<ModelSendOtp.Data.SendOtp> observerSendOtp = sendOtp -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }
            userOtp = sendOtp.getUserOtp();
        }
    };

    Observer<ModelCreateUser.Data.CreateUser> observerCreateUser = createUser -> {

        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }
            if (createUser != null) {
                if (createUser.getUser_id() != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_KEY_ON_BOARD_USER_ID, createUser.getUser_id());
                    UserOnBoardingStepThreeFragment userOnBoardingStepThreeFragment = new UserOnBoardingStepThreeFragment();
                    userOnBoardingStepThreeFragment.setArguments(bundle);

                    addFragment(R.id.activity_home_flContainer, UserOnBoardingStepTwoFragment.this, userOnBoardingStepThreeFragment, false, false);
                }
            }
        }
    };


    private void createUser() {

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        String mobileNumber = userOtp.getMobile();
        String otp = enteredOTP;
        String fullName = userOtp.getFullName();
        String otpSecret = userOtp.getSecret();
        String type = Constants.USER_TYPE_RIDER;

        CreateUserMutation createUserMutation = new CreateUserMutation(countryCode, mobileNumber, type, otpSecret, fullName, otp);
        viewModel.createUser(createUserMutation);
    }
}