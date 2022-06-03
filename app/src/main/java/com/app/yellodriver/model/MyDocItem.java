package com.app.yellodriver.model;

public class MyDocItem {
    private String description;
    private String imgId;

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    private String docTypeId;
    private String fileUploadID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public MyDocItem() {

    }

    public String getFileUploadID() {
        return fileUploadID;
    }

    public void setFileUploadID(String fileUploadID) {
        this.fileUploadID = fileUploadID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}  