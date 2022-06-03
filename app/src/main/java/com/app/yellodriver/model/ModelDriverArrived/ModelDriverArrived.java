package com.app.yellodriver.model.ModelDriverArrived;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDriverArrived {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("VehicleArrived")
        @Expose
        private VehicleArrived vehicleArrived;

        public VehicleArrived getVehicleArrived() {
            return vehicleArrived;
        }

        public void setVehicleArrived(VehicleArrived vehicleArrived) {
            this.vehicleArrived = vehicleArrived;
        }

        public class VehicleArrived {

            @SerializedName("ride_id")
            @Expose
            private String rideId;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("vehicle_arrived_at")
            @Expose
            private String vehicleArrivedAt;

            public String getRideId() {
                return rideId;
            }

            public void setRideId(String rideId) {
                this.rideId = rideId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getVehicleArrivedAt() {
                return vehicleArrivedAt;
            }

            public void setVehicleArrivedAt(String vehicleArrivedAt) {
                this.vehicleArrivedAt = vehicleArrivedAt;
            }
        }
    }
}
