package com.app.yellodriver.model.ModelOnboardHistory;

public class OnoboardHistoryModel {
    String name;
    String phone;
    String date;
    String passType;
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public OnoboardHistoryModel() {
    }

    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }
}
