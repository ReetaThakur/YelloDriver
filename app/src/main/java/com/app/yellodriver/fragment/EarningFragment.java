package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.EarningBasedOnTimeQuery;
import com.app.yellodriver.EarningCommissionQuery;
import com.app.yellodriver.EarningLastTipQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelEarning.EarningViewModel;
import com.app.yellodriver.model.ModelEarning.ModelEarningCommission;
import com.app.yellodriver.model.ModelEarning.ModelEarningLastTip;
import com.app.yellodriver.model.ModelEarning.ModelEarningWeek;
import com.app.yellodriver.type.Timestamptz_comparison_exp;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EarningFragment extends BaseFragment {

    private TextView txtLastTripAmount;
    private TextView txtLastTripTotalAmount;
    private TextView txtLastTripRider;
    private TextView txtLastTripTime;
    private TextView txtLastTripDistance;

    private TextView txtTotalTripAmount;
    private TextView txtTotalMyTrip;
    private TextView txtTotalAmount;
    private TextView txtTotalTripValue;
    private TextView txtTotalTripTime;
    private TextView txtTotalTripDistance;

    private TextView txtUserCommisionDetails;
    private TextView txtUserTotalCommision;
    private TextView txtUserTotalBasicUser;
    private TextView txtUserTotalAdvanceUser;
    private TextView txtUserTotalPremiumUser;
    private TextView txtUserTotalBasicUserValue;
    private TextView txtUserTotalAdvanceUserValue;
    private TextView txtUserTotalPremiumUserValue;

    private RadioGroup rbGroup;
    private RadioButton rbWeek;
    private RadioButton rbMonth;
    private RadioButton rbYear;

    private static final String DATA_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private EarningViewModel earningViewModel;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;

    private String beforeDate;
    private String todayDate;

    @Override
    protected void initializeComponent(View view) {
        setUpToolBar(view);

        txtLastTripAmount = view.findViewById(R.id.fragment_earning_txtTripAmount);
        txtLastTripTotalAmount = view.findViewById(R.id.fragment_earning_txtPrice);
        txtLastTripRider = view.findViewById(R.id.fragment_earning_txtRiderValue);
        txtLastTripTime = view.findViewById(R.id.fragment_earning_txtTimeValue);
        txtLastTripDistance = view.findViewById(R.id.fragment_earning_txtDistanceValue);

        txtTotalTripAmount = view.findViewById(R.id.fragment_total_trip_txtTotalTripAmount);
        txtTotalMyTrip = view.findViewById(R.id.fragment_total_trip_txtMyTrip);
        txtTotalAmount = view.findViewById(R.id.fragment_total_earning_txtPrice);
        txtTotalTripValue = view.findViewById(R.id.fragment_total_trip_txtTotalTripValue);
        txtTotalTripTime = view.findViewById(R.id.fragment_total_trip_txtTotalTimeValue);
        txtTotalTripDistance = view.findViewById(R.id.fragment_total_trip_txtTotalDistanceValue);

        txtUserCommisionDetails = view.findViewById(R.id.fragment_user_commission_txtDetails);
        txtUserTotalCommision = view.findViewById(R.id.fragment_user_commission_txtPrice);
        txtUserTotalBasicUserValue = view.findViewById(R.id.fragment_user_commission_txtOneDayPassValue);
        txtUserTotalAdvanceUserValue = view.findViewById(R.id.fragment_user_commission_txtTwoDayPassValue);
        txtUserTotalPremiumUserValue = view.findViewById(R.id.fragment_user_commission_txtThreeDayPassValue);

        txtUserTotalBasicUser = view.findViewById(R.id.fragment_user_commission_txtOneDayPass);
        txtUserTotalAdvanceUser = view.findViewById(R.id.fragment_user_commission_txtTwoDayPass);
        txtUserTotalPremiumUser = view.findViewById(R.id.fragment_user_commission_txtThreeDayPassUser);

        rbGroup = view.findViewById(R.id.fragment_total_trip_radioGroup);
        rbWeek = view.findViewById(R.id.fragment_total_trip_rbWeek);
        rbMonth = view.findViewById(R.id.fragment_total_trip_rbMonth);
        rbYear = view.findViewById(R.id.fragment_total_trip_rbYear);

        earningViewModel = ViewModelProviders.of((HomeActivity) mActivity).get(EarningViewModel.class);
        earningViewModel.liveDataLastTip.observe(((HomeActivity) mActivity), observerLastTip);
        earningViewModel.liveDataWeek.observe(((HomeActivity) mActivity), observerWeek);
        earningViewModel.liveDataCommission.observe(((HomeActivity) mActivity), observerCommission);

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        EarningLastTipQuery earningLastTipQuery = new EarningLastTipQuery();
        earningViewModel.getLastTip(earningLastTipQuery);

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fragment_total_trip_rbWeek:
                        beforeDate = Utils.getCalculatedDate(DATA_FORMAT_UTC, -7);
                        todayDate = getCurrentUTC();

//                        customProgressDialog = new CustomProgressDialog(mActivity);
//                        customProgressDialog.showDialog();

                        Timestamptz_comparison_exp timestamptz_comparison_exp = Timestamptz_comparison_exp.builder()._gte(beforeDate)._lte(todayDate).build();

                        EarningBasedOnTimeQuery earningBasedOnTimeQuery = new EarningBasedOnTimeQuery(timestamptz_comparison_exp);
                        earningViewModel.getEarningBasedOnTime(earningBasedOnTimeQuery);
                        break;
                    case R.id.fragment_total_trip_rbMonth:
                        beforeDate = Utils.getCalculatedDate(DATA_FORMAT_UTC, -30);
                        todayDate = getCurrentUTC();

                        customProgressDialog = new CustomProgressDialog(mActivity);
                        customProgressDialog.showDialog();

                        Timestamptz_comparison_exp timestamptz_comparison_expMonth = Timestamptz_comparison_exp.builder()._gte(beforeDate)._lte(todayDate).build();

                        EarningBasedOnTimeQuery earningBasedOnTimeQueryMonth = new EarningBasedOnTimeQuery(timestamptz_comparison_expMonth);
                        earningViewModel.getEarningBasedOnTime(earningBasedOnTimeQueryMonth);
                        break;
                    case R.id.fragment_total_trip_rbYear:
                        beforeDate = Utils.getCalculatedDate(DATA_FORMAT_UTC, -365);
                        todayDate = getCurrentUTC();

                        customProgressDialog = new CustomProgressDialog(mActivity);
                        customProgressDialog.showDialog();

                        Timestamptz_comparison_exp timestamptz_comparison_expYear = Timestamptz_comparison_exp.builder()._gte(beforeDate)._lte(todayDate).build();

                        EarningBasedOnTimeQuery earningBasedOnTimeQueryYear = new EarningBasedOnTimeQuery(timestamptz_comparison_expYear);
                        earningViewModel.getEarningBasedOnTime(earningBasedOnTimeQueryYear);
                        break;
                }
            }
        });

        rbGroup.check(R.id.fragment_total_trip_rbWeek);

        txtTotalMyTrip.setOnClickListener(view12 -> {
            addFragment(R.id.activity_home_flContainer, EarningFragment.this, new MyTripsFragment(), false, false);
        });

        YLog.e("UTC Date ", getCurrentUTC());
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_earning;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_contactus);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.earning);
        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    public String getCurrentUTC() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat(DATA_FORMAT_UTC);
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }

    Observer<List<ModelEarningLastTip.Data.YtUserWallet>> observerLastTip = lastTipsList -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (lastTipsList != null && lastTipsList.size() > 0) {
            if (lastTipsList.get(0) != null) {
                if (lastTipsList.get(0).getAmount() != null) {
                    txtLastTripTotalAmount.setText("$" + lastTipsList.get(0).getAmount());
                    txtLastTripRider.setText(lastTipsList.get(0).getRide().getUser().getFullName());
                    txtLastTripDistance.setText(String.format("%.2f",lastTipsList.get(0).getRideDistance()/1.609) + " m");
                    txtLastTripTime.setText(lastTipsList.get(0).getRideDuration() + " mins");
                }
            }
        }

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        EarningCommissionQuery earningCommissionQuery = new EarningCommissionQuery();
        earningViewModel.getTotalCommission(earningCommissionQuery);

    };

    Observer<ModelEarningWeek.Data.TotalTipWeek> observerWeek = totalTipWeek -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (totalTipWeek != null) {
            if (totalTipWeek.getAggregate() != null) {
                if (totalTipWeek.getAggregate().getSum() != null) {
                    if (totalTipWeek.getAggregate().getSum().getAmount() != null) {
                        txtTotalAmount.setText("$" + totalTipWeek.getAggregate().getSum().getAmount());
                        txtTotalTripValue.setText("$" + totalTipWeek.getAggregate().getSum().getAmount());
                    }

                    if (totalTipWeek.getAggregate().getSum().getRideDistance() != null) {
                        txtTotalTripDistance.setText(String.format("%.2f",totalTipWeek.getAggregate().getSum().getRideDistance()/1.609) + " m");
                    }

                    if (totalTipWeek.getAggregate().getSum().getRideDuration() != null) {
                        txtTotalTripTime.setText(totalTipWeek.getAggregate().getSum().getRideDuration() + " mins");
                    }
                }
            }
        }
    };

    Observer<List<ModelEarningCommission.Data.YtGetDriverCommissionPlanWise>> observerCommission = totalCommission -> {

        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        if (totalCommission != null && totalCommission.size() > 0) {
            double total = 0;
            for (int i = 0; i < totalCommission.size(); i++) {
                total = total + totalCommission.get(i).getTotalAmount();
            }

            if (totalCommission.get(0) != null) {
                if (totalCommission.get(0).getPlanTitle() != null) {
                    txtUserTotalBasicUser.setText(totalCommission.get(0).getPlanTitle());
                }
            }

            if (totalCommission.get(0) != null) {
                if (totalCommission.get(0).getTotalUser() != 0) {
                    txtUserTotalBasicUserValue.setText(totalCommission.get(0).getTotalUser() + "");
                }
            }

            if (totalCommission.size() > 1) {

                if (totalCommission.get(1) != null) {
                    if (totalCommission.get(1).getPlanTitle() != null) {
                        txtUserTotalAdvanceUser.setText(totalCommission.get(1).getPlanTitle());
                    }
                }

                if (totalCommission.get(1) != null) {
                    if (totalCommission.get(1).getTotalUser() != 0) {
                        txtUserTotalAdvanceUserValue.setText(totalCommission.get(1).getTotalUser() + "");
                    }
                }
            }

            if (totalCommission.size() > 2) {

                if (totalCommission.get(2) != null) {
                    if (totalCommission.get(2).getPlanTitle() != null) {
                        txtUserTotalPremiumUser.setText(totalCommission.get(2).getPlanTitle());
                    }
                }

                if (totalCommission.get(2) != null) {
                    if (totalCommission.get(2).getTotalUser() != 0) {
                        txtUserTotalPremiumUserValue.setText(totalCommission.get(2).getTotalUser() + "");
                    }
                }
            }

            txtUserTotalCommision.setText("$" + total);
        }
    };
}