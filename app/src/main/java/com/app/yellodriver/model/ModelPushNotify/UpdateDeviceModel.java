package com.app.yellodriver.model.ModelPushNotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDeviceModel {
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

        @SerializedName("update_yt_push_registration")
        @Expose
        private Update_yt_push_registration update_yt_push_registration;

        public void setUpdate_yt_push_registration(Update_yt_push_registration insert_yt_push_registration_one) {
            this.update_yt_push_registration = insert_yt_push_registration_one;
        }

        public Update_yt_push_registration getUpdate_yt_push_registration() {
            return this.update_yt_push_registration;
        }

        //==================================
        public class Update_yt_push_registration {

            @SerializedName("affected_rows")
            @Expose
            private int affected_rows;

            public void setAffected_rows(int affected_rows){
                this.affected_rows = affected_rows;
            }
            public int getAffected_rows(){
                return this.affected_rows;
            }
        }
    }
//==================================

}
