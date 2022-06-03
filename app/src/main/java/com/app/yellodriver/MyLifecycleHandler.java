package com.app.yellodriver;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.yellodriver.util.YLog;

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = MyLifecycleHandler.class.getSimpleName();
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.

    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    private static AppCompatActivity activity;
    private static int numRunningActivities = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        this.activity = (AppCompatActivity) activity;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;

        this.activity = (AppCompatActivity) activity;
        YLog.e(TAG, "Activity Name Resumed" + activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
        numRunningActivities++;

        this.activity = (AppCompatActivity) activity;
        YLog.e(TAG, "Activity Name Started" + activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        numRunningActivities--;
    }

    public static AppCompatActivity getCurrentActivity() {
        if (activity != null) {
            return activity;
        } else {
            return null;
        }
    }

    public static boolean isAppForeground() {
        if (numRunningActivities == 0) {
            return false;
        } else {
            return true;
        }
    }
}