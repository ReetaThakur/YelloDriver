package com.app.yellodriver.model.ModelEarning;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Error;
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

public class EarningViewModel extends ViewModel {

    public MutableLiveData<List<ModelEarningLastTip.Data.YtUserWallet>> liveDataLastTip = new MutableLiveData<>();
    public MutableLiveData<ModelEarningWeek.Data.TotalTipWeek> liveDataWeek = new MutableLiveData<>();
    public MutableLiveData<List<ModelEarningCommission.Data.YtGetDriverCommissionPlanWise>> liveDataCommission = new MutableLiveData<>();

    public void getLastTip(Query query) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response Trip", response.toString());

                    if (response.getData() != null) {

                        Operation.Data data = (Operation.Data) response.getData();
                        String json = OperationDataJsonSerializer.serialize(data, "  ");
                        Gson gson = new Gson();
                        ModelEarningLastTip modelEarningLastTip = gson.fromJson(json, ModelEarningLastTip.class);

                        List<ModelEarningLastTip.Data.YtUserWallet> lastTipList = modelEarningLastTip.getData().getYtUserWallet();

                        liveDataLastTip.postValue(lastTipList);
                    } else {
                        liveDataLastTip.postValue(null);
                    }
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataLastTip.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
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

    public void getEarningBasedOnTime(Query query) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response Based on time", response.toString());

                    if (response.getData() != null) {

                        Operation.Data data = (Operation.Data) response.getData();
                        String json = OperationDataJsonSerializer.serialize(data, "  ");
                        Gson gson = new Gson();
                        ModelEarningWeek modelEarningWeek = gson.fromJson(json, ModelEarningWeek.class);

                        ModelEarningWeek.Data.TotalTipWeek totalTipWeek = modelEarningWeek.getData().getTotalTipWeek();

                        liveDataWeek.postValue(totalTipWeek);
                    } else {
                        liveDataWeek.postValue(null);
                    }
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataWeek.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
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


    public void getTotalCommission(Query query) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response Based on time", response.toString());

                    if (response.getData() != null) {

                        Operation.Data data = (Operation.Data) response.getData();
                        String json = OperationDataJsonSerializer.serialize(data, "  ");
                        Gson gson = new Gson();
                        ModelEarningCommission modelEarningCommission = gson.fromJson(json, ModelEarningCommission.class);

                        List<ModelEarningCommission.Data.YtGetDriverCommissionPlanWise> totalCommission = modelEarningCommission.getData().getYtGetDriverCommissionPlanWise();

                        liveDataCommission.postValue(totalCommission);
                    } else {
                        liveDataCommission.postValue(null);
                    }
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataCommission.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
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
}
