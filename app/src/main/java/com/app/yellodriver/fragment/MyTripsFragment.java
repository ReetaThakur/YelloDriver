package com.app.yellodriver.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.MyTripsQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.adapters.TripListAdapter;
import com.app.yellodriver.model.ModelTrip.TripModel;
import com.app.yellodriver.model.ModelTrip.TripsViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MyTripsFragment extends BaseFragment {

    RecyclerView rvMyTrips;
    TripListAdapter rvTripsAdapter;
    private int pageLimit = 10;
    private int pageOffset = 0;

    private Input<Integer> limit = new Input<>(pageLimit, true);
    private Input<Integer> offset = new Input<>(pageOffset, true);
    //private Input<Object> driver_userID = new Input<Object>("9e5e68ea-ca0f-438b-bd07-944fddaff1d2", true);
    //private Input<Object> driver_userID = new Input<Object>("", true);

    private TripsViewModel model;
    ArrayList<TripModel.TripData.YtRide> tripsArrayList = new ArrayList<>();

    boolean isLoading = false;
    private CustomProgressDialog customProgressDialog;

    private Activity activity;

    String driverUserId;
    Input<Object> inputDriverUserId;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initializeComponent(View view) {
        rvMyTrips = view.findViewById(R.id.rvTrips);
        //populateData();
        //initAdapter();
        activity = getActivity();

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.my_trips);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        rvTripsAdapter = new TripListAdapter(tripsArrayList);
        rvMyTrips.setAdapter(rvTripsAdapter);

        setUpViewModel();
        initScrollListener();
        customProgressDialog = new CustomProgressDialog(activity);
        customProgressDialog.showDialog();

        driverUserId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID);
        inputDriverUserId = new Input<>(driverUserId, true);

        model.getTripData(new MyTripsQuery(limit, offset, inputDriverUserId));
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) activity).get(TripsViewModel.class);

        model.mutableLiveDataTrip.observe(((HomeActivity) activity), observerApoloResponse);

    }

    Observer<List<TripModel.TripData.YtRide>> observerApoloResponse = listContent -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (listContent != null) {

            tripsArrayList.addAll(listContent);
            rvTripsAdapter.notifyDataSetChanged();

            /*for(int q=0;q<listContent.size();q++) {
                String title = listContent.get(q).getDoc_type().getTitle();
                String desc = listContent.get(q).getDoc_type().getDescription();
                String fileUploadID = listContent.get(q).getFile_upload_id();
                YLog.e("title:description:fileId", title + ":"+desc+":"+fileUploadID);
            }*/

        } else {
            //emptyNotifyview.setVisibility(View.VISIBLE);
            //rvMyNotifs.setVisibility(View.GONE);
        }
    };

    private void initScrollListener() {
        rvMyTrips.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tripsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        tripsArrayList.add(null);
        rvTripsAdapter.notifyItemInserted(tripsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tripsArrayList.remove(tripsArrayList.size() - 1);
                int scrollPosition = tripsArrayList.size();
                rvTripsAdapter.notifyItemRemoved(scrollPosition);

                pageOffset = pageOffset + 10;

                Input<Integer> offset = new Input<>(pageOffset, true);

                model.getTripData(new MyTripsQuery(limit, offset, inputDriverUserId));

                //rvTripsAdapter.notifyDataSetChanged();
                //isLoading = false;
            }
        }, 2000);
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.layout_triphistory;
    }
}