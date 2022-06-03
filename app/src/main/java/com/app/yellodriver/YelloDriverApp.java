package com.app.yellodriver;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;

import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;

import io.paperdb.Paper;

/**
 * <p>Application class for define things which use till application running.</p>
 */
public class YelloDriverApp extends Application {

    private static YelloDriverApp yelloDriverApp;
    static MyLifecycleHandler myLifeCycleHadler;
    @Override
    public void onCreate() {
        super.onCreate();
        yelloDriverApp = this;
        Paper.init(this);
        myLifeCycleHadler = new MyLifecycleHandler();
        registerActivityLifecycleCallbacks(myLifeCycleHadler);

        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        float density = Resources.getSystem().getDisplayMetrics().density;
        YLog.e("Width:Height:density",width+":"+height+":"+density);
        Utils.getDeviceId();
    }

    public static YelloDriverApp getInstance() {
        return yelloDriverApp;
    }

    public static Activity getCurrentActivity() {
        return myLifeCycleHadler.getCurrentActivity();
    }
}