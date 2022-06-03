package com.app.yellodriver.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.yellodriver.R;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsFragment extends BaseFragment {

    private TextView txtNotificationSettings;
    private TextView txtFaqs;
    private TextView txtPrivacyPolicy;
    private TextView txtTerms;
    private TextView txtAboutUs;
    private TextView txtUserGuide;
    private TextView txtHelpDesk;

    private TextView txtUserName;
    private TextView txtSince;
    private ImageView imgUser;
    private RatingBar ratingBar;


    private MyProfileModel.Data.YtUser userList = null;

    @Override
    protected void initializeComponent(View view) {

        if (getArguments() != null) {
            if (getArguments().getParcelable(Constants.BUNDLE_PROFILE_DATA) != null) {
                userList = getArguments().getParcelable(Constants.BUNDLE_PROFILE_DATA);
            }
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        txtNotificationSettings = view.findViewById(R.id.txtNotificationSettings);
        txtFaqs = view.findViewById(R.id.txtFaq);
        txtPrivacyPolicy = view.findViewById(R.id.txtPrivacyPolicy);
        txtTerms = view.findViewById(R.id.txtTerms);
        txtAboutUs = view.findViewById(R.id.txtAboutUs);
        txtUserGuide = view.findViewById(R.id.txtUserGuide);
        txtHelpDesk = view.findViewById(R.id.txtHelpDesk);

        txtUserName = view.findViewById(R.id.fragment_settings_txtUsername);
        txtSince = view.findViewById(R.id.fragment_settings_txtSince);
        imgUser = view.findViewById(R.id.fragment_settings_imgUser);
        ratingBar = view.findViewById(R.id.ratingBar);

        tvName.setText(R.string.string_settings);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        if (userList != null) {
            txtUserName.setText(userList.getFullName());
            txtUserName.setText(userList.getFullName());

            txtSince.setText(DateUtils.timeAgo(userList.getCreatedAt()));

            if (userList.getAverage_rate() != null) {
                ratingBar.setRating(Float.parseFloat(userList.getAverage_rate()));
            } else {
                ratingBar.setRating(0f);
            }

            if (userList.getProfilePhoto() != null) {
                if (userList.getProfilePhoto().getId() != null) {
                    ImageLoader.loadGraphQlImage(getActivity(), imgUser, userList.getProfilePhoto().getId(), R.drawable.ic_place_holder_user);
                }
            }
        }

        txtNotificationSettings.setOnClickListener(view12 -> {
            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, new NotificationSettingFragment(), false, false);
        });

        txtFaqs.setOnClickListener(view12 -> {
            //addFragment(R.id.activity_home_flContainer, SettingsFragment.this, new FAQsFragment(), false, false);

            FAQsFragment faQsFragment = new FAQsFragment();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.bundle_heading), getString(R.string.mnu_faqs));
            bundle.putString(getString(R.string.bundle_slugname), "faq_driver");
            faQsFragment.setArguments(bundle);


            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, faQsFragment, false, false);

        });

        txtUserGuide.setOnClickListener(view12 -> {
            //addFragment(R.id.activity_home_flContainer, SettingsFragment.this, new UserGuideFragment(), false, false);

            FAQsFragment faQsFragment = new FAQsFragment();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.bundle_heading), getString(R.string.user_guide));
            bundle.putString(getString(R.string.bundle_slugname), "ug_driver");
            faQsFragment.setArguments(bundle);


            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, faQsFragment, false, false);

        });

        txtPrivacyPolicy.setOnClickListener(view12 -> {
            PolicyFragment policyFragment = new PolicyFragment();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.bundle_heading), getString(R.string.privacy_policy));
            bundle.putString(getString(R.string.bundle_slugname), "privacy_policy");
            policyFragment.setArguments(bundle);


            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, policyFragment, false, false);
        });

        txtAboutUs.setOnClickListener(view12 -> {
            PolicyFragment policyFragment = new PolicyFragment();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.bundle_heading), getString(R.string.about_us));
            bundle.putString(getString(R.string.bundle_slugname), "about_us");
            policyFragment.setArguments(bundle);

            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, policyFragment, false, false);
        });

        txtTerms.setOnClickListener(view12 -> {
            PolicyFragment policyFragment = new PolicyFragment();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.bundle_heading), getString(R.string.terms_conditions));
            bundle.putString(getString(R.string.bundle_slugname), "terms_and_conditions");
            policyFragment.setArguments(bundle);

            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, policyFragment, false, false);
        });

        txtHelpDesk.setOnClickListener(view13 -> {
            addFragment(R.id.activity_home_flContainer, SettingsFragment.this, new ContactUsFragment(), false, false);
        });
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_settings;
    }
}