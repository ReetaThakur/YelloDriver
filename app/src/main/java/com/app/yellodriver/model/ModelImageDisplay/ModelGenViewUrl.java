package com.app.yellodriver.model.ModelImageDisplay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelGenViewUrl {

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

        @SerializedName("GenViewUrl")
        @Expose
        private ArrayList<GenViewUrl> genViewUrl = null;

        public ArrayList<GenViewUrl> getGenViewUrl() {
            return genViewUrl;
        }

        public void setGenViewUrl(ArrayList<GenViewUrl> genViewUrl) {
            this.genViewUrl = genViewUrl;
        }

        public class GenViewUrl {

            @SerializedName("__typename")
            @Expose
            private String typename;
            @SerializedName("url")
            @Expose
            private String url;
            @SerializedName("expires_at")
            @Expose
            private String expiresAt;
            @SerializedName("upload_id")
            @Expose
            private String uploadId;

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getExpiresAt() {
                return expiresAt;
            }

            public void setExpiresAt(String expiresAt) {
                this.expiresAt = expiresAt;
            }

            public String getUploadId() {
                return uploadId;
            }

            public void setUploadId(String uploadId) {
                this.uploadId = uploadId;
            }

        }
    }
}
