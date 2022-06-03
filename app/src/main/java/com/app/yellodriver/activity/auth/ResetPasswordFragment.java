package com.app.yellodriver.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.GreetingActivity;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.fragment.BaseFragment;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ResetPasswordFragment extends BaseFragment {

    private Button btnUpdatePwd;
    private TextInputEditText edtTxtNewPassword, edtTxtConfPassword;
    private TextInputLayout txtInputNewPassword;
    private TextInputLayout txtInputConfirmPassword;
    private LinearLayout llResetPasswordForm;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initializeComponent(View view) {

        txtInputNewPassword = view.findViewById(R.id.tilnewpasswd);
        edtTxtNewPassword = view.findViewById(R.id.edtnewpass);
        edtTxtNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputNewPassword.setError("");
                }
            }
        });

        txtInputConfirmPassword = view.findViewById(R.id.tilconfpwd);
        edtTxtConfPassword = view.findViewById(R.id.edtconfpwd);
        edtTxtConfPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputConfirmPassword.setError("");
                }
            }
        });

        btnUpdatePwd = (Button) view.findViewById(R.id.btnupdtpwd);
        llResetPasswordForm = (LinearLayout) view.findViewById(R.id.llresetform);

        btnUpdatePwd.setOnClickListener(this);


    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_resetpassword;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnupdtpwd:

                txtInputNewPassword.setError("");
                txtInputConfirmPassword.setError("");
                llResetPasswordForm.requestFocus();

                String newPassword = edtTxtNewPassword.getText().toString().trim();
                String confirmPassword = edtTxtConfPassword.getText().toString().trim();

                if (checkValidation(newPassword, confirmPassword)) {

                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            YLog.d(TAG, "User password updated.");
                                            Utils.getUpdatedToken();
                                            updateMessageDialog();
                                        }else{
                                            try {
                                                String errorMsg = task.getException().getMessage();
                                                txtInputNewPassword.setError(errorMsg+"");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

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


    private void updateMessageDialog() {
        KAlertDialog successDlg = new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                .setContentText(getString(R.string.msg_success_passwordchange));
        successDlg.setCanceledOnTouchOutside(false);
        successDlg.setConfirmText(getResources().getString(R.string.lbl_ok));
        successDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                successDlg.dismiss();
//                startActivity(new Intent(getActivity(), HomeActivity.class));
                startActivity(new Intent(getActivity(), GreetingActivity.class));
                getActivity().finish();
            }
        });
        successDlg.show();
    }

    private boolean checkValidation(String newPassword, String confirmPassword) {
        if (newPassword.length() == 0) {
            txtInputNewPassword.setError(getString(R.string.err_emptynewpassword));
            return false;
        } else if (confirmPassword.length() == 0) {
            txtInputConfirmPassword.setError(getString(R.string.err_emptyconfirmpassword));
            return false;
        }
        if (!newPassword.contentEquals(confirmPassword)) {
            txtInputConfirmPassword.setError(getString(R.string.err_mismatchpassword));
            return false;
        }
        return true;
    }
}
