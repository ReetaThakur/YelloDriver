package com.app.yellodriver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.model.MyDocItem;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyDocumentAdapter extends RecyclerView.Adapter<MyDocumentAdapter.ViewHolder> {
    private ArrayList<MyDocItem> listdata;
    Context context;

    public MyDocumentAdapter(Context con, ArrayList<MyDocItem> listdata) {
        this.listdata = listdata;
        context = con;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mydoc_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyDocItem myListData = listdata.get(position);
        holder.tvDocName.setText(listdata.get(position).getDescription());
//        if(listdata.get(position).getImgId()==null){
//            holder.imgDoc.setImageBitmap(null);
//            holder.imgDoc.setBackgroundResource(R.drawable.rounded_filledgray_background);
//        }else {
//            Glide.with(context).load(listdata.get(position).getImgId()).into(holder.imgDoc);;
//            holder.imgDoc.setBackgroundResource(R.drawable.rounded_bordergray_background);
//        }

        holder.imgDoc.setImageDrawable(null);

        if (myListData.getFileUploadID() != null) {
            ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), holder.imgDoc, myListData.getFileUploadID(), R.drawable.placeholder_banner);
        } else {
//            ImageLoader.loadImage(YelloDriverApp.getInstance(),viewHolder.imgTrip,R.drawable.placeholder_banner,R.drawable.placeholder_banner);

//            Glide.with(YelloDriverApp.getInstance()).clear(viewHolder.imgTrip);
            // remove the placeholder (optional); read comments below
//            viewHolder.imgTrip.setImageDrawable(null);
            ImageLoader.loadImage(YelloDriverApp.getInstance(), holder.imgDoc, R.drawable.placeholder_banner, R.drawable.placeholder_banner);
        }

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgDoc;
        public TextView tvDocName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imgDoc = (ImageView) itemView.findViewById(R.id.ivdocimage);
            this.tvDocName = (TextView) itemView.findViewById(R.id.tvdocname);
        }
    }
}  