package com.app.yellodriver.activity.splash;

import android.content.Intent;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.yellodriver.BuildConfig;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.BaseActivity;

public class SplashActivity extends BaseActivity {

    private SplashViewModel splashViewModel;
    private TextView tvCode;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initializeComponents() {
        splashViewModel = ViewModelProviders.of((SplashActivity.this)).get(SplashViewModel.class);

        tvCode = findViewById(R.id.txtVersionCode);
        splashViewModel.init();

        splashViewModel.splashHandlerMutableLiveData.observe(this, (Observer) o -> goToNextActivity());

        tvCode.setText("Version Code: " + BuildConfig.VERSION_NAME);
    }

    public void goToNextActivity() {

        final Intent homeIntent = new Intent(SplashActivity.this, SplashAnimationActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashViewModel.handler != null) {
            splashViewModel.handler.removeCallbacks(splashViewModel.runnable);
            finish();
        }
    }
}