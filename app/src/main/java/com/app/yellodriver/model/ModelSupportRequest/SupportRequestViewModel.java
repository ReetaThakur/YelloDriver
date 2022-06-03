package com.app.yellodriver.model.ModelSupportRequest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.model.ModelProfile.ModelUploadProfilePhoto;
import com.app.yellodriver.model.ModelVehicleDoc.VehicleDocModel;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SupportRequestViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    public MutableLiveData<SupportRequestModel.Data.InsertYtSupportRequestOne> mutableInsertRequest = new MutableLiveData<>();
    public MutableLiveData<ModelSupportRequestAttachement.Data.UploadSupportRequestAttachment> mutableUpload = new MutableLiveData<>();
    public MutableLiveData<ModelUpdateSupportRequest.Data.UpdateSupportRequest> mutableUpdateSupport = new MutableLiveData<>();

    public void insertSupportRequest(Mutation mutation) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("Response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    SupportRequestModel modelContent = gson.fromJson(json, SupportRequestModel.class);

                    SupportRequestModel.Data.InsertYtSupportRequestOne contentId = modelContent.getData().getInsertYtSupportRequestOne();

                    mutableInsertRequest.postValue(contentId);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableInsertRequest.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
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

    public void updateSupportRequest(Mutation mutation) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("Response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelUpdateSupportRequest modelContent = gson.fromJson(json, ModelUpdateSupportRequest.class);

                    ModelUpdateSupportRequest.Data.UpdateSupportRequest updateSupportRequest = modelContent.getData().getUpdateSupportRequest();

                    mutableUpdateSupport.postValue(updateSupportRequest);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableUpdateSupport.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
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

    public void uploadSupportRequestAttachment(Mutation mutation) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response", response.toString() != null ? response.toString() : "");

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();

                    ModelSupportRequestAttachement modelSupportRequestAttachement = gson.fromJson(json, ModelSupportRequestAttachement.class);

                    ModelSupportRequestAttachement.Data.UploadSupportRequestAttachment updateSupportRequest = modelSupportRequestAttachement.getData().getUploadSupportRequestAttachment();

                    mutableUpload.postValue(updateSupportRequest);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                        mutableUpload.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                YLog.e("Error", e.toString());
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