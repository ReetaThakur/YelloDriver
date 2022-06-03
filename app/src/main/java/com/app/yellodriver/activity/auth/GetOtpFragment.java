package com.app.yellodriver.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.yellodriver.R;
import com.app.yellodriver.fragment.BaseFragment;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GetOtpFragment extends BaseFragment implements View.OnClickListener {
    TextInputEditText edtMobileNumber;
    private Button btnGetOtp;
    private TextInputLayout txtInputMobile;
    private LinearLayout llMobileLoginForm;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initializeComponent(View view) {
        txtInputMobile = view.findViewById(R.id.tilmobileforotp);
        edtMobileNumber = view.findViewById(R.id.fragment_get_otp_edtMobileNumber);
        edtMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputMobile.setError("");
                }
            }
        });

        btnGetOtp = (Button) view.findViewById(R.id.fragment_get_otp_btnGetOtp);
        btnGetOtp.setOnClickListener(this);
        llMobileLoginForm = (LinearLayout) view.findViewById(R.id.llmobileloginform);

    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_get_otp;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_get_otp_btnGetOtp:
                Utils.hideSoftKeyBoard(getActivity(), view);
                txtInputMobile.setError("");
                llMobileLoginForm.requestFocus();

                String enteredMobile = Constants.COUNTRY_CODE + edtMobileNumber.getText().toString().trim();

                if (checkValidation(enteredMobile)) {

                    VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString(getString(R.string.bundle_mobile_number), enteredMobile);
                    bundle.putBoolean(getString(R.string.bundle_from_get_otp), true);
                    verifyOTPFragment.setArguments(bundle);
                    addFragment(R.id.activity_auth_flContainer, GetOtpFragment.this, verifyOTPFragment, false, false);
                }
                break;
        }
    }

    private boolean checkValidation(String enteredMobile) {
        if (enteredMobile.length() == 0) {
            txtInputMobile.setError(getString(R.string.err_emptymobile));
            return false;
        } else if (!Utils.isValidPhoneNumber(enteredMobile)) {
            txtInputMobile.setError(getString(R.string.err_invalidmobile));
            return false;
        }
        return true;
    }
}