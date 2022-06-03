package com.app.yellodriver.model;

public class HelpReasonModel {

    private String helpReason;

    public String getHelpReason() {
        return helpReason;
    }

    public void setHelpReason(String helpReason) {
        this.helpReason = helpReason;
    }

    public String getHelpReasonId() {
        return helpReasonId;
    }

    public void setHelpReasonId(String helpReasonId) {
        this.helpReasonId = helpReasonId;
    }

    private String helpReasonId;
    private boolean isSelected;



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}