package com.app.yellodriver.model.ModelTrip;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall.Callback;
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


public class TripsViewModel extends ViewModel {
    //this is the data that we will fetch asynchronously 
    public MutableLiveData<List<TripModel.TripData.YtRide>> mutableLiveDataTrip = new MutableLiveData<>();

    public void getTripData(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response Trip", response.toString());

                    if (response.getData() != null) {

                        Operation.Data data = (Operation.Data) response.getData();
                        String json = OperationDataJsonSerializer.serialize(data, "  ");
                        Gson gson = new Gson();
                        TripModel modelContent = gson.fromJson(json, TripModel.class);

                        List<TripModel.TripData.YtRide> listContent = modelContent.getData().getYt_ride();

                        mutableLiveDataTrip.postValue(listContent);
                    } else {
                        mutableLiveDataTrip.postValue(null);
                    }
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage()+"");
                    }
                    mutableLiveDataTrip.postValue(null);
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