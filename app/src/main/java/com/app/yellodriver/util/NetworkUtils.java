package com.app.yellodriver.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.app.yellodriver.R;

/**
 * Purpose of this Class is to check internet connection of phone and perform actions on user's input
 */
public class NetworkUtils {
    /**
     * Checks internet network connection.
     *
     * @param context    Activity context
     * @param message    if want to show connection message to user then true, false otherwise.
     * @param isSnackBar if want to show snackBar then true else shows alert dialog with buttons.
     * @param goSettings if want to go action setting for connection then true, otherwise only OK button.
     * @return if network connectivity exists or is in the process of being established, false otherwise.
     */
    public static boolean isOnline(final Activity context, boolean message, boolean isSnackBar, boolean goSettings) {
        if (context != null && !context.isFinishing()) {
            if (isNetworkAvailable(context)) {
                return true;
            }

            if (message) {
                if (isSnackBar) {
                    Utils.showSnackBar(context, context.findViewById(android.R.id.content), context.getString(R.string.alert_no_connection), context.getString(R.string.alert_some_error), context.getString(R.string.settings), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        }
                    });

                } else {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                    dialog.setTitle(context.getString(R.string.app_name));
                    dialog.setCancelable(false);
                    dialog.setMessage(context.getString(R.string.alert_no_connection));

                    if (goSettings) {
                        dialog.setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            }
                        });

                        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                    } else {
                        dialog.setNeutralButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                    }
                    dialog.show();
                }

                return false;
            }
        }
        return false;
    }

    /**
     * Checks the Network availability.
     *
     * @return true if internet available, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
