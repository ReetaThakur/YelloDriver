package com.app.yellodriver.model.ModelDocument;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentModel {
    @SerializedName("data")
    @Expose
    private DocumentData data;

    public void setData(DocumentData data) {
        this.data = data;
    }

    public DocumentData getData() {
        return this.data;
    }

    //==================================


    public class DocumentData {
        @SerializedName("yt_driver_doc")
        @Expose
        private List<YtDriverDoc> yt_driverDoc;

        public void setYt_driverDoc(List<YtDriverDoc> yt_driverDoc) {
            this.yt_driverDoc = yt_driverDoc;
        }

        public List<YtDriverDoc> getYt_DriverDoc() {
            return this.yt_driverDoc;
        }

        //==================================

        public class YtDriverDoc {
            /*@SerializedName("doc_type")
            @Expose
            private Doc_type doc_type;*/

            @SerializedName("file_upload_id")
            @Expose
            private String file_upload_id;

            @SerializedName("id")
            @Expose
            private String driverDocId;

            @SerializedName("doc_type_id")
            @Expose
            private String docTypeId;

            @SerializedName("updated_at")
            @Expose
            private String docUpdatedAt;

            public String getDriverDocId() {
                return driverDocId;
            }

            public void setDriverDocId(String driverDocId) {
                this.driverDocId = driverDocId;
            }

            public String getDocTypeId() {
                return docTypeId;
            }

            public void setDocTypeId(String docTypeId) {
                this.docTypeId = docTypeId;
            }

            public String getDocUpdatedAt() {
                return docUpdatedAt;
            }

            public void setDocUpdatedAt(String docUpdatedAt) {
                this.docUpdatedAt = docUpdatedAt;
            }

            public void setFile_upload_id(String file_upload_id) {
                this.file_upload_id = file_upload_id;
            }

            public String getFile_upload_id() {
                return this.file_upload_id;
            }

           /* public void setDoc_type(Doc_type doc_type) {
                this.doc_type = doc_type;
            }

            public Doc_type getDoc_type() {
                return this.doc_type;
            }*/

            //==================================
            /*public class Doc_type {
                @SerializedName("title")
                @Expose
                private String title;

                @SerializedName("description")
                @Expose
                private String description;

                @SerializedName("id")
                @Expose
                private String id;

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getTitle() {
                    return this.title;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getDescription() {
                    return this.description;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return this.id;
                }
            }*/
        }
    }

}

