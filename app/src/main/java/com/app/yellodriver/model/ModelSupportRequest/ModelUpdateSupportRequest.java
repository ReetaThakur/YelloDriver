package com.app.yellodriver.model.ModelSupportRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUpdateSupportRequest {

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

        @SerializedName("UpdateSupportRequest")
        @Expose
        private UpdateSupportRequest updateSupportRequest;

        public UpdateSupportRequest getUpdateSupportRequest() {
            return updateSupportRequest;
        }

        public void setUpdateSupportRequest(UpdateSupportRequest updateSupportRequest) {
            this.updateSupportRequest = updateSupportRequest;
        }

        public class UpdateSupportRequest {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("status")
            @Expose
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}