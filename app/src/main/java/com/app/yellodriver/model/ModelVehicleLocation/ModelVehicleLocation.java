package com.app.yellodriver.model.ModelVehicleLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelVehicleLocation {

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

        @SerializedName("yt_vehicle_location")
        @Expose
        private ArrayList<YtVehicleLocation> ytVehicleLocation = null;

        public ArrayList<YtVehicleLocation> getYtVehicleLocation() {
            return ytVehicleLocation;
        }

        public void setYtVehicleLocation(ArrayList<YtVehicleLocation> ytVehicleLocation) {
            this.ytVehicleLocation = ytVehicleLocation;
        }

        public class YtVehicleLocation {

            @SerializedName("location")
            @Expose
            private String location;

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

        }
    }
}