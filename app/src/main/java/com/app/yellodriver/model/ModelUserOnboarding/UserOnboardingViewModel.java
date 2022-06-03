package com.app.yellodriver.model.ModelUserOnboarding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.BoardingPassSubscription;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.model.ModelRideRequest.RideRequestModel;
import com.app.yellodriver.model.ModelUserOnboarding.ModelBoardingPass.ModelBoardingPassSubscription;
import com.app.yellodriver.model.ModelUserOnboarding.ModelCreateUser.ModelCreateUser;
import com.app.yellodriver.model.ModelUserOnboarding.ModelPlan.ModelPlan;
import com.app.yellodriver.model.ModelUserOnboarding.ModelSendOtp.ModelSendOtp;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class UserOnboardingViewModel extends ViewModel {

    public MutableLiveData<ModelSendOtp.Data.SendOtp> liveDataSendOtp = new MutableLiveData<>();
    public MutableLiveData<ModelCreateUser.Data.CreateUser> liveDataCreateUser = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ModelPlan.Data.YtPlan>> liveDataPlan = new MutableLiveData<>();
    public MutableLiveData<ModelSendBoardingPass.Data.SendBoardingPass> liveDataSendPass = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ModelBoardingPassSubscription.Data.YtBoardingPas>> liveDataBoardingPass = new MutableLiveData<>();

    public BoardingPassSubscription boardingPassSubscription;
    public ApolloSubscriptionCall<BoardingPassSubscription.Data> apoloBoardingPassSubscription;

    public void sendOtp(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("sendOtp response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelSendOtp modelSendOtp = gson.fromJson(json, ModelSendOtp.class);

                    ModelSendOtp.Data.SendOtp sendOtp = modelSendOtp.getData().getSendOtp();

                    liveDataSendOtp.postValue(sendOtp);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataSendOtp.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataSendOtp.postValue(null);
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


    public void createUser(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("CreateUser response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelCreateUser modelCreateUser = gson.fromJson(json, ModelCreateUser.class);

                    ModelCreateUser.Data.CreateUser createUser = modelCreateUser.getData().getCreateUser();

                    liveDataCreateUser.postValue(createUser);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataCreateUser.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataCreateUser.postValue(null);
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

    public void getPlanList(Query query) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("getPlanList response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelPlan modelPlan = gson.fromJson(json, ModelPlan.class);

                    ArrayList<ModelPlan.Data.YtPlan> plan = modelPlan.getData().getYtPlan();

                    liveDataPlan.postValue(plan);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataPlan.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataPlan.postValue(null);
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

    public void sendBoardingPass(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("CreateUser response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelSendBoardingPass modelSendBoardingPass = gson.fromJson(json, ModelSendBoardingPass.class);

                    ModelSendBoardingPass.Data.SendBoardingPass sendBoardingPass = modelSendBoardingPass.getData().getSendBoardingPass();

                    liveDataSendPass.postValue(sendBoardingPass);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataSendPass.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataSendPass.postValue(null);
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

    public void cancelSubscription() {
        if (apoloBoardingPassSubscription != null) {
            apoloBoardingPassSubscription.cancel();
        }
    }

    public void getBoardingPassSubscription() {

        apoloBoardingPassSubscription = ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .subscribe(boardingPassSubscription);

        apoloBoardingPassSubscription
                .execute(new ApolloSubscriptionCall.Callback() {
                    @Override
                    public void onResponse(@NotNull Response response) {
                        YLog.e("SUBSCRIPTION", response.toString());

                        if (!response.hasErrors()) {
                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();
                            ModelBoardingPassSubscription modelBoardingPassSubscription = gson.fromJson(json, ModelBoardingPassSubscription.class);

                            ArrayList<ModelBoardingPassSubscription.Data.YtBoardingPas> listContent = modelBoardingPassSubscription.getData().getYtBoardingPass();
                            liveDataBoardingPass.postValue(listContent);
                        } else {
                            for (Object error : response.getErrors()) {
                                //Could not verify JWT: JWTExpired
                                YLog.e("All errors:", ((Error) error).getMessage());
                            }
                            liveDataBoardingPass.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        YLog.e("SUBSCRIPTION", "Failure" + e);
                        liveDataBoardingPass.postValue(null);
                    }

                    @Override
                    public void onCompleted() {
                        YLog.e("SUBSCRIPTION", "OnComplete");
                    }

                    @Override
                    public void onTerminated() {
                        YLog.e("SUBSCRIPTION", "OnTerminate");
                    }

                    @Override
                    public void onConnected() {
                        YLog.e("SUBSCRIPTION", "onConnected");
                    }
                });
    }
}