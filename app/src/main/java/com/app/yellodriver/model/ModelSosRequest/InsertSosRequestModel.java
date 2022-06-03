package com.app.yellodriver.model.ModelSosRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertSosRequestModel {

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

        @SerializedName("insert_yt_sos_request_one")
        @Expose
        private InsertYtSosRequestOne insertYtSosRequestOne;

        public InsertYtSosRequestOne getInsertYtSosRequestOne() {
            return insertYtSosRequestOne;
        }

        public void setInsertYtSosRequestOne(InsertYtSosRequestOne insertYtSosRequestOne) {
            this.insertYtSosRequestOne = insertYtSosRequestOne;
        }

        public class InsertYtSosRequestOne {

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
