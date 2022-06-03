package com.app.yellodriver.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.app.yellodriver.R;
import com.app.yellodriver.activity.BaseActivity;
import com.app.yellodriver.activity.auth.AuthActivity;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.JWTUtils;
import com.app.yellodriver.util.YLog;

import io.paperdb.Paper;

/**
 * Simple startup activity showing static image.
 */
public class SplashAnimationActivity extends BaseActivity {

    private SplashAnimationViewModel splashAnimationViewModel = new SplashAnimationViewModel();
    private LinearLayout llCar;
    private LinearLayout llWelcome;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_splash_animation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            splashAnimationViewModel.init(this);
        }

        if (isDuplicateInstance()) {//return if this is duplicate instance of same category and instance
            return;
        }

        splashAnimationViewModel.handlerMutableLiveData.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                goToNextActivity();
            }
        });
    }

    @Override
    protected void initializeComponents() {
        llCar = findViewById(R.id.splash_llCar);
        llWelcome = findViewById(R.id.splash_llWelcome);

        new Handler().postDelayed(() -> setUpAnimation(), 1000);
    }

    private void setUpAnimation() {

//        Animation bottomUp = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_up);
//        Animation bottomUpWelcome = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_up_welcome);
////        ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.splash_llCar);
//        llCar.startAnimation(bottomUp);
//        llWelcome.startAnimation(bottomUpWelcome);
//        llCar.setVisibility(View.VISIBLE);
//        llWelcome.setVisibility(View.VISIBLE);

        Animation bottom_up = AnimationUtils.loadAnimation(this, R.anim.bottom_up);

        bottom_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llCar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        llCar.startAnimation(bottom_up);

        Animation bottom_up_welcome = AnimationUtils.loadAnimation(this, R.anim.bottom_up_welcome);

        bottom_up_welcome.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llWelcome.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        llWelcome.startAnimation(bottom_up_welcome);
    }

    /**
     * This method will prevent multiple instances of an activity when it is launched with different intents
     */
    private boolean isDuplicateInstance() {
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                finish();
                return true;
            }
        }
        return false;
    }

    public void goToNextActivity() {

        String token = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_TOKEN, "");
        if (token != null && !token.isEmpty()) {
            YLog.d("UserToken",""+token);
            try {
                JWTUtils jToken = new JWTUtils();
                jToken.decoded(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent navIntent = new Intent(SplashAnimationActivity.this, HomeActivity.class);
            startActivity(navIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            YLog.e("UserToken","No Login");
            Intent navIntent = new Intent(SplashAnimationActivity.this, AuthActivity.class);
            startActivity(navIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashAnimationViewModel.handler != null) {
            splashAnimationViewModel.handler.removeCallbacks(splashAnimationViewModel.runnable);
            finish();
        }
    }
}