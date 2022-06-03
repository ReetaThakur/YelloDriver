package com.app.yellodriver.model.ModelStripe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.model.ModelExtendPass.ModelExtendBoardingPass;
import com.app.yellodriver.model.ModelUserOnboarding.ModelCreatePayment.ModelCreatePayment;
import com.app.yellodriver.model.ModelUserOnboarding.ModelPurchaseBoardingPass;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class StripeViewModel extends ViewModel {

    public MutableLiveData<ModelStripeToken.Data.CreateDeviceToken> liveDataStripeToken = new MutableLiveData<>();
    public MutableLiveData<ModelPurchaseBoardingPass.Data.PurchaseBoardingPass> liveDataPurchaseBoardingPass = new MutableLiveData<>();
    public MutableLiveData<ModelCreatePayment.Data.CreatePayment> liveDataCreatePayment = new MutableLiveData<>();
    public MutableLiveData<ModelCapturePayment.Data.CapturePayment> liveDataCapturePayment = new MutableLiveData<>();

    public void createStripeToken(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("createStripeToken response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelStripeToken modelStripeToken = gson.fromJson(json, ModelStripeToken.class);

                    ModelStripeToken.Data.CreateDeviceToken createDeviceToken = modelStripeToken.getData().getCreateDeviceToken();

                    liveDataStripeToken.postValue(createDeviceToken);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataStripeToken.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataStripeToken.postValue(null);
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

    public void purchaseBoardingPass(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("purchaseBoardingPass response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelPurchaseBoardingPass modelPurchaseBoardingPass = gson.fromJson(json, ModelPurchaseBoardingPass.class);

                    ModelPurchaseBoardingPass.Data.PurchaseBoardingPass purchaseBoardingPass = modelPurchaseBoardingPass.getData().getPurchaseBoardingPass();

                    liveDataPurchaseBoardingPass.postValue(purchaseBoardingPass);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataPurchaseBoardingPass.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataPurchaseBoardingPass.postValue(null);
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

    public void createPayment(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("createPayment response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelCreatePayment modelCreatePayment = gson.fromJson(json, ModelCreatePayment.class);

                    ModelCreatePayment.Data.CreatePayment createPayment = modelCreatePayment.getData().getCreatePayment();

                    liveDataCreatePayment.postValue(createPayment);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataCreatePayment.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataCreatePayment.postValue(null);
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

    public void capturePayment(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("createStripeToken response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelCapturePayment modelCapturePayment = gson.fromJson(json, ModelCapturePayment.class);

                    ModelCapturePayment.Data.CapturePayment capturePayment = modelCapturePayment.getData().getCapturePayment();

                    liveDataCapturePayment.postValue(capturePayment);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataCapturePayment.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataCapturePayment.postValue(null);
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