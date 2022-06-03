package com.app.yellodriver.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.yellodriver.R;
import com.app.yellodriver.util.Utils;


/**
 * Purpose of this Class is to display different dialog in application.
 */
public class DisplayDialog {


    /**
     * A custom dialog object for agent Subscribed
     */
    private Dialog commonDialog;


    /**
     * Displays alert dialog to show common messages.
     *
     * @param title   Title of the Dialog : Application's Name
     * @param message Message to be shown in the Dialog displayed
     * @param context Context of the Application, where the Dialog needs to be displayed
     */
    public static void displayDialog(final Context context, final String title, final String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.StyleCommonDialog).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        if (!((Activity) context).isFinishing()) {
            alertDialog.show();
        }
    }


    /**
     * Custom view as app theme common dialog
     *
     * @param context        Activity context
     * @param title          Title text
     * @param msg            Message text
     * @param strPositiveBtn Positive button text
     * @param strNegativeBtn Negative button text
     * @param isTitle        Show title if true otherwise not
     * @param isNegativeBtn  Show negative if true otherwise not
     * @param listener       Listener for positive and negative buttons of dialog
     */
    public void showCommonDialog(final Context context, final String title, String msg, final String strPositiveBtn,
                                 final String strNegativeBtn, final boolean isTitle, final boolean isNegativeBtn,
                                 final DialogButtonListener listener) {
        if (context != null && !((Activity) context).isFinishing()) {
            if (TextUtils.isEmpty(msg)) {
                msg = context.getString(R.string.alert_some_error);
            }

            commonDialog = new Dialog(context, R.style.StyleCommonDialog);
            commonDialog.setContentView(R.layout.dialog_custom_common);

            if (commonDialog.getWindow() != null) {
                final WindowManager.LayoutParams wlmp = commonDialog.getWindow().getAttributes();
                wlmp.gravity = Gravity.CENTER;
                wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                commonDialog.getWindow().setAttributes(wlmp);
                commonDialog.setCancelable(true);
                commonDialog.setCanceledOnTouchOutside(false);
                commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //Set the dialog to immersive
                //                mFeedOptionDialog.getWindow().getDecorView().setSystemUiVisibility(
                //                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

            final TextView tvTitle = commonDialog.findViewById(R.id.dialog_custom_common_tvTitle);
            final TextView tvMsg = commonDialog.findViewById(R.id.dialog_custom_common_tvMsg);
            final TextView btnPositive = commonDialog.findViewById(R.id.dialog_custom_common_btnPositive);
            final TextView btnNegative = commonDialog.findViewById(R.id.dialog_custom_common_btnNegative);

            tvTitle.setText(title.trim());
            tvMsg.setText(Utils.setFormattedHtmlString(msg.trim()));

            if (isTitle) {
                tvTitle.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
            }

            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissCommonnDialog();
                    listener.onPositiveButtonClicked();
                }
            });

            if (!TextUtils.isEmpty(strPositiveBtn.trim())) {
                btnPositive.setText(strPositiveBtn.trim());
            }

            if (isNegativeBtn) {
                btnNegative.setVisibility(View.VISIBLE);

                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissCommonnDialog();
                        listener.onNegativeButtonClicked();
                    }
                });
                if (!TextUtils.isEmpty(strNegativeBtn.trim())) {
                    btnNegative.setText(strNegativeBtn.trim());
                }
            } else {
                btnNegative.setVisibility(View.GONE);
            }

            if (commonDialog != null && !commonDialog.isShowing()) {
                commonDialog.show();
            }
        }
    }

    /**
     * Dismiss agent subscribed dialog if it is showing
     */
    private void dismissCommonnDialog() {
        if (commonDialog != null && commonDialog.isShowing()) {
            commonDialog.dismiss();
            commonDialog = null;
        }
    }

    public interface DialogButtonListener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }

//    /**
//     * A Progress Dialog object
//     */
//    private Dialog progressDialog;
//    /**
//     * Dismiss Progress dialog if it is showing
//     */
//    public void dismissProgressDialog() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//    }


    /**
     * Displays the progress dialog on activity.
     * This method will generate progress dialog and displays it on screen if its not currently showing,
     * If the progressbar dialog already been showing than it will not generate new dialog and return old generated dialog.
     *
     * @param mContext requires to create ProgressDialog in Application
     * @return Returns the object of Progress dialog that currently generated or previously generated.
     */
//    public Dialog showProgressDialog(final Context mContext) {
//        if (mContext != null) {
//            if (progressDialog == null) {
//                progressDialog = new Dialog(mContext);
//                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            }
//
//            if (progressDialog.getWindow() != null) {
//                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            }
//
//            progressDialog.setContentView(R.layout.progress_layout);
//            progressDialog.setCancelable(false);
//            progressDialog.setCanceledOnTouchOutside(false);
//            final ImageView iv = progressDialog.findViewById(R.id.image);
//            Glide.with(mContext).asBitmap().load(R.drawable.icon_check).into(iv);
//            Glide.with(mContext).asGif().load(R.drawable.icon_check).into(iv);
//
//            if (progressDialog != null && !progressDialog.isShowing()) {
//                progressDialog.show();
//            }
//        }
//        return progressDialog;
//    }
}
