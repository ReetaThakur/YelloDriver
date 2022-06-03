package com.app.yellodriver.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.yellodriver.R;
import com.app.yellodriver.fragment.BaseFragment;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class ReceiveEmailLinkFragment extends BaseFragment {
    TextInputEditText edtTxtInputEmail;
    private TextView tvNavToOTP;
    private Button btnSendEmailLink;
    private TextInputLayout txtInputResetEmail;
    private LinearLayout llResetByEmailForm;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initializeComponent(View view) {

        txtInputResetEmail = view.findViewById(R.id.tilresetemail);
        edtTxtInputEmail = view.findViewById(R.id.edtemailforlink);
        edtTxtInputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputResetEmail.setError("");
                }
            }
        });

        tvNavToOTP = (TextView) view.findViewById(R.id.tvnavtootp);
        tvNavToOTP.setOnClickListener(this);

        btnSendEmailLink = (Button) view.findViewById(R.id.btnsendemail);
        btnSendEmailLink.setOnClickListener(this);

        llResetByEmailForm = view.findViewById(R.id.llrestbyemlform);

    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_resetbyemail;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvnavtootp:
                addFragment(R.id.activity_auth_flContainer, ReceiveEmailLinkFragment.this, new ReceiveMobileOTPFragment(), false, false);
                break;
            case R.id.btnsendemail:

                txtInputResetEmail.setError("");
                llResetByEmailForm.requestFocus();

                String enteredEmail = edtTxtInputEmail.getText().toString().trim();

                if (checkValidation(enteredEmail)) {

                    if (NetworkUtils.isNetworkAvailable(getActivity())) {

                        CustomProgressDialog customProgressDialog = new CustomProgressDialog(getActivity());
                        customProgressDialog.showDialog();

                        FirebaseAuth.getInstance().sendPasswordResetEmail(enteredEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            YLog.e(TAG, "Email sent.");

                                            customProgressDialog.dismissDialog();
                                            KAlertDialog successDlg = new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                                                    // .setTitleText("Good job!")
                                                    .setContentText(getString(R.string.msg_success_emailsent));
                                            successDlg.setCanceledOnTouchOutside(true);
                                            successDlg.show();
                                        } else {
                                            customProgressDialog.dismissDialog();

                                            KAlertDialog successDlg = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                                                    // .setTitleText("Good job!")
                                                    .setContentText(getString(R.string.alert_some_error));
                                            successDlg.setCanceledOnTouchOutside(true);
                                            successDlg.show();
                                        }
                                    }
                                });
                    } else {
                        Utils.showNormalInternetDialog(getActivity());
                    }
                }

                break;
        }
    }

    public boolean checkValidation(String enteredEmail) {
        if (enteredEmail.length() == 0) {
            txtInputResetEmail.setError(getString(R.string.err_emptyemail));
            return false;
        } else if (!Utils.isValidEmailId(enteredEmail)) {
            txtInputResetEmail.setError(getString(R.string.err_invalidemail));
            return false;
        }

        return true;
    }
}
