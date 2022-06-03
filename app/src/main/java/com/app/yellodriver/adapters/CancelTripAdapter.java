package com.app.yellodriver.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.R;
import com.app.yellodriver.RideCancelTripMutation;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.fragment.DeclineTripFragment;
import com.app.yellodriver.fragment.HomeFragment;
import com.app.yellodriver.model.CancelTripModel;
import com.app.yellodriver.model.ModelCancelOptions.ModelCancelOptions;
import com.app.yellodriver.model.ModelCancelRide.RideCancelModel;
import com.app.yellodriver.model.ModelCancelRide.RideCancelViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.YLog;

import java.util.ArrayList;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class CancelTripAdapter extends RecyclerView.Adapter<CancelTripAdapter.ViewHolder> {
    DeclineTripFragment declineTripFragment;
    ArrayList<ModelCancelOptions.Data.YtSystemSetting> cancelTripReasonList;
    Context context;
    private CustomProgressDialog customProgressDialog;

    // RecyclerView recyclerView;
    public CancelTripAdapter(DeclineTripFragment declineFragment, Context con, ArrayList<ModelCancelOptions.Data.YtSystemSetting> reasonList) {
        cancelTripReasonList = reasonList;
        context = con;
        declineTripFragment = declineFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.canceltrip_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        final ModelCancelOptions.Data.YtSystemSetting.Content.Option model = cancelTripReasonList.get(0).getContent().getOptions().get(position);

        if (cancelTripReasonList.get(0).getContent().getOptions().get(position).isSelected()) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_selected));
        } else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_deselect));
        }

//        holder.tvCancelripReason.setText(cancelTripReasonList.get(position).getCanceltripReason());
        holder.tvCancelripReason.setText(cancelTripReasonList.get(0).getContent().getOptions().get(position).getCaption());
        holder.llreasonFullview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < cancelTripReasonList.get(0).getContent().getOptions().size(); i++) {
                    cancelTripReasonList.get(0).getContent().getOptions().get(i).setSelected(false);
                }

                cancelTripReasonList.get(0).getContent().getOptions().get(position).setSelected(true);
                notifyDataSetChanged();

                CancelTripDialog(cancelTripReasonList.get(0).getContent().getOptions().get(position).getCaption());
            }
        });
    }


    @Override
    public int getItemCount() {
        return cancelTripReasonList.get(0).getContent().getOptions().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvCancelripReason;
        public LinearLayout llreasonFullview;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvCancelripReason = (TextView) itemView.findViewById(R.id.tvCancelripReason);
            llreasonFullview = (LinearLayout) itemView.findViewById(R.id.relativeLayout);
            imageView = (ImageView) itemView.findViewById(R.id.cancelTrip_imgSelected);
        }
    }

    public void CancelTripDialog(String cancelReason) {
        Dialog cancelTripDialog = new Dialog(context, R.style.FullHeightDialog);
        cancelTripDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelTripDialog.setContentView(R.layout.cancel_trip_dialog);
        cancelTripDialog.setCancelable(true);
        Button btnCancelTripAccept = (Button) cancelTripDialog.findViewById(R.id.btnCancelTripAccept);

        btnCancelTripAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCancelReasonToServer(cancelReason);
                cancelTripDialog.dismiss();
            }
        });
        Button btnCancelTripDecline = (Button) cancelTripDialog.findViewById(R.id.btnCancelTripDecline);
        btnCancelTripDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTripDialog.dismiss();
            }
        });

        //now that the dialog is set up, it's time to show it
        cancelTripDialog.show();
    }

    private void sendCancelReasonToServer(String cancelReason) {

        customProgressDialog = new CustomProgressDialog(context);
        customProgressDialog.showDialog();

        RideCancelViewModel rideCancelViewModel = ViewModelProviders.of((HomeActivity) context).get(RideCancelViewModel.class);
        rideCancelViewModel.mutableApoloResponseCancelTrip.observe(((HomeActivity) context), observerApoloResponseCancelTrip);
//        String myReason = cancelReason;
        //Input<String> cancelReason = new Input<>(myReason, true);
        String curUserId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");
        String rideId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_CURRENT_RIDE_ID, "");
        Input<Object> cancelByUserId = new Input<>(curUserId, true);
        //Input<String> status = new Input<>("CANCELED", true);
//        Input<Object> rideId = new Input<>("3b38bc1a-a63a-4174-abde-c85b83829a07", true);

        rideCancelViewModel.setRideCancel(new RideCancelTripMutation(cancelReason, rideId));
    }

    Observer<RideCancelModel.Data.CancelRide> observerApoloResponseCancelTrip = listContent -> {

        if (null != customProgressDialog) {
            customProgressDialog.dismissDialog();
        }

        if (listContent != null) {
            String recordId = listContent.getId();
            YLog.e("Ride Cancelled Successfully", recordId + "");
            CancelTripSuccess();

            /*IMPORTANT TO REMOVE SHEET UPDATES*/
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_NAV_TO_RIDER, false);
            Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_IS_RIDE_ONGOING, false);
        } else {
            //emptyNotifyview.setVisibility(View.VISIBLE);
            //rvMyNotifs.setVisibility(View.GONE);
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };

    public void CancelTripSuccess() {
        Dialog rideCompletedDialog = new Dialog(context, R.style.FullHeightDialog);
        rideCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rideCompletedDialog.setContentView(R.layout.ride_success_dialog);

        TextView txtView = rideCompletedDialog.findViewById(R.id.success_dialog_txtMsg);
        txtView.setText(context.getString(R.string.trip_has_been_cancelled_successfully));

        rideCompletedDialog.setCancelable(true);

        rideCompletedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Intent intent = new Intent((HomeActivity) context, HomeFragment.class);
                //intent.putExtra("qrcodedata", qrCodeData);
                declineTripFragment.getTargetFragment().onActivityResult(declineTripFragment.getTargetRequestCode(), RESULT_OK, intent);
                declineTripFragment.getFragmentManager().popBackStack();
            }
        });

        //now that the dialog is set up, it's time to show it
        rideCompletedDialog.show();

        new Handler().postDelayed(() -> {
            try {
                if (null != rideCompletedDialog)
                    rideCompletedDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3000);
    }
}  