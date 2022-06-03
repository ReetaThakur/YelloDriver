package com.app.yellodriver.model.ModelRideStart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class RideStartViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    public MutableLiveData<RideStartModel.Data.StartRide> mutableApoloResponse = new MutableLiveData<>();

    public void sendQRToStartRide(Mutation mutation) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if(!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    RideStartModel modelContent = gson.fromJson(json, RideStartModel.class);

                    RideStartModel.Data.StartRide listContent = modelContent.getData().getStartRide();

                    mutableApoloResponse.postValue(listContent);
                }else{
                    for(Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error)error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableApoloResponse.postValue(null);
                }

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                mutableApoloResponse.postValue(null);
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