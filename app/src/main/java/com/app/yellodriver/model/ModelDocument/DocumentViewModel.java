package com.app.yellodriver.model.ModelDocument;

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

public class DocumentViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    public MutableLiveData<List<DocumentModel.DocumentData.YtDriverDoc>> mutableLiveDataDocList = new MutableLiveData<>();

    public void getDocumentsList(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("Response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    DocumentModel modelContent = gson.fromJson(json, DocumentModel.class);

                    List<DocumentModel.DocumentData.YtDriverDoc> listContent = modelContent.getData().getYt_DriverDoc();

                    mutableLiveDataDocList.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataDocList.postValue(null);
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