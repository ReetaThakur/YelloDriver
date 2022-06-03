package com.app.yellodriver.model.ModelPushNotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//==================================
public class FetchDeviceModel {
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
        @SerializedName("yt_push_registration")
        @Expose
        private List<Yt_push_registration> yt_push_registration;

        public void setYt_push_registration(List<Yt_push_registration> yt_push_registration) {
            this.yt_push_registration = yt_push_registration;
        }

        public List<Yt_push_registration> getYt_push_registration() {
            return this.yt_push_registration;
        }

        public class Yt_push_registration {
            @SerializedName("id")
            @Expose
            private String id;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }
        }

    }

//==================================

}
