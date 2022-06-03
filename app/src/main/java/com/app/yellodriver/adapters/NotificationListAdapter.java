package com.app.yellodriver.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.model.ModelNotification.NotificationModel;
import com.app.yellodriver.util.DateUtils;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<NotificationModel.Data.YtNotification> mItemList;


    public NotificationListAdapter(List<NotificationModel.Data.YtNotification> itemList) {

        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

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

        TextView tvMainMessage;
        TextView tvDateHdr;
        TextView tvDateCreated;
        //SwipeLayout swipeLayout;
        //TextView tvCnfDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
            //swipeLayout.close();
            //tvCnfDelete = itemView.findViewById(R.id.tvcnfdelete);
            //swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            tvDateCreated = itemView.findViewById(R.id.tvdatenotif);
            tvMainMessage = itemView.findViewById(R.id.tvnotiftxt);
            tvDateHdr = itemView.findViewById(R.id.tvnotifdatehdr);
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

    private void populateItemRows(ItemViewHolder viewHolder, final int position) {
        NotificationModel.Data.YtNotification singleNotifObj = mItemList.get(position);
        String message = singleNotifObj.getContent().getMessage();
        String createdDate = singleNotifObj.getCreated_at();
        String msgDate = DateUtils.convertDateToNewFormat(createdDate,"hh:mm a");
        viewHolder.tvDateCreated.setText(msgDate);
        String hdrDate = DateUtils.convertDateToNewFormat(createdDate,"MMM, dd");
        viewHolder.tvMainMessage.setText(message);
        mItemList.get(position).setDisplayHdrDate(hdrDate);
        viewHolder.tvDateHdr.setText(hdrDate);

        // if not first item, check if item above has the same header
        if (position > 0 && mItemList.get(position - 1).getDisplayHdrDate().equalsIgnoreCase(hdrDate)) {
            viewHolder.tvDateHdr.setVisibility(View.GONE);
        } else {
            viewHolder.tvDateHdr.setVisibility(View.VISIBLE);
        }

        /*viewHolder.tvCnfDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemList.remove(position);
                notifyDataSetChanged();
            }
        });*/
    }
}
