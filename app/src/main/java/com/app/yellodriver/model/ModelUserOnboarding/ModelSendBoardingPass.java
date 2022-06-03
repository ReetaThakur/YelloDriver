package com.app.yellodriver.model.ModelUserOnboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSendBoardingPass {

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

        @SerializedName("SendBoardingPass")
        @Expose
        private SendBoardingPass sendBoardingPass;

        public SendBoardingPass getSendBoardingPass() {
            return sendBoardingPass;
        }

        public void setSendBoardingPass(SendBoardingPass sendBoardingPass) {
            this.sendBoardingPass = sendBoardingPass;
        }

        public class SendBoardingPass {

            @SerializedName("boarding_pass_id")
            @Expose
            private String boardingPassId;
            @SerializedName("status")
            @Expose
            private String status;

            public String getBoardingPassId() {
                return boardingPassId;
            }

            public void setBoardingPassId(String boardingPassId) {
                this.boardingPassId = boardingPassId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
