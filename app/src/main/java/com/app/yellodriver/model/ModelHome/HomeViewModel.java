package com.app.yellodriver.model.ModelHome;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.MyLifecycleHandler;
import com.app.yellodriver.NotificationTypeEnum;
import com.app.yellodriver.R;
import com.app.yellodriver.RideRequestSubscription;
import com.app.yellodriver.RideStatusSubscription;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.fragment.HomeFragment;
import com.app.yellodriver.model.ModelCancelOptions.ModelCancelOptions;
import com.app.yellodriver.model.ModelDriverArrived.ModelDriverArrived;
import com.app.yellodriver.model.ModelExtendPass.ModelExtendBoardingPass;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.model.ModelRideAccept.RideAcceptModel;
import com.app.yellodriver.model.ModelRideComplete.RideCompleteModel;
import com.app.yellodriver.model.ModelRideRequest.RideRequestModel;
import com.app.yellodriver.model.ModelRideStatus.ModelRideStatus;
import com.app.yellodriver.model.ModelSosRequest.InsertSosRequestModel;
import com.app.yellodriver.model.ModelStripe.ModelCapturePayment;
import com.app.yellodriver.model.ModelStripe.ModelStripeToken;
import com.app.yellodriver.model.ModelUploadRIde.ModelUploadRide;
import com.app.yellodriver.model.ModelUserOnboarding.ModelCreatePayment.ModelCreatePayment;
import com.app.yellodriver.model.ModelUserOnboarding.ModelPurchaseBoardingPass;
import com.app.yellodriver.model.ModelVehicleLocation.ModelVehicleLocation;
import com.app.yellodriver.service.ForegroundService;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.NetworkUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


public class HomeViewModel extends ViewModel {
    //this is the data that we will fetch asynchronously 
    public MutableLiveData<MyProfileModel.Data.YtUser> mutableLiveDataUserProfile = new MutableLiveData<>();
    //public MutableLiveData<List<RideRequestModel.Data.YtRideRequest>> mutableLiveDataRideRequest = new MutableLiveData<>();
    public MutableLiveData<RideAcceptModel.Data.AcceptRide> mutableLiveDataAcceptRide = new MutableLiveData<>();
    public MutableLiveData<RideCompleteModel.Data.CompleteRide> mutableLiveDataCompleteRide = new MutableLiveData<>();
    public MutableLiveData<InsertSosRequestModel.Data.InsertYtSosRequestOne> mutableLiveDataInsertSOS = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ModelVehicleLocation.Data.YtVehicleLocation>> mutableLiveDataVehicleLocation = new MutableLiveData<>();
    public MutableLiveData<ModelUploadRide.Data.UploadRideRouteMap> mutableLiveDataUploadRideRoute = new MutableLiveData<>();

    public MutableLiveData<ModelStripeToken.Data.CreateDeviceToken> liveDataStripeToken = new MutableLiveData<>();
    public MutableLiveData<ModelExtendBoardingPass.Data.ExtendBoardingPass> liveDataExtendsBoardingPass = new MutableLiveData<>();
    public MutableLiveData<ModelCreatePayment.Data.CreatePayment> liveDataCreatePayment = new MutableLiveData<>();
    public MutableLiveData<ModelCapturePayment.Data.CapturePayment> liveDataCapturePayment = new MutableLiveData<>();

    private RideStatusSubscription rideStatusSubscription;
    private ApolloSubscriptionCall<RideStatusSubscription.Data> apoloRideStatus;

    public MutableLiveData<List<ModelRideStatus.Data.YtRide>> mutableLiveDataRideStatus = new MutableLiveData<>();
    public MutableLiveData<List<ModelRideStatus.Data.YtRide>> mutableLiveDataCheckRideStatus = new MutableLiveData<>();

    public MutableLiveData<ModelDriverArrived.Data.VehicleArrived> mutableLiveDataDriverArrived = new MutableLiveData<>();


    public void getProfileData(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    MyProfileModel modelContent = gson.fromJson(json, MyProfileModel.class);

                    MyProfileModel.Data.YtUser listContent = modelContent.getData().getYtUser();

                    mutableLiveDataUserProfile.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("Errors getProfileData:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataUserProfile.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                mutableLiveDataUserProfile.postValue(null);
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

    public void getTripCancelReason(Query query) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelCancelOptions modelCancelOptions = gson.fromJson(json, ModelCancelOptions.class);

                    List<ModelCancelOptions.Data.YtSystemSetting> listYtSystemSetting = modelCancelOptions.getData().getYtSystemSetting();

                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_MODEL_CANCEL_REASON, modelCancelOptions);
//                            mutableLiveDataUserProfile.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("Errors getProfileData:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataUserProfile.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                mutableLiveDataUserProfile.postValue(null);
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

    public void acceptRide(Mutation mutation) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("acceptRide", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    RideAcceptModel rideAcceptModel = gson.fromJson(json, RideAcceptModel.class);

                    RideAcceptModel.Data.AcceptRide acceptRide = rideAcceptModel.getData().getAcceptRide();

                    mutableLiveDataAcceptRide.postValue(acceptRide);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataAcceptRide.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                mutableLiveDataAcceptRide.postValue(null);
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

    public void completeRide(Mutation mutation) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("completeRide", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    RideCompleteModel rideAcceptModel = gson.fromJson(json, RideCompleteModel.class);

                    RideCompleteModel.Data.CompleteRide completeRideResp = rideAcceptModel.getData().getCompleteRide();

                    mutableLiveDataCompleteRide.postValue(completeRideResp);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataCompleteRide.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mutableLiveDataCompleteRide.postValue(null);
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

    public void insertSOS(Mutation mutation) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("sosRequest", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    InsertSosRequestModel insertSosRequestModel = gson.fromJson(json, InsertSosRequestModel.class);

                    InsertSosRequestModel.Data.InsertYtSosRequestOne insertYtSosRequestOne = insertSosRequestModel.getData().getInsertYtSosRequestOne();

                    mutableLiveDataInsertSOS.postValue(insertYtSosRequestOne);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataInsertSOS.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mutableLiveDataAcceptRide.postValue(null);
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

    public void getVehicleLocationByRide(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelVehicleLocation modelVehicleLocation = gson.fromJson(json, ModelVehicleLocation.class);

                    ArrayList<ModelVehicleLocation.Data.YtVehicleLocation> listVehicleLocation = modelVehicleLocation.getData().getYtVehicleLocation();

                    mutableLiveDataVehicleLocation.postValue(listVehicleLocation);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("Errors getProfileData:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataVehicleLocation.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                mutableLiveDataUserProfile.postValue(null);
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

    public void uploadRouteImage(Mutation mutation) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelUploadRide modelUploadRide = gson.fromJson(json, ModelUploadRide.class);

                    ModelUploadRide.Data.UploadRideRouteMap uploadRideRouteMap = modelUploadRide.getData().getUploadRideRouteMap();

                    mutableLiveDataUploadRideRoute.postValue(uploadRideRouteMap);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("Errors getProfileData:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataUploadRideRoute.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                mutableLiveDataUserProfile.postValue(null);
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

    public void extendsBoardingPass(Mutation mutation) {

        ApolloCall.Callback callback = new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("purchaseBoardingPass response", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelExtendBoardingPass modelExtendBoardingPass = gson.fromJson(json, ModelExtendBoardingPass.class);

                    ModelExtendBoardingPass.Data.ExtendBoardingPass extendBoardingPass = modelExtendBoardingPass.getData().getExtendBoardingPass();

                    liveDataExtendsBoardingPass.postValue(extendBoardingPass);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    liveDataExtendsBoardingPass.postValue(null);
                }
            }


            @Override
            public void onFailure(@NotNull ApolloException e) {
                liveDataExtendsBoardingPass.postValue(null);
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


    public void cancelRideStatusSubscription() {
        if (apoloRideStatus != null) {
            apoloRideStatus.cancel();
        }
    }

    public void checkRideStatus(Query query) {

        Callback callback = new Callback() {

            @Override
            public void onResponse(com.apollographql.apollo.api.@NotNull Response response) {
                if (!response.hasErrors()) {
                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelRideStatus modelRideStatus = gson.fromJson(json, ModelRideStatus.class);

                    List<ModelRideStatus.Data.YtRide> listContent = modelRideStatus.getData().getYtRide();

                    mutableLiveDataCheckRideStatus.postValue(listContent);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                    }

                    mutableLiveDataCheckRideStatus.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                e.printStackTrace();
                mutableLiveDataCheckRideStatus.postValue(null);
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

    public void getRideRequestSubscription(String rideId) {

        rideStatusSubscription = new RideStatusSubscription(rideId);

        apoloRideStatus = ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .subscribe(rideStatusSubscription);

        apoloRideStatus
                .execute(new ApolloSubscriptionCall.Callback() {
                    @Override
                    public void onResponse(@NotNull Response response) {
                        YLog.e("SUBSCRIPTION RIDE ", response.toString());

                        if (!response.hasErrors()) {
                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();
                            ModelRideStatus modelRideStatus = gson.fromJson(json, ModelRideStatus.class);

                            List<ModelRideStatus.Data.YtRide> listContent = modelRideStatus.getData().getYtRide();

                            mutableLiveDataRideStatus.postValue(listContent);
                        } else {
                            for (Object error : response.getErrors()) {
                                //Could not verify JWT: JWTExpired
                                YLog.e("All errors:", ((Error) error).getMessage());
                            }

                            mutableLiveDataRideStatus.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        YLog.e("SUBSCRIPTION RIDE ", "Failure" + e);

                        mutableLiveDataRideStatus.postValue(null);
                    }

                    @Override
                    public void onCompleted() {
                        YLog.e("SUBSCRIPTION RIDE ", "OnComplete");
                    }

                    @Override
                    public void onTerminated() {
                        YLog.e("SUBSCRIPTION RIDE ", "OnTerminate");
                    }

                    @Override
                    public void onConnected() {
                        YLog.e("SUBSCRIPTION RIDE ", "onConnected");
                    }
                });
    }

    public void driverArrived(Mutation mutation) {

        Callback callback = new Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                if (!response.hasErrors()) {

                    YLog.e("driverArrived", response.toString());

                    Operation.Data data = (Operation.Data) response.getData();
                    String json = OperationDataJsonSerializer.serialize(data, "  ");
                    Gson gson = new Gson();
                    ModelDriverArrived driverArrived = gson.fromJson(json, ModelDriverArrived.class);

                    ModelDriverArrived.Data.VehicleArrived vehicleArrived = driverArrived.getData().getVehicleArrived();

                    mutableLiveDataDriverArrived.postValue(vehicleArrived);
                } else {
                    for (Object error : response.getErrors()) {
                        //Could not verify JWT: JWTExpired
                        YLog.e("All errors:", ((Error) error).getMessage());
                        Utils.showCommonErrorDialog(((Error) error).getMessage() + "");
                    }
                    mutableLiveDataDriverArrived.postValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mutableLiveDataDriverArrived.postValue(null);
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