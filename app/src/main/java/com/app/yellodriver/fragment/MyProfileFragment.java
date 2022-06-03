package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.yellodriver.MyProfileQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.customview.CircleImageView;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.model.ModelProfile.MyProfileViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;

import io.paperdb.Paper;

public class MyProfileFragment extends BaseFragment {

    private TextView txtUpdateProfile;
    private TextView txtMyTrips;
    private TextView txtSettings;
    private TextView txtLogout;
    private TextView txtName;
    private TextView txtSince;
    private RatingBar ratingBar;
    private CircleImageView imgUser;
    private Activity mActivity;

    private MyProfileViewModel model;
    private MyProfileModel.Data.YtUser listUser = null;

    private CustomProgressDialog customProgressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    protected void initializeComponent(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        txtUpdateProfile = view.findViewById(R.id.txtUpdateProfile);
        txtMyTrips = view.findViewById(R.id.txtMyTrips);
        txtSettings = view.findViewById(R.id.txtSettings);
        txtName = view.findViewById(R.id.fragment_my_profile_txtName);
        txtSince = view.findViewById(R.id.fragment_my_profile_txtSince);
        txtLogout = view.findViewById(R.id.txtLogout);
        imgUser = view.findViewById(R.id.fragment_my_profile_imgUser);
        ratingBar = view.findViewById(R.id.ratingBar);

        tvName.setText(R.string.profile);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        txtUpdateProfile.setOnClickListener(view12 -> {

            UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();
            Bundle bundle = new Bundle();

            bundle.putParcelable(Constants.BUNDLE_PROFILE_DATA, listUser);
            updateProfileFragment.setArguments(bundle);
            addFragment(R.id.activity_home_flContainer, MyProfileFragment.this, updateProfileFragment, false, false);
        });

        txtMyTrips.setOnClickListener(view12 -> {
            addFragment(R.id.activity_home_flContainer, MyProfileFragment.this, new MyTripsFragment(), false, false);
        });

        txtSettings.setOnClickListener(view13 -> {
            SettingsFragment settingsFragment = new SettingsFragment();
            Bundle bundle = new Bundle();

            bundle.putParcelable(Constants.BUNDLE_PROFILE_DATA, listUser);
            settingsFragment.setArguments(bundle);
            addFragment(R.id.activity_home_flContainer, MyProfileFragment.this, settingsFragment, false, false);
        });

        txtLogout.setOnClickListener(view14 -> {
            ((HomeActivity) mActivity).logout();
        });

        setUpViewModel();

        customProgressDialog = new CustomProgressDialog(getActivity());
        customProgressDialog.showDialog();

        model.getProfileData(new MyProfileQuery(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "")));
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_my_profile;
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) getActivity()).get(MyProfileViewModel.class);

        model.mutableLiveDataProfile.observe(((HomeActivity) getActivity()), observerApoloResponse);

    }


    Observer<MyProfileModel.Data.YtUser> observerApoloResponse = userContentList -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (userContentList != null) {

            listUser = userContentList;

            txtName.setText(userContentList.getFullName());
            txtSince.setText(DateUtils.timeAgo(listUser.getCreatedAt()));

            if (listUser.getAverage_rate() != null) {
                ratingBar.setRating(Float.parseFloat(listUser.getAverage_rate()));
            } else {
                ratingBar.setRating(0f);
            }

            if (userContentList.getProfilePhoto() != null) {
                if (userContentList.getProfilePhoto().getId() != null) {
                    ImageLoader.loadGraphQlImage(getActivity(), imgUser, userContentList.getProfilePhoto().getId(), R.drawable.ic_place_holder_user);
                }
            }
        } else {

        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        YLog.e("****", hidden + "");
        if (!hidden) {

            customProgressDialog = new CustomProgressDialog(getActivity());
            customProgressDialog.showDialog();

            model.getProfileData(new MyProfileQuery(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "")));
        }
    }
}