package com.app.yellodriver.model.ModelNotifSetting;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotifSettingsViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    public MutableLiveData<List<NotifSettingModel.Data.Yt_user_setting>> queryMutableLiveNotifySettings = new MutableLiveData<>();
    public MutableLiveData<List<NotifSettingModel.Data.Update_yt_user_setting.Returning>> updateMutableLiveNotifySettings = new MutableLiveData<>();
    public MutableLiveData<NotifSettingModel.Data.Yt_user_setting> insertMutableLiveNotifySettings = new MutableLiveData<>();

    public void getUserNotifySettings(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    NotifSettingModel modelContent = gson.fromJson(json, NotifSettingModel.class);

                    List<NotifSettingModel.Data.Yt_user_setting> listContent = modelContent.getData().getYt_user_setting();

                    queryMutableLiveNotifySettings.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                    }
                    queryMutableLiveNotifySettings.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                queryMutableLiveNotifySettings.postValue(null);
            }
        };

        if (NetworkUtils.isNetworkAvailable(YelloDriverApp.getCurrentActivity())) {
            ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                    .query(query)
                    .enqueue(callback);
        } else {
            Utils.showInternetDialog(YelloDriverApp.getCurrentActivity(), query, null, callback);
        }
    }

    public void setNotifySettings(Mutation mutation) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                YLog.e("NotifySetting Response", response.toString());
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    NotifSettingModel modelContent = gson.fromJson(json, NotifSettingModel.class);

                    List<NotifSettingModel.Data.Update_yt_user_setting.Returning> listContent = modelContent.getData().getUpdate_yt_user_setting().getReturning();

                    updateMutableLiveNotifySettings.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                    }
                    updateMutableLiveNotifySettings.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                updateMutableLiveNotifySettings.postValue(null);
            }
        };


        if (NetworkUtils.isNetworkAvailable(YelloDriverApp.getCurrentActivity())) {

            ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                    .mutate(mutation)
                    .enqueue(callback);
        } else {
            Utils.showInternetDialog(YelloDriverApp.getCurrentActivity(), null, mutation, callback);
        }
    }

    public void insertNotifySettings(Mutation mutation) {
        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                YLog.e("InsertNotifySetting Response", response.toString());
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    NotifSettingModel modelContent = gson.fromJson(json, NotifSettingModel.class);

                    NotifSettingModel.Data.Yt_user_setting listContent = modelContent.getData().getOneYt_user_setting();

                    insertMutableLiveNotifySettings.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                    }
                    insertMutableLiveNotifySettings.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                insertMutableLiveNotifySettings.postValue(null);
            }
        };

        if (NetworkUtils.isNetworkAvailable(YelloDriverApp.getCurrentActivity())) {

            ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                    .mutate(mutation)
                    .enqueue(callback);
        } else {
            Utils.showInternetDialog(YelloDriverApp.getCurrentActivity(), null, mutation, callback);
        }
    }
}