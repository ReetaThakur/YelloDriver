package com.app.yellodriver.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.ContentQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.adapters.FAQsExpandableListAdapter;
import com.app.yellodriver.model.ModelFAQ.ContentViewModel;
import com.app.yellodriver.model.ModelFAQ.ModelContent;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.YLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQsFragment extends BaseFragment {
    ExpandableListView expandLVFAQ;
    FAQsExpandableListAdapter faqListAdapter;
    ArrayList<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private ContentViewModel model;
    private int pageLimit = 20;
    private int pageOffset = 0;

    private Input<Integer> limit = new Input<>(pageLimit, true);
    private Input<Integer> offset = new Input<>(pageOffset, true);
    private Input<String> slugName;// = new Input<>("faq_driver", true);
    private String heading;

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

        setUpToolBar(view);
        expandLVFAQ = (ExpandableListView) view.findViewById(R.id.explvfaq);


        expandableListDetail = new HashMap<>();
        expandableListTitle = new ArrayList<String>();

        faqListAdapter = new FAQsExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandLVFAQ.setAdapter(faqListAdapter);
        expandLVFAQ.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandLVFAQ.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandLVFAQ.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                return false;
            }
        });

        setUpViewModel();

        customProgressDialog = new CustomProgressDialog(activity);
        customProgressDialog.showDialog();

        model.getFAQData(new ContentQuery(limit, offset, slugName));
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_frequently_asked_question;
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) getActivity()).get(ContentViewModel.class);

        model.mutableApoloResponseQA.observe(((HomeActivity) getActivity()), observerApoloResponse);
    }

    Observer<List<ModelContent.Data.YtContent>> observerApoloResponse = listContent -> {

        expandableListTitle.clear();
        expandableListDetail.clear();
        faqListAdapter.notifyDataSetChanged();

        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (listContent != null) {
            for (int q = 0; q < listContent.size(); q++) {
                String questionString = listContent.get(q).getContent().getData().getQ();
                if (null != questionString && questionString.trim().length() > 0) {
                    String answerString = listContent.get(q).getContent().getData().getA();
                    YLog.e("Ques:" + questionString, "Ans:" + answerString);
                    expandableListTitle.add(questionString);

                    List<String> answerArr = new ArrayList<String>();
                    answerArr.add(answerString);
                    expandableListDetail.put(questionString, answerArr);
                }
            }
            faqListAdapter.notifyDataSetChanged();

        } else {
            //emptyNotifyview.setVisibility(View.VISIBLE);
            //rvMyNotifs.setVisibility(View.GONE);
        }
    };

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_vehicleinfo);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(heading + "");

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }
}
