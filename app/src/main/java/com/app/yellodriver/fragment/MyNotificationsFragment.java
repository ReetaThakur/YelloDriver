package com.app.yellodriver.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.NotificationQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.adapters.NotificationListAdapter;
import com.app.yellodriver.model.ModelNotification.MyNotificationsViewModel;
import com.app.yellodriver.model.ModelNotification.NotificationModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MyNotificationsFragment extends BaseFragment {

    RecyclerView rvMyNotifs;
    NotificationListAdapter rvNotifAdapter;

    boolean isLoading = false;

    private MyNotificationsViewModel model;

    private int pageLimit = 10;
    private int pageOffset = 0;

    private Input<Integer> limit = new Input<>(pageLimit, true);
    private Input<Integer> offset = new Input<>(pageOffset, true);

    private List<NotificationModel.Data.YtNotification> listContentMain = new ArrayList<>();
    private ConstraintLayout emptyNotifyview;
    private Button btnGoToNotifSettings;

    private CustomProgressDialog customProgressDialog;

    @Override
    protected int defineLayoutResource() {
        return R.layout.layout_notificationlist;
    }

    @Override
    protected void initializeComponent(View view) {

        rvMyNotifs = view.findViewById(R.id.rvnotifications);
        emptyNotifyview = view.findViewById(R.id.clnonotifications);
        btnGoToNotifSettings = view.findViewById(R.id.btngonotifstgs);
        btnGoToNotifSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(R.id.activity_home_flContainer, MyNotificationsFragment.this, new NotificationSettingFragment(), false, false);
            }
        });

        rvNotifAdapter = new NotificationListAdapter(listContentMain);
        rvMyNotifs.setAdapter(rvNotifAdapter);

        setUpToolBar(view);
        setUpViewModel();
        initScrollListener();

        customProgressDialog = new CustomProgressDialog(getActivity());
        customProgressDialog.showDialog();

        String senderUserId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");
        model.getNotificationData(new NotificationQuery(limit, offset, senderUserId));
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.notification);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) getActivity()).get(MyNotificationsViewModel.class);

        model.mutableLiveDataNotifications.observe(((HomeActivity) getActivity()), observerApoloResponse);

    }


    Observer<List<NotificationModel.Data.YtNotification>> observerApoloResponse = listContent -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (listContent != null) {
            if (listContentMain.size() > 0 && (null == listContentMain.get(listContentMain.size() - 1))) {
                listContentMain.remove(listContentMain.size() - 1);
                int scrollPosition = listContentMain.size();
                rvNotifAdapter.notifyItemRemoved(scrollPosition);
            }
            listContentMain.addAll(listContent);
            rvNotifAdapter.notifyDataSetChanged();
        } else {
            if (pageOffset == 0) {
                emptyNotifyview.setVisibility(View.VISIBLE);
                rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };

    private void initScrollListener() {
        rvMyNotifs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listContentMain.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                listContentMain.add(null);
                rvNotifAdapter.notifyItemInserted(listContentMain.size() - 1);

                pageOffset = pageOffset + 10;

                Input<Integer> limit = new Input<>(pageLimit, true);
                Input<Integer> offset = new Input<>(pageOffset, true);
                String senderUserId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");

                model.getNotificationData(new NotificationQuery(limit, offset,senderUserId));

            }
        }, 100);
    }
}