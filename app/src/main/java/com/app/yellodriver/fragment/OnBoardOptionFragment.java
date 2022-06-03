package com.app.yellodriver.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.yellodriver.R;

public class OnBoardOptionFragment extends BaseFragment {

    TextView txtAddNewUser;
    TextView txtHistory;
    TextView txtStats;

    @Override
    protected void initializeComponent(View view) {
        setUpToolBar(view);

        txtAddNewUser = view.findViewById(R.id.fragment_onboard_options_add_user);
        txtHistory = view.findViewById(R.id.fragment_onboard_options_history);
        txtStats = view.findViewById(R.id.fragment_onboard_options_stats);

        txtAddNewUser.setOnClickListener(view1 -> {
            addFragment(R.id.activity_home_flContainer, OnBoardOptionFragment.this, new UserOnBoardingStepOneFragment(), false, false);
//            addFragment(R.id.activity_home_flContainer, OnBoardOptionFragment.this, new UserOnBoardingStepThreeFragment(), false, false);
        });

        txtHistory.setOnClickListener(view2 -> {
            addFragment(R.id.activity_home_flContainer, OnBoardOptionFragment.this, new OnBoardHistoryFragment(), false, false);
        });

        txtStats.setOnClickListener(view3 -> {
            addFragment(R.id.activity_home_flContainer, OnBoardOptionFragment.this, new EarningFragment(), false, false);
        });
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_onboard_options;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_contactus);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.user_onboarding);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }
}