package com.app.yellodriver.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.OnBoarderHistoryQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.adapters.OnBoardHistoryAdapter;
import com.app.yellodriver.model.ModelOnboardHistory.OnoboardHistoryModel;
import com.app.yellodriver.model.ModelOnboardHistory.history.ModelOnBoardedUser;
import com.app.yellodriver.model.ModelOnboardHistory.history.OnBoardedHistoryViewModel;
import com.app.yellodriver.model.ModelUserOnboarding.UserOnboardingViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class OnBoardHistoryFragment extends BaseFragment implements OnBoardHistoryAdapter.ContactsAdapterListener {
    private RecyclerView historyRecyclerView;
    private List<OnoboardHistoryModel> onoboardHistoryModelList;
    private OnBoardHistoryAdapter mAdapter;
    private SearchView searchByNameView;

    private OnBoardedHistoryViewModel onBoardedHistoryViewModel;
    private Activity mActivity;
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
        setUpToolBar(view);

        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        onoboardHistoryModelList = new ArrayList<>();
        mAdapter = new OnBoardHistoryAdapter(getActivity(), onoboardHistoryModelList, OnBoardHistoryFragment.this);

        // white background notification bar

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        historyRecyclerView.setLayoutManager(mLayoutManager);
        historyRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.setAdapter(mAdapter);

        onBoardedHistoryViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(OnBoardedHistoryViewModel.class);
        onBoardedHistoryViewModel.mutableLiveDataHistory.observe(getViewLifecycleOwner(), observerOnBoardedHistory);

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        String driverId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, "");

        OnBoarderHistoryQuery onBoarderHistoryQuery = new OnBoarderHistoryQuery(driverId);
        onBoardedHistoryViewModel.getOnBoardedHistory(onBoarderHistoryQuery);


        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchByNameView = (SearchView) view.findViewById(R.id.searchByNameView);
        searchByNameView.requestFocusFromTouch();
        searchByNameView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchByNameView.setMaxWidth(Integer.MAX_VALUE);
        searchByNameView.clearFocus();
//        searchByNameView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.searchbyname) + "</font>"));

        LinearLayout first = (LinearLayout)searchByNameView.getChildAt(0);
        LinearLayout second = (LinearLayout)first.getChildAt(2);
        LinearLayout third = (LinearLayout)second.getChildAt(1);
        SearchView.SearchAutoComplete autoComplete = (SearchView.SearchAutoComplete)third.getChildAt(0);

        autoComplete.setHintTextColor(getResources().getColor(R.color.colorWhite));
        autoComplete.setTextColor(getResources().getColor(R.color.colorWhite));


        searchByNameView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    try {
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            }
                        }, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // listening to search query text change
        searchByNameView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.onboardhistory_fragment;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_contactus);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.onboardhistory);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    Observer<List<ModelOnBoardedUser.Data.YtUser>> observerOnBoardedHistory = ytUser -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (ytUser != null) {
            fetchContacts(ytUser);
        }
    };

    private void fetchContacts(List<ModelOnBoardedUser.Data.YtUser> ytUserList) {
        onoboardHistoryModelList.clear();

        if (ytUserList != null) {
            if (ytUserList.size() > 0) {
                for (int i = 0; i < ytUserList.size(); i++) {

                    if (ytUserList.get(i) != null && ytUserList.get(i).getBoardingPasses() != null) {
                        OnoboardHistoryModel item = new OnoboardHistoryModel();
                        item.setName(ytUserList.get(i).getFullName());
                        item.setPhone(ytUserList.get(i).getMobile());
                        if (ytUserList.get(i).getBoardingPasses().size() > 0) {
                            item.setDate(DateUtils.convertDate(DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DateUtils.DATE_FORMAT_DD_MM_YYYY, ytUserList.get(i).getBoardingPasses().get(0).getPurchasedAt()));
                            item.setPassType(ytUserList.get(i).getBoardingPasses().get(0).getPlan().getTitle());
                        } else {
                            item.setDate("");
                            item.setPassType("");
                        }
                        onoboardHistoryModelList.add(item);
                    }
                }
            }
        }

        // refreshing recycler view
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onContactSelected(OnoboardHistoryModel onoboardHistoryModel) {
//        Toast.makeText(getActivity(), "Selected: " + onoboardHistoryModel.getName() + ", " + onoboardHistoryModel.getPhone(), Toast.LENGTH_LONG).show();
    }
}
