package com.app.yellodriver.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.model.ModelTrip.TripModel;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ArrayList<TripModel.TripData.YtRide> mItemList;

    public TripListAdapter(ArrayList<TripModel.TripData.YtRide> itemList) {

        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == VIEW_TYPE_ITEM) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTripCreatedDate;
        TextView tvTripDistanceDuration;
        TextView tvStartAddress;
        TextView tvEndAddress;
        ImageView imgTrip;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartAddress = itemView.findViewById(R.id.tvtripstartaddress);
            tvEndAddress = itemView.findViewById(R.id.tvtripendaddress);
            tvTripCreatedDate = itemView.findViewById(R.id.tvtripcreatedate);
            tvTripDistanceDuration = itemView.findViewById(R.id.tvtripdistanceduration);
            imgTrip = itemView.findViewById(R.id.imgTrip);
        }

    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    String tripDuration;

    private void populateItemRows(ItemViewHolder viewHolder, final int position) {
        TripModel.TripData.YtRide singleTripObj = mItemList.get(position);

        viewHolder.tvStartAddress.setText(singleTripObj.getStart_address().getLine1());
        viewHolder.tvEndAddress.setText(singleTripObj.getEnd_address().getLine1());
        try {
            if (null != singleTripObj.getCreatedAt()) {
                viewHolder.tvTripCreatedDate.setText(DateUtils.convertDateToSuffixFormat(singleTripObj.getCreatedAt()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tripDuration = "NA";
        if (null != singleTripObj.getStart_at() && null != singleTripObj.getEnd_at()) {
            tripDuration = DateUtils.getDurationFromTwoDates(singleTripObj.getStart_at(), singleTripObj.getEnd_at());
        }
        viewHolder.tvTripDistanceDuration.setText(String.format("%.2f",singleTripObj.getDistance()/1.609) + "m | Duration: " + tripDuration);
        viewHolder.imgTrip.setImageDrawable(null);

        if (singleTripObj.getRoute_map_file_id() != null) {
            ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), viewHolder.imgTrip, singleTripObj.getRoute_map_file_id(), R.drawable.placeholder_banner);
        } else {
//            ImageLoader.loadImage(YelloDriverApp.getInstance(),viewHolder.imgTrip,R.drawable.placeholder_banner,R.drawable.placeholder_banner);

//            Glide.with(YelloDriverApp.getInstance()).clear(viewHolder.imgTrip);
            // remove the placeholder (optional); read comments below
//            viewHolder.imgTrip.setImageDrawable(null);
            ImageLoader.loadImage(YelloDriverApp.getInstance(), viewHolder.imgTrip, R.drawable.placeholder_banner, R.drawable.placeholder_banner);
        }
    }
}