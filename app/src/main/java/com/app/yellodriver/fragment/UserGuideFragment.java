package com.app.yellodriver.fragment;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.app.yellodriver.R;
import com.app.yellodriver.adapters.UsrguideExpandableListAdapter;
import com.app.yellodriver.model.UserGudesModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserGuideFragment extends BaseFragment {
    ExpandableListView expandLVUsrGuide;
    UsrguideExpandableListAdapter usrGuideListAdapter;
    ArrayList<String> expandableListTitle;
    HashMap<String, List<String>> expListUsrGuideData;

    @Override
    protected void initializeComponent(View view) {
        setUpToolBar(view);
        expandLVUsrGuide = (ExpandableListView) view.findViewById(R.id.explvusrguide);
        expListUsrGuideData = UserGudesModel.getData();
        expandableListTitle = new ArrayList<String>(expListUsrGuideData.keySet());
        usrGuideListAdapter = new UsrguideExpandableListAdapter(getActivity(), expandableListTitle, expListUsrGuideData);
        expandLVUsrGuide.setAdapter(usrGuideListAdapter);
        expandLVUsrGuide.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandLVUsrGuide.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandLVUsrGuide.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                return false;
            }
        });

    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_userguide;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_userguide);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.user_guide);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }
}
