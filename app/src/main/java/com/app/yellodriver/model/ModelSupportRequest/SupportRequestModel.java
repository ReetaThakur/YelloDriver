package com.app.yellodriver.model.ModelSupportRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupportRequestModel {

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

        @SerializedName("insert_yt_support_request_one")
        @Expose
        private InsertYtSupportRequestOne insertYtSupportRequestOne;

        public InsertYtSupportRequestOne getInsertYtSupportRequestOne() {
            return insertYtSupportRequestOne;
        }

        public void setInsertYtSupportRequestOne(InsertYtSupportRequestOne insertYtSupportRequestOne) {
            this.insertYtSupportRequestOne = insertYtSupportRequestOne;
        }

        public class InsertYtSupportRequestOne {

            @SerializedName("id")
            @Expose
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

        }
    }
}