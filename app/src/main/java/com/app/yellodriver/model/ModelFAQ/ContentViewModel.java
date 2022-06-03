package com.app.yellodriver.model.ModelFAQ;

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

public class ContentViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    public MutableLiveData<List<ModelContent.Data.YtContent>> mutableApoloResponseQA = new MutableLiveData<>();
    public MutableLiveData<List<ModelHtmlContent.Data.YtHtmlContent>> mutableApoloResponseHTML = new MutableLiveData<>();

    public void getFAQData(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                YLog.e("Response", response.toString() != null ? response.toString() : "");

                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelContent modelContent = gson.fromJson(json, ModelContent.class);

                    List<ModelContent.Data.YtContent> listContent = modelContent.getData().getYtContent();

                    mutableApoloResponseQA.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage()+"");
                    }
                    mutableApoloResponseQA.postValue(null);
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

    public void getHtmlData(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelHtmlContent modelContent = gson.fromJson(json, ModelHtmlContent.class);

                    List<ModelHtmlContent.Data.YtHtmlContent> listHtmlContent = modelContent.getData().getYtHtmlContents();

                    mutableApoloResponseHTML.postValue(listHtmlContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage()+"");
                    }
                    mutableApoloResponseHTML.postValue(null);
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