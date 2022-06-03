package com.app.yellodriver.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.ContentQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelFAQ.ContentViewModel;
import com.app.yellodriver.model.ModelFAQ.ModelHtmlContent;
import com.app.yellodriver.util.CustomProgressDialog;

import java.util.List;

public class PolicyFragment extends BaseFragment {

    private TextView txtPolicy;

    private String heading;
    private int pageLimit = 20;
    private int pageOffset = 0;

    private ContentViewModel model;
    private Input<Integer> limit = new Input<>(pageLimit, true);
    private Input<Integer> offset = new Input<>(pageOffset, true);
    private Input<String> slugName;;
    private TextView tvHeader;

    private CustomProgressDialog customProgressDialog;
    private Activity activity;

    @Override
    protected void initializeComponent(View view) {
        activity = getActivity();
        Bundle bundle = getArguments();

        if (bundle != null) {
            String rcvSlugname = bundle.getString(getString(R.string.bundle_slugname));
            slugName = new Input<>(rcvSlugname, true);
            heading = bundle.getString(getString(R.string.bundle_heading));
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        tvHeader = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        txtPolicy = view.findViewById(R.id.txtPolicy);

        tvHeader.setText(heading);
        txtPolicy.setText("");

        txtPolicy.setMovementMethod(new ScrollingMovementMethod());

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        setUpViewModel();

        customProgressDialog = new CustomProgressDialog(activity);
        customProgressDialog.showDialog();

        model.getHtmlData(new ContentQuery(limit, offset,slugName));
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_policy;
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) getActivity()).get(ContentViewModel.class);

        model.mutableApoloResponseHTML.observe(((HomeActivity) getActivity()), observerApoloResponse);
    }

    Observer<List<ModelHtmlContent.Data.YtHtmlContent>> observerApoloResponse = listContent -> {
        //if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }
            if (listContent != null && listContent.size() > 0) {

                String htmlString = listContent.get(0).getContent().getData();
                if (null != htmlString) {
                    txtPolicy.setText(Html.fromHtml(htmlString));
                    tvHeader.setText(listContent.get(0).getTitle());
                }
            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        //}
    };

}