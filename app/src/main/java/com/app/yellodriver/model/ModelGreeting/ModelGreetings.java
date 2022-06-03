package com.app.yellodriver.model.ModelGreeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelGreetings {

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

        @SerializedName("yt_user")
        @Expose
        private List<YtUser> ytUser = null;

        @SerializedName("yt_greeting")
        @Expose
        private List<YtGreeting> ytGreeting = null;

        public List<YtGreeting> getYtGreeting() {
            return ytGreeting;
        }

        public void setYtGreeting(List<YtGreeting> ytGreeting) {
            this.ytGreeting = ytGreeting;
        }

        public List<YtUser> getYtUser() {
            return ytUser;
        }

        public void setYtUser(List<YtUser> ytUser) {
            this.ytUser = ytUser;
        }

        public class YtUser {

            @SerializedName("full_name")
            @Expose
            private String fullName;

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

        }

        public class YtGreeting {

            @SerializedName("context")
            @Expose
            private String context;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("message")
            @Expose
            private String message;

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}