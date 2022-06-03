package com.app.yellodriver.activity.splash;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {

    /*
     * Handler is used to set some delay on this screen
     */
    protected Handler handler;
    protected MutableLiveData<Boolean> splashHandlerMutableLiveData = new MutableLiveData<>();

    public void init() {
        executeHandler();
    }

    private void executeHandler() {
        handler = new Handler();
//        long INTERVAL = 1500;
        handler.postDelayed(runnable, 2000);
    }

    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            splashHandlerMutableLiveData.postValue(true);
        }
    };
}