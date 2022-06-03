package com.app.yellodriver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.model.ModelOnboardHistory.OnoboardHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class OnBoardHistoryAdapter extends RecyclerView.Adapter<OnBoardHistoryAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<OnoboardHistoryModel> onoboardHistoryModelList;
    private List<OnoboardHistoryModel> onoboardHistoryModelListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOnBoardName, tvOnBoardDate,tvOnboardPhno,tvOnBoardPassType;

        public MyViewHolder(View view) {
            super(view);
            tvOnBoardName = view.findViewById(R.id.tvOnBoardName);
            tvOnboardPhno = view.findViewById(R.id.tvOnboardPhno);
            tvOnBoardDate = view.findViewById(R.id.tvOnBoardDate);
            tvOnBoardPassType = view.findViewById(R.id.tvOnBoardPassType);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(onoboardHistoryModelListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public OnBoardHistoryAdapter(Context context, List<OnoboardHistoryModel> onoboardHistoryModelList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.onoboardHistoryModelList = onoboardHistoryModelList;
        this.onoboardHistoryModelListFiltered = onoboardHistoryModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.onboard_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OnoboardHistoryModel onoboardHistoryModel = onoboardHistoryModelListFiltered.get(position);
        holder.tvOnBoardName.setText(onoboardHistoryModel.getName());
        holder.tvOnBoardDate.setText(onoboardHistoryModel.getDate());
        holder.tvOnboardPhno.setText(onoboardHistoryModel.getPhone());
        holder.tvOnBoardPassType.setText(onoboardHistoryModel.getPassType());
    /*    Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
*/    }

    @Override
    public int getItemCount() {
        return onoboardHistoryModelListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    onoboardHistoryModelListFiltered = onoboardHistoryModelList;
                } else {
                    List<OnoboardHistoryModel> filteredList = new ArrayList<>();
                    for (OnoboardHistoryModel row : onoboardHistoryModelList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    onoboardHistoryModelListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = onoboardHistoryModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                onoboardHistoryModelListFiltered = (ArrayList<OnoboardHistoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(OnoboardHistoryModel onoboardHistoryModel);
    }
}
