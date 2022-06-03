package com.app.yellodriver.model.ModelVehicleDoc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleDocModel {

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

        @SerializedName("yt_vehicle_doc")
        @Expose
        private List<YtVehicleDoc> ytVehicleDoc = null;

        public List<YtVehicleDoc> getYtVehicleDoc() {
            return ytVehicleDoc;
        }

        public void setYtVehicleDoc(List<YtVehicleDoc> ytVehicleDoc) {
            this.ytVehicleDoc = ytVehicleDoc;
        }

        public class YtVehicleDoc {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("doc_type_id")
            @Expose
            private String docTypeId;
            @SerializedName("file_upload_id")
            @Expose
            private String fileUploadId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDocTypeId() {
                return docTypeId;
            }

            public void setDocTypeId(String docTypeId) {
                this.docTypeId = docTypeId;
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
