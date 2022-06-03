package com.app.yellodriver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.model.HelpReasonModel;

import java.util.ArrayList;

public class HelpReasonAdapter extends RecyclerView.Adapter<HelpReasonAdapter.ViewHolder> {
    ArrayList<HelpReasonModel> helpReasonList;
    Context context;

    // RecyclerView recyclerView;
    public HelpReasonAdapter(Context con, ArrayList<HelpReasonModel> reasonList) {
        helpReasonList = reasonList;
        context = con;
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

        final HelpReasonModel model = helpReasonList.get(position);

        if (model.isSelected()) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_selected));
        } else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_deselect));
        }

        holder.tvCancelripReason.setText(helpReasonList.get(position).getHelpReason());
        holder.llreasonFullview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < helpReasonList.size(); i++) {
                    helpReasonList.get(i).setSelected(false);
                }
                model.setSelected(true);
                notifyDataSetChanged();

                //CancelTripDialog();
            }
        });
    }


    @Override
    public int getItemCount() {
        return helpReasonList.size();
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

    /*public void CancelTripDialog() {
        Dialog cancelTripDialog = new Dialog(context, R.style.FullHeightDialog);
        cancelTripDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelTripDialog.setContentView(R.layout.cancel_trip_dialog);
        cancelTripDialog.setCancelable(true);
        Button btnCancelTripAccept = (Button) cancelTripDialog.findViewById(R.id.btnCancelTripAccept);

        btnCancelTripAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelTripSuccess();
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

    public void CancelTripSuccess() {
        Dialog rideCompletedDialog = new Dialog(context, R.style.FullHeightDialog);
        rideCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rideCompletedDialog.setContentView(R.layout.ride_success_dialog);

        TextView txtView = rideCompletedDialog.findViewById(R.id.success_dialog_txtMsg);
        txtView.setText(context.getString(R.string.trip_has_been_cancelled_successfully));

        rideCompletedDialog.setCancelable(true);

        //now that the dialog is set up, it's time to show it
        rideCompletedDialog.show();
    }*/
}  