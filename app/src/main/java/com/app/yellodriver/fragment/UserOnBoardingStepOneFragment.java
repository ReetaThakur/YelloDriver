package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Mutation;
import com.app.yellodriver.R;
import com.app.yellodriver.SendOtpMutation;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelHome.HomeViewModel;
import com.app.yellodriver.model.ModelUserOnboarding.ModelSendOtp.ModelSendOtp;
import com.app.yellodriver.model.ModelUserOnboarding.UserOnboardingViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

public class UserOnBoardingStepOneFragment extends BaseFragment {

    private Button btnGetOtp;
    private TextInputEditText edtMobileNumber;
    private TextInputEditText edtFullName;
    private TextInputEditText edtCountryCode;
    private TextInputLayout txtMobile;
    private TextInputLayout txtFullName;
    private UserOnboardingViewModel viewModel;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private String mobileNumber;
    private String fullName;
    private String countryCode = "+1";

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

        edtMobileNumber = view.findViewById(R.id.user_onboarding_step_one_edtMobile);
        edtFullName = view.findViewById(R.id.user_onboarding_step_one_edtName);
        edtCountryCode = view.findViewById(R.id.user_onboarding_step_one_edtCountryCode);
        txtMobile = view.findViewById(R.id.user_onboarding_step_one_txtMobile);
        txtFullName = view.findViewById(R.id.user_onboarding_step_one_txtName);
        btnGetOtp = view.findViewById(R.id.fragment_user_onboarding_step_one_btnGetOtp);

        btnCancel.setVisibility(View.VISIBLE);

        tvName.setText(R.string.lbl_user_boarding);

        viewModel = ViewModelProviders.of((HomeActivity) mActivity).get(UserOnboardingViewModel.class);
        viewModel.liveDataSendOtp.observe(getViewLifecycleOwner(), observerSendOtp);

        imgBack.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        edtCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        edtCountryCode.setText(dialCode);
                        countryCode = dialCode;
                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "COUNTRY_PICKER");
            }
        });

        btnGetOtp.setOnClickListener(view12 -> {

            mobileNumber = edtMobileNumber.getText().toString();
            fullName = edtFullName.getText().toString();

            if (checkValidation(mobileNumber, fullName)) {

                customProgressDialog = new CustomProgressDialog(mActivity);
                customProgressDialog.showDialog();

                SendOtpMutation sendOtpMutation = new SendOtpMutation(countryCode, mobileNumber);

                viewModel.sendOtp(sendOtpMutation);
            }
        });
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_user_onboarding_step_one;
    }

    Observer<ModelSendOtp.Data.SendOtp> observerSendOtp = sendOtp -> {

        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        sendOtp.getUserOtp().setFullName(fullName);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.INTENT_KEY_MODEL_USER_OTP, sendOtp.getUserOtp());
        bundle.putString(Constants.INTENT_KEY_COUNTRY_CODE, countryCode);
        UserOnBoardingStepTwoFragment userOnBoardingStepTwoFragment = new UserOnBoardingStepTwoFragment();
        userOnBoardingStepTwoFragment.setArguments(bundle);

        addFragment(R.id.activity_home_flContainer, UserOnBoardingStepOneFragment.this, userOnBoardingStepTwoFragment, false, false);
    };

    private boolean checkValidation(String mobileNumber, String fullName) {

        if (mobileNumber.length() == 0) {
            txtMobile.setError(getString(R.string.err_emptymobile));
            return false;
        } else if (!Utils.isValidPhoneNumber(Constants.COUNTRY_CODE + mobileNumber)) {
            txtMobile.setError(getString(R.string.err_invalidmobile));
            return false;
        } else if (fullName.length() == 0) {
            txtFullName.setError(getString(R.string.err_emptyname));
            return false;
        } else {
            return true;
        }
    }

}