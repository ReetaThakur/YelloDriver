package com.app.yellodriver.model.ModelRideStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRideStatus {

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

        @SerializedName("yt_ride")
        @Expose
        private List<YtRide> ytRide = null;

        public List<YtRide> getYtRide() {
            return ytRide;
        }

        public void setYtRide(List<YtRide> ytRide) {
            this.ytRide = ytRide;
        }

        public class YtRide {

            @SerializedName("status")
            @Expose
            private String status;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

}
