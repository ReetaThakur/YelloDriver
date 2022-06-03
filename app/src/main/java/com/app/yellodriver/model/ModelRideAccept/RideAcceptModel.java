package com.app.yellodriver.model.ModelRideAccept;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RideAcceptModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

//==================================

    public class Data {
        @SerializedName("AcceptRide")
        @Expose
        private AcceptRide acceptRide;

        public AcceptRide getAcceptRide() {
            return acceptRide;
        }

        public void setAcceptRide(AcceptRide acceptRide) {
            this.acceptRide = acceptRide;
        }

        //==================================
        public class AcceptRide {
            @SerializedName("__typename")
            @Expose
            private String typename;
            @SerializedName("available")
            @Expose
            private Boolean available;
            @SerializedName("driver_user_id")
            @Expose
            private String driverUserId;
            @SerializedName("eta_number")
            @Expose
            private Double etaNumber;
            @SerializedName("eta_unit")
            @Expose
            private String etaUnit;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("is_accepted")
            @Expose
            private Boolean isAccepted;
            @SerializedName("ride_id")
            @Expose
            private String rideId;
            @SerializedName("user_id")
            @Expose
            private String userId;

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public Boolean getAvailable() {
                return available;
            }

            public void setAvailable(Boolean available) {
                this.available = available;
            }

            public String getDriverUserId() {
                return driverUserId;
            }

            public void setDriverUserId(String driverUserId) {
                this.driverUserId = driverUserId;
            }

            public Double getEtaNumber() {
                return etaNumber;
            }

            public void setEtaNumber(Double etaNumber) {
                this.etaNumber = etaNumber;
            }

            public String getEtaUnit() {
                return etaUnit;
            }

            public void setEtaUnit(String etaUnit) {
                this.etaUnit = etaUnit;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Boolean getIsAccepted() {
                return isAccepted;
            }

            public void setIsAccepted(Boolean isAccepted) {
                this.isAccepted = isAccepted;
            }

            public String getRideId() {
                return rideId;
            }

            public void setRideId(String rideId) {
                this.rideId = rideId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}