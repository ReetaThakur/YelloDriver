package com.app.yellodriver.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Subscription;
import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.dialog.DisplayDialog;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Performs common utility operations.
 */
public class Utils {

    /**
     * checks the GPS is enable or not
     *
     * @param activity    object required for get SystemService
     * @param showMessage if true will show enable GPS alert with got to settings option otherwise check silently
     * @return true if location enabled otherwise false
     */
    public static boolean checkLocationAccess(final Activity activity, boolean showMessage) {
        if (activity != null && !activity.isFinishing()) {
            final LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isNetworkEnabled) {
                return true;
            } else if (isGpsEnabled) {
                return true;
            }

            if (showMessage) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                dialogBuilder.setTitle(activity.getString(R.string.app_name));
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage(activity.getString(R.string.alert_check_gps));

                dialogBuilder.setPositiveButton(activity.getString(R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

                dialogBuilder.setNegativeButton(activity.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface mDialog, int id) {
                        mDialog.dismiss();
                    }
                });
                dialogBuilder.show();

                return false;
            }
        }
        return false;
    }


    /**
     * Validates the Email Id
     *
     * @param emailId email id to be verified
     * @return true valid email id, false invalid emailid
     */
    public static boolean isValidEmailId(final String emailId) {
        return !TextUtils.isEmpty(emailId) && Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
//        if (target.length()!=10) {
//            return false;
//        } else if(!target.toString().contains("+1")){
        if (!target.toString().contains("+1")) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    /**
     * Validates the Url
     *
     * @param url email id to be verified
     * @return true valid email id, false invalid emailid
     */
    public static boolean isValidUrl(final String url) {
        return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }


    /**
     * Hide the soft keyboard from screen for edit text only
     *
     * @param context context of current visible activity
     * @param view    clicked view
     */
    public static void hideSoftKeyBoard(final Context context, final View view) {
        try {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Show soft keyboard on auto focus of edittext
     *
     * @param context context of current visible activity
     * @param view    focused view
     */
    public static void showKeyboard(final Context context, final View view) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Returns android secure id.
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(final Context context) {
        try {
            String deviceId = null;
            // 1 android ID - unreliable
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (deviceId == null) {
                // 2 compute DEVICE ID
                deviceId = "35" + // we make this look like a valid IMEI
                        Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                        + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13
            }
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * Call snack bar which will disapper in 3-5 seconds
     *
     * @return Snack bar
     */
    public static Snackbar showSnackBar(final Context context, final View view, final String message, final String defaultMessage, final String actionLabel, final View.OnClickListener clickListener) {
        if (view == null) {
            return null;
        }

        final Snackbar snackbar = Snackbar.make(view, TextUtils.isEmpty(message) ? defaultMessage : message, Snackbar.LENGTH_LONG);
        final View snackBarView = snackbar.getView();
//        snackBarView.setBackgroundColor(ContextCompat.getColor(context, isError ? android.R.color.holo_red_dark : android.R.color.holo_green_dark));
        if (!TextUtils.isEmpty(actionLabel)) {
            snackbar.setAction(actionLabel, clickListener);
        }
        final TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
        return snackbar;
    }

    /**
     * Returns the device's DisplayMetrics
     */
    public static DisplayMetrics getDeviceMetrics(final Activity activity) {
        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * to get file uri as per OS version check for pre Marshmallow uri also
     *
     * @param context context of current visible activity
     * @param file    file which Uri whants
     * @return file Uri
     */
    public static Uri getUri(final Context context, final File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (context != null) {
                return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
            } else {
                return Uri.fromFile(file);
            }
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * Launch web page into third party app.
     */
    public static void openWebPage(final Activity activity, final String url) {
        if (activity != null && !TextUtils.isEmpty(url)) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        }
    }

    /**
     * set formatted Html text
     *
     * @param formattedString formatted string
     * @return spanned text
     */
    public static Spanned setFormattedHtmlString(final String formattedString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(formattedString, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(formattedString);
        }
    }


    /**
     * Performs the share intent.
     */
    public static void shareItem(final Activity activity, final String url, final String chooserTitle) {
        if (!TextUtils.isEmpty(url) && activity != null) {
            final Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, chooserTitle));
        }
    }


    /**
     * Cancels the running async task.
     */
    public static boolean cancelAsyncTask(final AsyncTask asyncTask) {
        if (asyncTask != null && asyncTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            asyncTask.cancel(true);
        }
        return false;
    }

    /**
     * checks the device has camera or not
     *
     * @param mActivity object required for get package manager
     * @return true if camera is available otherwise false
     */
    public static boolean isCamera(Activity mActivity) {
        if (mActivity != null && !mActivity.isFinishing()) {
            PackageManager packageManager = mActivity.getPackageManager();

            return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        } else {
            return false;
        }
    }

    public static float spToPx(int sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    public static void setDrawableColor(Drawable drawable, int color) {
        drawable.mutate();
        if (drawable instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) drawable;
            shapeDrawable.getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            colorDrawable.setColor(color);
        }

    }

    public static void setDrawableStroke(Drawable drawable, int color) {
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.mutate();
//            gradientDrawable.setStroke(4, color);
            gradientDrawable.setStroke(1, color);
        }
    }

    public static String[] getLatLongFromString(String locationString) {

        String location = locationString.replace("(", "");
        String locationTemp = location.replace(")", "");
        String finalLocation = locationTemp.trim();

        return finalLocation.split(",");
    }

    public static String setLatLongFromLocation(Location location) {
        return "(" + location.getLongitude() + "," + location.getLatitude() + ")";
    }

    public static boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) YelloDriverApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    public static String getArrivalByDistanceDuration(double metres, double seconds) {
        double totalDistance = metres / 1000;

        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, (int) Math.round(seconds));

        Date date = calendar.getTime();
        String time = seconds + "";
        try {
            time = timeFormat.format(date);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return String.format("%.2f", totalDistance) + "KM Arrival at " + time;
    }

    public static void showCommonErrorDialog(String errorMessage) {

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                /*KAlertDialog successDlg = new KAlertDialog(YelloDriverApp.getCurrentActivity(), KAlertDialog.ERROR_TYPE)
                        .setContentText(errorMessage+"");
                successDlg.setCanceledOnTouchOutside(true);
                successDlg.show();*/

                final KAlertDialog pDialog = new KAlertDialog(YelloDriverApp.getCurrentActivity(), KAlertDialog.ERROR_TYPE)
                        .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                        .setContentText(errorMessage)
                        .setConfirmText(YelloDriverApp.getInstance().getString(R.string.lbl_ok));
                pDialog.show();
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);

//                DisplayDialog dlg = new DisplayDialog();
//                dlg.displayDialog(YelloDriverApp.getCurrentActivity(), YelloDriverApp.getInstance().getResources().getString(R.string.app_name), errorMessage);
            }
        });


    }

    //    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void getUpdatedToken() {

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Task<GetTokenResult> tokenResultTask = firebaseUser.getIdToken(true);

        tokenResultTask.addOnSuccessListener(getTokenResult -> {
            String token = tokenResultTask.getResult().getToken();
            YLog.e("Token", token);
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_TOKEN, token);
        });
    }

    public static void getDeviceId() {
        String deviceId = android.provider.Settings.Secure.getString(
                YelloDriverApp.getInstance().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        YLog.e("DeviceId", deviceId);
        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_DEVICE_ID, deviceId);

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showInternetDialog(Activity activity, Query query, Mutation mutation, ApolloCall.Callback callback) {

        if (CustomProgressDialog.getDialogObject() != null && CustomProgressDialog.getDialogObject().isShowing()) {
            CustomProgressDialog.dismissDialog();
        }

        if (activity != null) {

            final Dialog dialogView = new Dialog(activity, R.style.styleFullScreenDialog);
            dialogView.setContentView(R.layout.no_internet_connection);
            dialogView.setCancelable(false);

            final Button btnRetry = dialogView.findViewById(R.id.no_internet_connection_btnTryAgain);

            if (!activity.isFinishing()) {
                dialogView.show();
            }
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NetworkUtils.isNetworkAvailable(activity)) {

                        if (query != null) {
                            ApoloCall.getApolloClient(activity)
                                    .query(query)
                                    .clone()
                                    .enqueue(callback);
                        } else if (mutation != null) {
                            ApoloCall.getApolloClient(activity)
                                    .mutate(mutation)
                                    .clone()
                                    .enqueue(callback);
                        }

                        CustomProgressDialog customProgressDialog = new CustomProgressDialog(YelloDriverApp.getCurrentActivity());
                        customProgressDialog.showDialog();

                        dialogView.dismiss();
                    }
                }
            });

            dialogView.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        btnRetry.performClick();
                    }
                    return true;
                }
            });
        }
    }

    public static void showNormalInternetDialog(Activity activity) {

        if (CustomProgressDialog.getDialogObject() != null && CustomProgressDialog.getDialogObject().isShowing()) {
            CustomProgressDialog.dismissDialog();
        }

        if (activity != null) {

            final Dialog dialogView = new Dialog(activity, R.style.styleFullScreenDialog);
            dialogView.setContentView(R.layout.no_internet_connection);
            dialogView.setCancelable(false);

            final Button btnRetry = dialogView.findViewById(R.id.no_internet_connection_btnTryAgain);

            if (!activity.isFinishing()) {
                dialogView.show();
            }
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NetworkUtils.isNetworkAvailable(activity)) {
                        dialogView.dismiss();
                    }
                }
            });

            dialogView.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        btnRetry.performClick();
                    }
                    return true;
                }
            });
        }
    }


    /**
     * Pass your date format and no of days for minus from current
     * If you want to get previous date then pass days with minus sign
     * else you can pass as it is for next date
     * @param dateFormat
     * @param days
     * @return Calculated Date
     */
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    /***
     *
     * @param mString this will setup to your textView
     * @param colorId  text will fill with this color.
     * @return string with color, it will append to textView.
     */
    public static Spannable getColoredString(String mString, int colorId) {
        Spannable spannable = new SpannableString(mString);
        spannable.setSpan(new ForegroundColorSpan(colorId), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        YLog.e("Colored String",spannable.toString());
        return spannable;
    }

}