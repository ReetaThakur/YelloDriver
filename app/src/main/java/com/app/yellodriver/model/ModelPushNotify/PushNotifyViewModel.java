package com.app.yellodriver.model.ModelPushNotify;

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
import com.app.yellodriver.model.ModelVehicleStatus.VehicleStatusModel;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PushNotifyViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    public MutableLiveData<DeleteDeviceModel.Data.Delete_yt_push_registration> deleteMutableLivePushNotify = new MutableLiveData<>();
    public MutableLiveData<UpdateDeviceModel.Data.Update_yt_push_registration> updateMutableLivePushNotify = new MutableLiveData<>();
    public MutableLiveData<InsertDeviceModel.Data.Insert_yt_push_registration_one> insertMutableLivePushNotify = new MutableLiveData<>();
    public MutableLiveData<List<FetchDeviceModel.Data.Yt_push_registration>> getMutableLivePushNotify = new MutableLiveData<>();
    public MutableLiveData<List<VehicleStatusModel.Data.Update_yt_vehicle.Returning>> mutableApoloSetStatus = new MutableLiveData<>();


    public void getPushDeviceId(Query query) {

        ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .query(query)
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                        if (!response.hasErrors()) {
                            YLog.e("getPushDeviceId:", response + "");
                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();
                            FetchDeviceModel modelContent = gson.fromJson(json, FetchDeviceModel.class);

                            List<FetchDeviceModel.Data.Yt_push_registration> listContent = modelContent.getData().getYt_push_registration();

                            getMutableLivePushNotify.postValue(listContent);
                        } else {
                            for (Object error : response.getErrors()) {
                                //Could not verify JWT: JWTExpired
                                YLog.e("getPushDeviceId errors:", ((Error) error).getMessage());
                            }
                            getMutableLivePushNotify.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        e.printStackTrace();
                        getMutableLivePushNotify.postValue(null);
                    }
                });
    }

    public void deletePushDeviceId(Mutation mutation) {

        ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .mutate(mutation)
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                        if (!response.hasErrors()) {
                            YLog.e("deletePushDeviceId:", response + "");
                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();
                            DeleteDeviceModel modelContent = gson.fromJson(json, DeleteDeviceModel.class);

                            DeleteDeviceModel.Data.Delete_yt_push_registration listContent = modelContent.getData().getDelete_yt_push_registration();

                            deleteMutableLivePushNotify.postValue(listContent);
                        } else {
                            for (Object error : response.getErrors()) {
                                //Could not verify JWT: JWTExpired
                                YLog.e("deletePushDeviceId errors:", ((Error) error).getMessage());
                            }
                            deleteMutableLivePushNotify.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        e.printStackTrace();
                        deleteMutableLivePushNotify.postValue(null);
                    }
                });
    }

    public void updatePushDeviceId(Mutation mutation) {


        ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .mutate(mutation)
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                        YLog.e("updatePushDeviceId Response", response.toString());
                        if (!response.hasErrors()) {
                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();
                            UpdateDeviceModel modelContent = gson.fromJson(json, UpdateDeviceModel.class);

                            UpdateDeviceModel.Data.Update_yt_push_registration listContent = modelContent.getData().getUpdate_yt_push_registration();

                            updateMutableLivePushNotify.postValue(listContent);
                        } else {
                            for (Object error : response.getErrors()) {
                                //Could not verify JWT: JWTExpired
                                YLog.e("updatePushDeviceId errors:", ((Error) error).getMessage());
                            }
                            updateMutableLivePushNotify.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        e.printStackTrace();
                        updateMutableLivePushNotify.postValue(null);
                    }
                });
    }

    public void insertPushDeviceId(Mutation mutation) {


        ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .mutate(mutation)
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                        YLog.e("insertPushDeviceId Response", response.toString());
                        if (!response.hasErrors()) {
                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();
                            InsertDeviceModel modelContent = gson.fromJson(json, InsertDeviceModel.class);

                            InsertDeviceModel.Data.Insert_yt_push_registration_one listContent = modelContent.getData().getInsert_yt_push_registration_one();

                            insertMutableLivePushNotify.postValue(listContent);
                        } else {
                            for (Object error : response.getErrors()) {
                                //Could not verify JWT: JWTExpired
                                YLog.e("insertPushDeviceId errors:", ((Error) error).getMessage());
                            }
                            insertMutableLivePushNotify.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        e.printStackTrace();
                        insertMutableLivePushNotify.postValue(null);
                    }
                });
    }

    public void setUserStatus(Mutation mutation) {

        ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .mutate(mutation)
                .enqueue(new Callback() {

                    @Override
                    public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                        YLog.e("insertPushDeviceId Response", response.toString());
                        if (!response.hasErrors()) {
                            if (!response.hasErrors()) {
                                YLog.e("Response :", response.toString());
                                Operation.Data data = (Operation.Data) response.getData();
                                String json = OperationDataJsonSerializer.serialize(data, "  ");
                                Gson gson = new Gson();
                                VehicleStatusModel modelContent = gson.fromJson(json, VehicleStatusModel.class);

                                List<VehicleStatusModel.Data.Update_yt_vehicle.Returning> listContent = modelContent.getData().getUpdate_yt_vehicle().getReturning();

                                mutableApoloSetStatus.postValue(listContent);
                            } else {
                                for (Object error : response.getErrors()) {
                                    //Could not verify JWT: JWTExpired
                                    YLog.e("SetVehicleStatus errors:", ((Error) error).getMessage());
                                }
                                mutableApoloSetStatus.postValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        e.printStackTrace();
                        mutableApoloSetStatus.postValue(null);
                    }
                });
    }
}