package com.app.yellodriver.model.ModelVehicleStatus;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleStatusModel {

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
        @SerializedName("update_yt_vehicle")
        @Expose
        private Update_yt_vehicle update_yt_vehicle;

        public void setUpdate_yt_vehicle(Update_yt_vehicle update_yt_vehicle) {
            this.update_yt_vehicle = update_yt_vehicle;
        }

        public Update_yt_vehicle getUpdate_yt_vehicle() {
            return this.update_yt_vehicle;
        }


        //==================================

        public class Update_yt_vehicle {
            @SerializedName("returning")
            @Expose
            private List<Returning> returning;

            public void setReturning(List<Returning> returning) {
                this.returning = returning;
            }

            public List<Returning> getReturning() {
                return this.returning;
            }

            //==================================
            public class Returning {
                @SerializedName("online")
                @Expose
                private boolean online;

                public void setOnline(boolean online) {
                    this.online = online;
                }

                public boolean getOnline() {
                    return this.online;
                }
            }


        }
    }
//==================================


}