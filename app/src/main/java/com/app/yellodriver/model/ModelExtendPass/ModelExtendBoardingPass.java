package com.app.yellodriver.model.ModelExtendPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelExtendBoardingPass {

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

        @SerializedName("ExtendBoardingPass")
        @Expose
        private ExtendBoardingPass extendBoardingPass;

        public ExtendBoardingPass getExtendBoardingPass() {
            return extendBoardingPass;
        }

        public void setExtendBoardingPass(ExtendBoardingPass extendBoardingPass) {
            this.extendBoardingPass = extendBoardingPass;
        }

        public class ExtendBoardingPass {

            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("order_number")
            @Expose
            private Integer orderNumber;
            @SerializedName("order_id")
            @Expose
            private String orderId;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Integer getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(Integer orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }
        }
    }
}
