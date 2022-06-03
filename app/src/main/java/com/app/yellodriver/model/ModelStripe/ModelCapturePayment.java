package com.app.yellodriver.model.ModelStripe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//==================================
public class ModelCapturePayment {
    @SerializedName("data")
    @Expose
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    //==================================

    public class Data {
        @SerializedName("CapturePayment")
        @Expose
        private CapturePayment capturePayment;

        public CapturePayment getCapturePayment() {
            return capturePayment;
        }

        public void setCapturePayment(CapturePayment capturePayment) {
            this.capturePayment = capturePayment;
        }

        public class CapturePayment {
            @SerializedName("status")
            @Expose
            private String status;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
//==================================
}