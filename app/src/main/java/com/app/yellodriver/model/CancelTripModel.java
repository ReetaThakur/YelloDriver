package com.app.yellodriver.model;

public class CancelTripModel {

    private String canceltripReason;
    private String canceltripId;
    private boolean isSelected;

    public String getCanceltripReason() {
        return canceltripReason;
    }

    public void setCanceltripReason(String canceltripReason) {
        this.canceltripReason = canceltripReason;
    }

    public String getCanceltripId() {
        return canceltripId;
    }

    public void setCanceltripId(String canceltripId) {
        this.canceltripId = canceltripId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}