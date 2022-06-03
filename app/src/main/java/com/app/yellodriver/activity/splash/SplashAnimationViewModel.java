package com.app.yellodriver.activity.splash;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashAnimationViewModel extends ViewModel {

    /*
     * Handler is used to set some delay on this screen
     */
    protected Handler handler;
    protected MutableLiveData<Boolean> handlerMutableLiveData = new MutableLiveData<>();

    public void init(Context context) {
        executeHandler();
    }

    private void executeHandler() {
        handler = new Handler();
//        long INTERVAL = 1500;
        handler.postDelayed(runnable, 1200);
    }

    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handlerMutableLiveData.postValue(true);
        }
    };
}