package com.app.yellodriver.model.ModelNotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

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
        @SerializedName("yt_notification")
        @Expose
        private List<YtNotification> yt_notification;

        public void setYt_notification(List<YtNotification> yt_notification) {
            this.yt_notification = yt_notification;
        }

        public List<YtNotification> getYt_notification() {
            return this.yt_notification;
        }
        //==================================

        public class YtNotification {

            @SerializedName("content")
            @Expose
            private Content content;

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("priority")
            @Expose
            private String priority;

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            @SerializedName("created_at")
            @Expose
            private String created_at;

            public String getDisplayHdrDate() {
                return displayHdrDate;
            }

            public void setDisplayHdrDate(String displayHdrDate) {
                this.displayHdrDate = displayHdrDate;
            }

            private String displayHdrDate="";

            public void setContent(Content content) {
                this.content = content;
            }

            public Content getContent() {
                return this.content;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setPriority(String priority) {
                this.priority = priority;
            }

            public String getPriority() {
                return this.priority;
            }

            public class Content {
                private RideData data;

                private String title;

                private String message;

                public void setRideData(RideData data) {
                    this.data = data;
                }

                public RideData getRideData() {
                    return this.data;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getTitle() {
                    return this.title;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public String getMessage() {
                    return this.message;
                }
                //==================================

                public class End_address
                {
                    private String line1;

                    public void setLine1(String line1){
                        this.line1 = line1;
                    }
                    public String getLine1(){
                        return this.line1;
                    }
                }

                //==================================

                public class Start_address
                {
                    private String line1;

                    public void setLine1(String line1){
                        this.line1 = line1;
                    }
                    public String getLine1(){
                        return this.line1;
                    }
                }

                //==================================
                private class RideData
                {
                    private String ride_id;

                    private String rider_name;

                    private String driver_name;

                    private End_address end_address;

                    private String end_location;

                    private String rider_mobile;

                    private double ride_distance;

                    private Start_address start_address;

                    private String start_location;

                    private String ride_request_id;

                    public void setRide_id(String ride_id){
                        this.ride_id = ride_id;
                    }
                    public String getRide_id(){
                        return this.ride_id;
                    }
                    public void setRider_name(String rider_name){
                        this.rider_name = rider_name;
                    }
                    public String getRider_name(){
                        return this.rider_name;
                    }
                    public void setDriver_name(String driver_name){
                        this.driver_name = driver_name;
                    }
                    public String getDriver_name(){
                        return this.driver_name;
                    }
                    public void setEnd_address(End_address end_address){
                        this.end_address = end_address;
                    }
                    public End_address getEnd_address(){
                        return this.end_address;
                    }
                    public void setEnd_location(String end_location){
                        this.end_location = end_location;
                    }
                    public String getEnd_location(){
                        return this.end_location;
                    }
                    public void setRider_mobile(String rider_mobile){
                        this.rider_mobile = rider_mobile;
                    }
                    public String getRider_mobile(){
                        return this.rider_mobile;
                    }
                    public void setRide_distance(double ride_distance){
                        this.ride_distance = ride_distance;
                    }
                    public double getRide_distance(){
                        return this.ride_distance;
                    }
                    public void setStart_address(Start_address start_address){
                        this.start_address = start_address;
                    }
                    public Start_address getStart_address(){
                        return this.start_address;
                    }
                    public void setStart_location(String start_location){
                        this.start_location = start_location;
                    }
                    public String getStart_location(){
                        return this.start_location;
                    }
                    public void setRide_request_id(String ride_request_id){
                        this.ride_request_id = ride_request_id;
                    }
                    public String getRide_request_id(){
                        return this.ride_request_id;
                    }
                }

            }
        }
    }
}
