// Name:         Settings.java
// Author:         David Kincaid
// Company:        Mobile Software Design, LLC
// Platform:       Android
//
// Copyright (C) 2010, Mobile Software Design, LLC    
// All rights reserved.
//
// Description:     

package com.app.yellodriver.util;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationSettings {
    public static int restricupdate = 620;
    public static Context context;
    public static boolean pushNotification = false;
    public static boolean emailNotification = false;
    public static boolean smsNotification = false;
    public static boolean promotionNotification = false;
    public static boolean flashOnRideRequestNotification = false;
    public static boolean vibrationOnRideRequestNotification = false;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("YellowDrivr",
                Context.MODE_PRIVATE);
    }

    public static void read(Context context) {
        SharedPreferences preferences = getPreferences(context);
        restricupdate = preferences.getInt("sp_restricupdate", 620);
        pushNotification = preferences.getBoolean("sp_pushNotification", pushNotification);
        emailNotification = preferences.getBoolean("sp_emailNotification", emailNotification);
        promotionNotification = preferences.getBoolean("sp_promotionNotification", promotionNotification);
        smsNotification = preferences.getBoolean("sp_smsNotification", smsNotification);
        flashOnRideRequestNotification = preferences.getBoolean("sp_flashOnRideRequestNotification", flashOnRideRequestNotification);
        vibrationOnRideRequestNotification = preferences.getBoolean("sp_VibrationOnRideRequestNotification", vibrationOnRideRequestNotification);

    }

    // saves settings
    public static void write(Context context) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("restricupdate", restricupdate);
        editor.putBoolean("sp_pushNotification", pushNotification);
        editor.putBoolean("sp_promotionNotification", promotionNotification);
        editor.putBoolean("sp_emailNotification", emailNotification);
        editor.putBoolean("sp_smsNotification", smsNotification);
        editor.putBoolean("sp_flashOnRideRequestNotification", flashOnRideRequestNotification);
        editor.putBoolean("sp_VibrationOnRideRequestNotification", vibrationOnRideRequestNotification);
        editor.commit();
    }

}
