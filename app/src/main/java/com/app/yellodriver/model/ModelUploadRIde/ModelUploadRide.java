package com.app.yellodriver.model.ModelUploadRIde;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUploadRide {

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

        @SerializedName("UploadRideRouteMap")
        @Expose
        private UploadRideRouteMap uploadRideRouteMap;

        public UploadRideRouteMap getUploadRideRouteMap() {
            return uploadRideRouteMap;
        }

        public void setUploadRideRouteMap(UploadRideRouteMap uploadRideRouteMap) {
            this.uploadRideRouteMap = uploadRideRouteMap;
        }

        public class UploadRideRouteMap {

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