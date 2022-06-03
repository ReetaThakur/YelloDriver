package com.app.yellodriver.model;

public class StoredRiderInfo {

    String riderId = "";
    String riderName = "";
    String riderRating = "";
    String riderImage = "";
    String riderMobile = "";
    String boardingPassId = "";

    public StoredRiderInfo(String Id, String name, String rating, String imageUrl, String mobile, String boardingPassId) {
        riderId = Id;
        riderName = name;
        riderRating = rating;
        riderImage = imageUrl;
        riderMobile = mobile;
        this.boardingPassId = boardingPassId;
    }

    public StoredRiderInfo() {

    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderRating() {
        return riderRating;
    }

    public void setRiderRating(String riderRating) {
        this.riderRating = riderRating;
    }

    public String getRiderImage() {
        return riderImage;
    }

    public void setRiderImage(String riderImage) {
        this.riderImage = riderImage;
    }

    public String getRiderMobile() {
        return riderMobile;
    }

    public void setRiderMobile(String riderMobile) {
        this.riderMobile = riderMobile;
    }

    public String getBoardingPassId() {
        return boardingPassId;
    }

    public void setBoardingPassId(String boardingPassId) {
        this.boardingPassId = boardingPassId;
    }
}
