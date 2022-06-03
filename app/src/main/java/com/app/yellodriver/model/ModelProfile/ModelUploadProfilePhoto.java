package com.app.yellodriver.model.ModelProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUploadProfilePhoto {

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

        @SerializedName("UploadProfilePhoto")
        @Expose
        private UploadProfilePhoto uploadProfilePhoto;

        public UploadProfilePhoto getUploadProfilePhoto() {
            return uploadProfilePhoto;
        }

        public void setUploadProfilePhoto(UploadProfilePhoto uploadProfilePhoto) {
            this.uploadProfilePhoto = uploadProfilePhoto;
        }

        public class UploadProfilePhoto {

            @SerializedName("__typename")
            @Expose
            private String typename;
            @SerializedName("upload_url")
            @Expose
            private String uploadUrl;
            @SerializedName("view_url")
            @Expose
            private String viewUrl;
            @SerializedName("file_upload_id")
            @Expose
            private String fileUploadId;

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

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