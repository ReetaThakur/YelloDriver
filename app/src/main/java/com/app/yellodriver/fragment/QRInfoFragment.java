package com.app.yellodriver.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.ScanQRcodeActivity;
import com.app.yellodriver.model.StoredRiderInfo;
import com.app.yellodriver.model.StoredRoute;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.imageloader.ImageLoader;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class QRInfoFragment extends BaseFragment {
    int scanQRcode = 25;
    //Button btnMarkasCompleteContinue;

    private LinearLayout llContinueCancel;
    private Button btnExtendPass;
    private Button btnContinue;
    //private Button btnCancel;
    private ImageView ivqruserimg;
    private TextView tvRideStartAddr;
    private TextView tvRideEndAddr;
    private TextView tvUserName;
    private TextView tvUserRating;
    private TextView tvArrivalDistanceTime;

    @Override
    protected void initializeComponent(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        llContinueCancel = view.findViewById(R.id.llContinueCancel);
        btnExtendPass = view.findViewById(R.id.btnExtendPass);
        btnContinue = view.findViewById(R.id.fragment_qr_code_btnContinue);
        //btnCancel = view.findViewById(R.id.fragment_qr_code_btnCancel);
        tvRideStartAddr = view.findViewById(R.id.tvstartlocation);
        tvRideEndAddr = view.findViewById(R.id.tvendlocation);
        ivqruserimg = view.findViewById(R.id.ivqruserimg);

        tvUserName = view.findViewById(R.id.tvqrusername);
        tvUserRating = view.findViewById(R.id.tvqruserrating);
        tvArrivalDistanceTime = view.findViewById(R.id.tvqrdistancentime);


        imgBack.setOnClickListener(view14 -> getFragmentManager().popBackStack());

        StoredRoute currentRoute = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_ONGOING_ROUTE, null);
        tvRideStartAddr.setText(currentRoute.getStartAddrs());
        tvRideEndAddr.setText(currentRoute.getEndAddrs());
        String arrivalDistNTime = Utils.getArrivalByDistanceDuration(currentRoute.getDistanceMeters(), currentRoute.getDurationSeconds());
        tvArrivalDistanceTime.setText(arrivalDistNTime);

        StoredRiderInfo currentRider = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_RIDER_INFO, null);
        tvUserName.setText(currentRider.getRiderName());
//        tvUserRating.setText(currentRider.getRiderRating()+"");

        if (currentRider.getRiderImage() != null) {
            ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), ivqruserimg, currentRider.getRiderImage(), R.drawable.ic_place_holder_user);
        }

        if (!currentRider.getRiderRating().equals("0")) {
            tvUserRating.setVisibility(View.VISIBLE);
            tvUserRating.setText(currentRider.getRiderRating());
        } else {
//            tvUserRating.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvUserRating.setVisibility(View.GONE);
        }

        view.findViewById(R.id.imgQRcode).setOnClickListener(view12 -> {
            if (isCameraPermissionGranted()) {
                Intent in = new Intent(getActivity(), ScanQRcodeActivity.class);
                startActivityForResult(in, scanQRcode);
            }
        });

        btnContinue.setOnClickListener(view1 -> {
//            llContinueCancel.setVisibility(View.GONE);
//            btnExtendPass.setVisibility(View.VISIBLE);

            if (isCameraPermissionGranted()) {
                Intent in = new Intent(getActivity(), ScanQRcodeActivity.class);
                startActivityForResult(in, scanQRcode);
            }
        });

        btnExtendPass.setOnClickListener(view13 -> {
            renewPassDialog();
        });

        /*btnMarkasCompleteContinue=view.findViewById(R.id.btnMarkasCompleteContinue);
        btnMarkasCompleteContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_scaning_qr_code;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == scanQRcode && resultCode == RESULT_OK) {
                String qrCodeData = data.getStringExtra("qrCodeData");
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(qrCodeData)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/


                Intent intent = new Intent(getActivity(), HomeFragment.class);
                intent.putExtra("qrcodedata", qrCodeData);
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                getFragmentManager().popBackStack();

            }
        } else {
            //Temporary for demo
            Intent intent = new Intent(getActivity(), HomeFragment.class);
            intent.putExtra("qrcodedata", "{\"confirmation_code\":\"RC-6\",\"ride_id\":\"89d11d9b-7cc6-4eb3-84c3-8087f794e210\"}");
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            getFragmentManager().popBackStack();

        }
    }

    public boolean isCameraPermissionGranted() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 200);
            return false;
        } else {
            return true;

        }

    }

    public void renewPassDialog() {
        Dialog renewPassDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        renewPassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        renewPassDialog.setContentView(R.layout.renew_pass_dialog);

        renewPassDialog.setCancelable(true);

        //now that the dialog is set up, it's time to show it
        renewPassDialog.show();
    }
}
