package com.app.yellodriver.model.ModelUpdateProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.model.ModelProfile.ModelUploadProfilePhoto;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class UpdateProfileViewModel extends ViewModel {
    //this is the data that we will fetch asynchronously 
    public MutableLiveData<UpdateProfileModel.Data.UpdateUser> mutableLiveDataUpdateProfile = new MutableLiveData<>();
    public MutableLiveData<ModelUploadProfilePhoto.Data.UploadProfilePhoto> mutableProfileData = new MutableLiveData<>();

    public void updateProfileData(Mutation mutation) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response", response.toString() != null ? response.toString() : "");
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    UpdateProfileModel updateProfileModel = gson.fromJson(json, UpdateProfileModel.class);

                    UpdateProfileModel.Data.UpdateUser updateYtUser = updateProfileModel.getData().getUpdateUser();

                    mutableLiveDataUpdateProfile.postValue(updateYtUser);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataUpdateProfile.postValue(null);
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

    public void updateProfilePhoto(Mutation mutation) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {
                    YLog.e("Response", response.toString() != null ? response.toString() : "");

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();


                    ModelUploadProfilePhoto modelUploadProfilePhoto = gson.fromJson(json, ModelUploadProfilePhoto.class);

                    ModelUploadProfilePhoto.Data.UploadProfilePhoto uploadProfilePhoto = modelUploadProfilePhoto.getData().getUploadProfilePhoto();

                    mutableProfileData.postValue(uploadProfilePhoto);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                        mutableProfileData.postValue(null);
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