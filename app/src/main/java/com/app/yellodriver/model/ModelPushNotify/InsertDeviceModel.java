package com.app.yellodriver.model.ModelPushNotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertDeviceModel {
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

        @SerializedName("insert_yt_push_registration_one")
        @Expose
        private Insert_yt_push_registration_one insert_yt_push_registration_one;

        public void setInsert_yt_push_registration_one(Insert_yt_push_registration_one insert_yt_push_registration_one) {
            this.insert_yt_push_registration_one = insert_yt_push_registration_one;
        }

        public Insert_yt_push_registration_one getInsert_yt_push_registration_one() {
            return this.insert_yt_push_registration_one;
        }

        //==================================
        public class Insert_yt_push_registration_one {

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
