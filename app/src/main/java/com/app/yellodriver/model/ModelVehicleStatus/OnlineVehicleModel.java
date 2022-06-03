package com.app.yellodriver.model.ModelVehicleStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnlineVehicleModel {
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
        @SerializedName("yt_vehicle")
        @Expose
        private List<YtVehicle> yt_vehicle;

        public void setYt_vehicle(List<YtVehicle> yt_vehicle) {
            this.yt_vehicle = yt_vehicle;
        }

        public List<YtVehicle> getYt_vehicle() {
            return this.yt_vehicle;
        }

        public class YtVehicle {
            @SerializedName("online")
            @Expose
            private boolean online;

            @SerializedName("id")
            @Expose
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setOnline(boolean online) {
                this.online = online;
            }

            public boolean getOnline() {
                return this.online;
            }
        }
    //==================================
    }

}
