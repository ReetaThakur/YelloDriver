package com.app.yellodriver.model.ModelDocType;

import java.util.List;
public class DocTypeModel {
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }
    //==================================
    public class Data {
        private List<Yt_doc_type> yt_doc_type;

        public void setYt_doc_type(List<Yt_doc_type> yt_doc_type) {
            this.yt_doc_type = yt_doc_type;
        }

        public List<Yt_doc_type> getYt_doc_type() {
            return this.yt_doc_type;
        }

        //==================================
        public class Yt_doc_type {
            private String id;

            private String title;

            private String description;
            private String fileUploadID;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

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

            public String getFileUploadID() {
                return fileUploadID;
            }

            public void setFileUploadID(String fileUploadID) {
                this.fileUploadID = fileUploadID;
            }
        }
        //==================================
    }
}