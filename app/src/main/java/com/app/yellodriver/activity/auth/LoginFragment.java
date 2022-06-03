package com.app.yellodriver.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.R;
import com.app.yellodriver.UserQuery;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.GreetingActivity;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.fragment.BaseFragment;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.model.ModelUser.ModelUser;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.JWTUtils;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;


public class LoginFragment extends BaseFragment {

    private TextInputEditText edtTxtInputPassword;
    private TextInputEditText edtTxtInputEmail;
    private TextView tvNavToReset;
    private Button btnLogin;
    private ImageView imgLogo;

    private FirebaseAuth mAuth;
    private String token;
    private String userType = "";

    private CustomProgressDialog customProgressDialog;
    TextInputLayout txtInputLoginEmail;
    TextInputLayout txtInputLoginPassword;
    private LinearLayout llLoginForm;
    private Button btnGetOtp;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initializeComponent(View view) {

        imgLogo = view.findViewById(R.id.ivbrndlogo);
        btnGetOtp = view.findViewById(R.id.btnGetOtp);
        btnGetOtp.setOnClickListener(this);
        txtInputLoginEmail = view.findViewById(R.id.tilLoginEmail);
        edtTxtInputEmail = view.findViewById(R.id.edtTxtInputEmail);
        edtTxtInputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputLoginEmail.setError("");
                }
            }
        });

        txtInputLoginPassword = view.findViewById(R.id.tilLoginPassword);
        edtTxtInputPassword = view.findViewById(R.id.edtTxtIputPassword);
        edtTxtInputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    txtInputLoginPassword.setError("");
                }
            }
        });

        tvNavToReset = view.findViewById(R.id.tvresetpass);
        btnLogin = view.findViewById(R.id.btngologin);

        llLoginForm = view.findViewById(R.id.llloginform);

        btnLogin.setOnClickListener(this);
        tvNavToReset.setOnClickListener(this);

        addAnimation();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        customProgressDialog = new CustomProgressDialog(getActivity());
    }

    private void addAnimation() {
        Animation slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);

        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imgLogo.startAnimation(slide_up);
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_login;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvresetpass:
                addFragment(R.id.activity_auth_flContainer, LoginFragment.this, new ReceiveEmailLinkFragment(), false, false);
                break;

            case R.id.btngologin:

                txtInputLoginEmail.setError("");
                txtInputLoginPassword.setError("");
                llLoginForm.requestFocus();

                String enteredEmailOrMobile = edtTxtInputEmail.getText().toString().trim();
                String enteredPassword = edtTxtInputPassword.getText().toString().trim();

                if (checkValidation(enteredEmailOrMobile, enteredPassword)) {

                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        checkUserCredential(enteredEmailOrMobile, enteredPassword, userType);
                    } else {
                        Utils.showNormalInternetDialog(getActivity());
                    }
                }
                break;

            case R.id.btnGetOtp:
                addFragment(R.id.activity_auth_flContainer, LoginFragment.this, new GetOtpFragment(), false, false);
                break;
        }
    }

    private boolean checkValidation(String enteredEmailOrMobile, String enteredPassword) {

        boolean validType = false;

        if (enteredEmailOrMobile.length() == 0) {
            txtInputLoginEmail.setError(getString(R.string.err_emptyemail));
            return false;
        } else if (Utils.isValidEmailId(enteredEmailOrMobile)) {
            userType = "email";
            validType = true;
        }/* else if (Utils.isValidPhoneNumber(enteredEmailOrMobile)) {
            userType = "mobile";
            validType = true;
        }*/
        if (!validType) {
            txtInputLoginEmail.setError(getString(R.string.err_invalidemail));
            return false;
        } else if (enteredPassword.length() == 0) {
            txtInputLoginPassword.setError(getString(R.string.err_emptypassword));
            return false;
        }
        return true;
    }

    private void checkUserCredential(String email, String password, String userType) {

        customProgressDialog.showDialog();

        if (userType.equals("email")) {
            loginWithEmail(email, password);
        } else {

        }
    }

    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            YLog.e(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Task<GetTokenResult> tokenResultTask = user.getIdToken(true);

                            tokenResultTask.addOnSuccessListener(tokenResult -> {

                                token = tokenResult.getToken();
                                YLog.e(TAG + "UserToken", token);
                                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_TOKEN, token);

                                try {
                                    JWTUtils jToken = new JWTUtils();
                                    jToken.decoded(token);
                                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_ID, jToken.getHasuraUserId());
                                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_NAME, jToken.getHasuraUserName());
                                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_EMAIL, jToken.getHasuraUserEmail());
                                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_PHONE, jToken.getHasuraUserPhone());
                                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_ROLE, jToken.getHasuraUserRole());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                UserQuery userQuery = new UserQuery(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID));

                                ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                                        .query(userQuery)
                                        .enqueue(new ApolloCall.Callback<UserQuery.Data>() {
                                            @Override
                                            public void onResponse(@NotNull Response<UserQuery.Data> response) {
                                                if(customProgressDialog!=null){
                                                    customProgressDialog.dismissDialog();
                                                }

                                                if (!response.hasErrors()) {
                                                    Operation.Data data = (Operation.Data) response.getData();
                                                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                                                    Gson gson = new Gson();
                                                    ModelUser modelUser = gson.fromJson(json, ModelUser.class);

                                                    ModelUser.Data.YtUserByPk content = modelUser.getData().getYtUserByPk();
                                                    if (content != null) {
                                                        if (content.getActive() && !content.getBlock()) {
                                                            Intent intent = new Intent(getActivity(), GreetingActivity.class);
                                                            //Intent intent = new Intent(getActivity(), MapboxNavigationView.class);
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        } else {
                                                            Utils.showCommonErrorDialog(YelloDriverApp.getInstance().getString(R.string.user_block));
                                                        }
                                                    }
                                                } else {
                                                    if(customProgressDialog!=null){
                                                        customProgressDialog.dismissDialog();
                                                    }
                                                    for (Object error : response.getErrors()) {
                                                        //Could not verify JWT: JWTExpired
                                                        YLog.e("Errors getProfileData:", ((Error) error).getMessage());
                                                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull ApolloException e) {
                                                if(customProgressDialog!=null){
                                                    customProgressDialog.dismissDialog();
                                                }
                                            }
                                        });
                            });
                        } else {

                            customProgressDialog.dismissDialog();
                            final KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE)
                                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                                    .setContentText(YelloDriverApp.getInstance().getString(R.string.err_incorrectemailpassword))
                                    .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                            pDialog.show();
                            pDialog.setCancelable(true);
                            pDialog.setCanceledOnTouchOutside(true);
                        }
                    }
                });
    }
}