package com.app.yellodriver.activity.auth;

import com.app.yellodriver.R;
import com.app.yellodriver.activity.BaseActivity;

public class AuthActivity extends BaseActivity {

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initializeComponents();
//    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_auth;
    }

    @Override
    protected void initializeComponents() {
        replaceFragment(R.id.activity_auth_flContainer, getSupportFragmentManager(), new LoginFragment(), false, false);
    }
}