package com.app.yellodriver.model.ModelTrip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripModel {
    @SerializedName("data")
    @Expose
    private TripData data;

    public void setData(TripData data) {
        this.data = data;
    }

    public TripData getData() {
        return this.data;
    }

    //==================================

    public class TripData {
        @SerializedName("yt_ride")
        @Expose
        private List<YtRide> yt_ride;

        public void setYt_ride(List<YtRide> yt_ride) {
            this.yt_ride = yt_ride;
        }

        public List<YtRide> getYt_ride() {
            return this.yt_ride;
        }


        //==================================

        public class YtRide {
            @SerializedName("start_location")
            @Expose
            private String start_location;

            @SerializedName("start_address")
            @Expose
            private Start_address start_address;

            @SerializedName("end_location")
            @Expose
            private String end_location;

            @SerializedName("end_address")
            @Expose
            private End_address end_address;

            @SerializedName("distance")
            @Expose
            private double distance;

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("created_at")
            @Expose
            private String createdAt;

            @SerializedName("start_at")
            @Expose
            private String start_at;

            @SerializedName("end_at")
            @Expose
            private String end_at;

            @SerializedName("route_map_file_id")
            @Expose
            private String route_map_file_id;


            public void setStart_location(String start_location) {
                this.start_location = start_location;
            }

            public String getStart_location() {
                return this.start_location;
            }

            public void setStart_address(Start_address start_address) {
                this.start_address = start_address;
            }

            public Start_address getStart_address() {
                return this.start_address;
            }

            public void setEnd_location(String end_location) {
                this.end_location = end_location;
            }

            public String getEnd_location() {
                return this.end_location;
            }

            public void setEnd_address(End_address end_address) {
                this.end_address = end_address;
            }

            public End_address getEnd_address() {
                return this.end_address;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public double getDistance() {
                return this.distance;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public String getRoute_map_file_id() {
                return route_map_file_id;
            }

            public void setRoute_map_file_id(String route_map_file_id) {
                this.route_map_file_id = route_map_file_id;
            }

            public void setStart_at(String start_at){
                this.start_at = start_at;
            }
            public String getStart_at(){
                return this.start_at;
            }
            public void setEnd_at(String end_at){
                this.end_at = end_at;
            }
            public String getEnd_at(){
                return this.end_at;
            }

            public class Start_address {
                @SerializedName("line1")
                @Expose
                private String line1;

                public void setLine1(String line1) {
                    this.line1 = line1;
                }

                public String getLine1() {
                    return this.line1;
                }
            }

//==================================

            public class End_address {
                @SerializedName("line1")
                @Expose
                private String line1;

                public void setLine1(String line1) {
                    this.line1 = line1;
                }

                public String getLine1() {
                    return this.line1;
                }
            }
//==================================
        }
    }
}

