package com.app.yellodriver.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.GreetingActivity;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.customview.OtpView;
import com.app.yellodriver.fragment.BaseFragment;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.listener.OnOtpCompletionListener;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class VerifyOTPFragment extends BaseFragment implements View.OnClickListener {

    private Button tvNavToOTP;
    private OtpView otpView;
    private Button btnResendOTP;

    private CustomProgressDialog progressBar;

    private String enteredOTP = "";
    private String mobileNumber;
    private boolean fromGetOtp = false;

    private String verificationId;
    private FirebaseAuth mAuth;
    //Add this on top where other variables are declared
    PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore instance state
        // put this code after starting phone number verification
    }

    @Override
    protected void initializeComponent(View view) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            mobileNumber = bundle.getString(getString(R.string.bundle_mobile_number));
            fromGetOtp = bundle.getBoolean(getString(R.string.bundle_from_get_otp));
        }

        mAuth = FirebaseAuth.getInstance();

        otpView = view.findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                // do Stuff
                Log.d("onOtpCompleted=>", otp);
                enteredOTP = otp;
                verifyCode(otp);
            }
        });

        tvNavToOTP = (Button) view.findViewById(R.id.btndoverify);
        btnResendOTP = (Button) view.findViewById(R.id.btndoresendotp);

        btnResendOTP.setOnClickListener(this);
        tvNavToOTP.setOnClickListener(this);

        progressBar = new CustomProgressDialog(getActivity());
        sendVerificationCode(mobileNumber);
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_otpverify;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btndoverify:

                if (enteredOTP.length() == 0) {
                    final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                            .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                            .setContentText(getString(R.string.err_emptyotp))
                            .setConfirmText(getResources().getString(R.string.lbl_ok));
                    pDialog.show();
                    pDialog.setCancelable(true);
                    pDialog.setCanceledOnTouchOutside(true);
                    return;
                } else {
                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        verifyCode(enteredOTP);
                    } else {
                        Utils.showNormalInternetDialog(getActivity());
                    }
                }
                break;

            case R.id.btndoresendotp:
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    progressBar.showDialog();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            mobileNumber,        // Phone number to verify
                            30,               // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                            mCallBack,         // OnVerificationStateChangedCallbacks
                            mResendToken);             // Force Resending Token from callbacks
                } else {
                    Utils.showNormalInternetDialog(getActivity());
                }
                break;
        }
    }

    private void sendVerificationCode(String number) {
        progressBar.showDialog();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//
                        progressBar.dismissDialog();
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        Task<GetTokenResult> tokenResultTask = firebaseUser.getIdToken(true);
//
                        tokenResultTask.addOnSuccessListener(getTokenResult -> {
                            String token = tokenResultTask.getResult().getToken();
                            YLog.e("Token", token);

                            if (fromGetOtp) {
//                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                startActivity(new Intent(getActivity(), GreetingActivity.class));
                                getActivity().finish();
                            } else {
                                addFragment(R.id.activity_auth_flContainer, VerifyOTPFragment.this, new ResetPasswordFragment(), false, false);
                            }
                        });

                    } else {

                        progressBar.dismissDialog();
                        YLog.e("Error **", task.getException().getMessage());
                        final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                                .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                                .setContentText(getString(R.string.err_incorrect_otp))
                                .setConfirmText(getResources().getString(R.string.lbl_ok));
                        pDialog.show();
                        pDialog.setCancelable(true);
                        pDialog.setCanceledOnTouchOutside(true);
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            mResendToken = forceResendingToken; //Add this line to save the resend token
            progressBar.dismissDialog();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            YLog.e("Error **", e.getMessage());
            progressBar.dismissDialog();
            final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                    .setContentText(getString(R.string.err_incorrect_otp))
                    .setConfirmText(getResources().getString(R.string.lbl_ok));
            pDialog.show();
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);
        }
    };

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception exception) {

            progressBar.dismissDialog();
            YLog.e("Exception", exception.toString());
            final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                    .setContentText(getString(R.string.err_incorrect_otp))
                    .setConfirmText(getResources().getString(R.string.lbl_ok));
            pDialog.show();
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);
        }
    }
}