package com.app.yellodriver.model.ModelUserOnboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPurchaseBoardingPass {

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

        @SerializedName("PurchaseBoardingPass")
        @Expose
        private PurchaseBoardingPass purchaseBoardingPass;

        public PurchaseBoardingPass getPurchaseBoardingPass() {
            return purchaseBoardingPass;
        }

        public void setPurchaseBoardingPass(PurchaseBoardingPass purchaseBoardingPass) {
            this.purchaseBoardingPass = purchaseBoardingPass;
        }

        public class PurchaseBoardingPass {

            @SerializedName("order_id")
            @Expose
            private String orderId;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("order_number")
            @Expose
            private Integer orderNumber;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

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

        }
    }
}