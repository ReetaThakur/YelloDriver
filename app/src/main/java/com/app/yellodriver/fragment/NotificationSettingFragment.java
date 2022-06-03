package com.app.yellodriver.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.GetUserSettingQuery;
import com.app.yellodriver.InserUserSettingsMutation;
import com.app.yellodriver.R;
import com.app.yellodriver.UpdateUserSettingsMutation;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelNotifSetting.NotifSettingModel;
import com.app.yellodriver.model.ModelNotifSetting.NotifSettingModel.Params;
import com.app.yellodriver.model.ModelNotifSetting.NotifSettingsViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.NotificationSettings;
import com.app.yellodriver.util.YLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.paperdb.Paper;

public class NotificationSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    Switch swPushNotification, swEmailNotification, swSMSNotification, swPromotionNotification, swFlashOnRideRequestNotification, swVibrationOnRideRequestNotification;
    //private Input<JSONObject> paramObject = Input.absent();
    private NotifSettingsViewModel notifySettingsVwModel;

    private CustomProgressDialog customProgressDialog;
    private Activity activity;

    @Override
    protected void initializeComponent(View view) {

        activity = getActivity();

        setUpToolBar(view);

        NotificationSettings.read(activity);

        setUpViewModel();

        swPushNotification = view.findViewById(R.id.swPushNotify);
        //swPushNotification.setChecked(NotificationSettings.pushNotification);

        swEmailNotification = view.findViewById(R.id.swEmailNotify);
        //swEmailNotification.setChecked(NotificationSettings.emailNotification);
        //swEmailNotification.setOnCheckedChangeListener(this);

        swSMSNotification = view.findViewById(R.id.swSMSNotify);
        //swSMSNotification.setChecked(NotificationSettings.smsNotification);
        //swSMSNotification.setOnCheckedChangeListener(this);

        swPromotionNotification = view.findViewById(R.id.swPromotionNotify);
        //swPromotionNotification.setChecked(NotificationSettings.promotionNotification);
        //swPromotionNotification.setOnCheckedChangeListener(this);

        swFlashOnRideRequestNotification = view.findViewById(R.id.swFlashOnRideRequest);
        swFlashOnRideRequestNotification.setChecked(NotificationSettings.flashOnRideRequestNotification);
        swFlashOnRideRequestNotification.setOnCheckedChangeListener(this);

        swVibrationOnRideRequestNotification = view.findViewById(R.id.swVibrateOnRideRequest);
        swVibrationOnRideRequestNotification.setChecked(NotificationSettings.vibrationOnRideRequestNotification);
        swVibrationOnRideRequestNotification.setOnCheckedChangeListener(this);

        customProgressDialog = new CustomProgressDialog(activity);
        customProgressDialog.showDialog();

        notifySettingsVwModel.getUserNotifySettings(new GetUserSettingQuery());
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_notification_setting;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
        tvName.setText(R.string.notificationsetting);
        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    private void setUpViewModel() {

        notifySettingsVwModel = ViewModelProviders.of((HomeActivity) activity).get(NotifSettingsViewModel.class);


        notifySettingsVwModel.updateMutableLiveNotifySettings.observe(getViewLifecycleOwner(), updateObserverApoloResponse);
        notifySettingsVwModel.queryMutableLiveNotifySettings.observe(getViewLifecycleOwner(), queryObserverApoloResponse);
        notifySettingsVwModel.insertMutableLiveNotifySettings.observe(getViewLifecycleOwner(), insertObserverApoloResponse);

    }

    Observer<List<NotifSettingModel.Data.Yt_user_setting>> queryObserverApoloResponse = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (listContent != null) {

                if(listContent.size()==0){
                    //No settings on server so insert one
                    JSONObject paramsObj = new JSONObject();
                    try {
                        paramsObj.put("email", swEmailNotification.isChecked());
                        paramsObj.put("promotional", swPromotionNotification.isChecked());
                        paramsObj.put("push", swPushNotification.isChecked());
                        paramsObj.put("sms", swSMSNotification.isChecked());
                        //postObj.put("params",paramsObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //customProgressDialog = new CustomProgressDialog(getActivity());
                    //customProgressDialog.showDialog();
                    Input<Object> params = new Input<Object>(paramsObj, true);
                    Input<Object> curUserId = new Input<Object>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID, ""),true);
                    notifySettingsVwModel.insertNotifySettings(new InserUserSettingsMutation(params,curUserId));
                }else {
                    //Update switches based on settings on server
                    for (int q = 0; q < listContent.size(); q++) {
                        Params rcvdParams = listContent.get(q).getParams();
                        boolean isEmail = rcvdParams.getEmail();
                        boolean isPromotional = rcvdParams.getPromotional();
                        boolean isPush = rcvdParams.getPush();
                        boolean isSMS = rcvdParams.getSms();


                        swEmailNotification.setChecked(isEmail);
                        swPromotionNotification.setChecked(isPromotional);
                        swPushNotification.setChecked(isPush);
                        swSMSNotification.setChecked(isSMS);
                    }
                }

            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }

            swEmailNotification.setOnCheckedChangeListener(this);
            swPromotionNotification.setOnCheckedChangeListener(this);
            swPushNotification.setOnCheckedChangeListener(this);
            swSMSNotification.setOnCheckedChangeListener(this);
        }
    };

    Observer<List<NotifSettingModel.Data.Update_yt_user_setting.Returning>> updateObserverApoloResponse = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (listContent != null) {
                for (int q = 0; q < listContent.size(); q++) {
                    Params rcvdParams = listContent.get(q).getParams();
                    YLog.e("Updated", rcvdParams.toString());
                /*boolean isEmail = rcvdParams.getEmail();
                boolean isPromotional = rcvdParams.getPromotional();
                boolean isPush = rcvdParams.getPush();
                boolean isSMS = rcvdParams.getSms();

                swEmailNotification.setChecked(isEmail);
                swPromotionNotification.setChecked(isPromotional);
                swPushNotification.setChecked(isPush);
                swSMSNotification.setChecked(isSMS);*/
                }

            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };

    Observer<NotifSettingModel.Data.Yt_user_setting> insertObserverApoloResponse = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (listContent != null) {

                Params rcvdParams = listContent.getParams();
                YLog.e("Inserted", rcvdParams.toString());

            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {

            case R.id.swPushNotify:
                NotificationSettings.pushNotification = b;
                break;
            case R.id.swEmailNotify:
                NotificationSettings.emailNotification = b;
                break;
            case R.id.swSMSNotify:
                NotificationSettings.smsNotification = b;
                break;
            case R.id.swPromotionNotify:
                NotificationSettings.promotionNotification = b;
                break;
            case R.id.swFlashOnRideRequest:
                NotificationSettings.flashOnRideRequestNotification = b;
                break;
            case R.id.swVibrateOnRideRequest:
                NotificationSettings.vibrationOnRideRequestNotification = b;
                break;
        }

        JSONObject paramsObj = new JSONObject();
        //JSONObject postObj = new JSONObject();
        /*{
          "params": { "sms": true, "push": true, "email": true, "promotional": true }
        }*/
        try {
            paramsObj.put("email", swEmailNotification.isChecked());
            paramsObj.put("promotional", swPromotionNotification.isChecked());
            paramsObj.put("push", swPushNotification.isChecked());
            paramsObj.put("sms", swSMSNotification.isChecked());
            //postObj.put("params",paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //customProgressDialog = new CustomProgressDialog(getActivity());
        //customProgressDialog.showDialog();
        Input<Object> params = new Input<Object>(paramsObj, true);
        notifySettingsVwModel.setNotifySettings(new UpdateUserSettingsMutation(params));
        NotificationSettings.write(getActivity());
    }
}
