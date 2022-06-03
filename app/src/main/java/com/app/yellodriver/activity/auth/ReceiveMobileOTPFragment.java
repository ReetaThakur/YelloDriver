package com.app.yellodriver.activity.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.yellodriver.R;
import com.app.yellodriver.fragment.BaseFragment;
import com.app.yellodriver.fragment.PolicyFragment;
import com.app.yellodriver.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReceiveMobileOTPFragment extends BaseFragment {
    TextInputEditText edtTxtInputMobile;
    private TextView tvNavToEmail;
    private Button btnNavToOTPView;
    private TextInputLayout txtInputResetMobile;
    private LinearLayout llResetByMobileForm;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initializeComponent(View view) {

        txtInputResetMobile = view.findViewById(R.id.tilresetmobile);
        edtTxtInputMobile = view.findViewById(R.id.edtTxtInputMobileNo);
        edtTxtInputMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputResetMobile.setError("");
                }
            }
        });

        tvNavToEmail = (TextView) view.findViewById(R.id.tvnavtoemail);
        tvNavToEmail.setOnClickListener(this);

        btnNavToOTPView = (Button) view.findViewById(R.id.btnnavtoverifyotp);
        btnNavToOTPView.setOnClickListener(this);

        llResetByMobileForm = (LinearLayout) view.findViewById(R.id.llrestbymob);
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_resetbymobile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvnavtoemail:
                addFragment(R.id.activity_auth_flContainer, ReceiveMobileOTPFragment.this, new ReceiveEmailLinkFragment(), false, false);
                break;

            case R.id.btnnavtoverifyotp:

                txtInputResetMobile.setError("");
                llResetByMobileForm.requestFocus();

                String enteredMobile = edtTxtInputMobile.getText().toString().trim();

                if (checkValidation(enteredMobile)) {

                    VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString(getString(R.string.bundle_mobile_number), enteredMobile);
                    verifyOTPFragment.setArguments(bundle);

                    addFragment(R.id.activity_auth_flContainer, ReceiveMobileOTPFragment.this, verifyOTPFragment, false, false);
                }
                break;
        }
    }

    private boolean checkValidation(String enteredMobile) {
        if (enteredMobile.length() == 0) {
            txtInputResetMobile.setError(getString(R.string.err_emptymobile));
            return false;
        } else if (!Utils.isValidPhoneNumber(enteredMobile)) {
            txtInputResetMobile.setError(getString(R.string.err_invalidmobile));
            return false;
        }
        return true;
    }
}
