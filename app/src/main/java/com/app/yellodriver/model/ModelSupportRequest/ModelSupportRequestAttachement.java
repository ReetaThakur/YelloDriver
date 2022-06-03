package com.app.yellodriver.model.ModelSupportRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSupportRequestAttachement {

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

        @SerializedName("UploadSupportRequestAttachment")
        @Expose
        private UploadSupportRequestAttachment uploadSupportRequestAttachment;

        public UploadSupportRequestAttachment getUploadSupportRequestAttachment() {
            return uploadSupportRequestAttachment;
        }

        public void setUploadSupportRequestAttachment(UploadSupportRequestAttachment uploadSupportRequestAttachment) {
            this.uploadSupportRequestAttachment = uploadSupportRequestAttachment;
        }

        public class UploadSupportRequestAttachment {

            @SerializedName("upload_url")
            @Expose
            private String uploadUrl;
            @SerializedName("view_url")
            @Expose
            private String viewUrl;
            @SerializedName("file_upload_id")
            @Expose
            private String fileUploadId;

            public String getUploadUrl() {
                return uploadUrl;
            }

            public void setUploadUrl(String uploadUrl) {
                this.uploadUrl = uploadUrl;
            }

            public String getViewUrl() {
                return viewUrl;
            }

            public void setViewUrl(String viewUrl) {
                this.viewUrl = viewUrl;
            }

            public String getFileUploadId() {
                return fileUploadId;
            }

            public void setFileUploadId(String fileUploadId) {
                this.fileUploadId = fileUploadId;
            }

        }
    }
}