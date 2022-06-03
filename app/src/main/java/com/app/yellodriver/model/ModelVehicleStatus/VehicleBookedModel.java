package com.app.yellodriver.model.ModelVehicleStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleBookedModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    //==================================
    public class Data {
        @SerializedName("update_yt_vehicle_by_pk")
        @Expose
        private Update_yt_vehicle_by_pk update_yt_vehicle_by_pk;

        public void setUpdate_yt_vehicle_by_pk(Update_yt_vehicle_by_pk update_yt_vehicle_by_pk) {
            this.update_yt_vehicle_by_pk = update_yt_vehicle_by_pk;
        }

        public Update_yt_vehicle_by_pk getUpdate_yt_vehicle_by_pk() {
            return this.update_yt_vehicle_by_pk;
        }
        //==================================
        public class Update_yt_vehicle_by_pk {
            @SerializedName("booked")
            @Expose
            private boolean booked;

            public void setBooked(boolean booked) {
                this.booked = booked;
            }

            public boolean getBooked() {
                return this.booked;
            }
        }
    }
}
