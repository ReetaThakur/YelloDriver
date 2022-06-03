package com.app.yellodriver.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.adapters.CancelTripAdapter;
import com.app.yellodriver.model.CancelTripModel;
import com.app.yellodriver.model.ModelCancelOptions.ModelCancelOptions;
import com.app.yellodriver.util.Constants;

import java.util.ArrayList;

import io.paperdb.Paper;

public class DeclineTripFragment extends BaseFragment {

    private RecyclerView recyclerCancelTrip;
    private CancelTripAdapter cnlTrpAdapter;
    private ArrayList<ModelCancelOptions.Data.YtSystemSetting> reasonList = new ArrayList<>();
    private ModelCancelOptions modelCancelOptions = new ModelCancelOptions();

    @Override
    protected void initializeComponent(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        modelCancelOptions = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_MODEL_CANCEL_REASON, new ModelCancelOptions());
        if(modelCancelOptions!=null){
            if(modelCancelOptions.getData()!=null){
                if(modelCancelOptions.getData().getYtSystemSetting()!=null){
                    reasonList = modelCancelOptions.getData().getYtSystemSetting();
                }
            }
        }

        tvName.setText(R.string.decline_trip);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        recyclerCancelTrip = view.findViewById(R.id.recyclerCancelTrip);

        cnlTrpAdapter = new CancelTripAdapter(this,getActivity(), reasonList);
        recyclerCancelTrip.setAdapter(cnlTrpAdapter);
        recyclerCancelTrip.setHasFixedSize(true);
        recyclerCancelTrip.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_cancel_trip;
    }

}
