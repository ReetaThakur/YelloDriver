package com.app.yellodriver.model.ModelUserOnboarding.ModelCreatePayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCreatePayment {

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

        @SerializedName("CreatePayment")
        @Expose
        private CreatePayment createPayment;

        public CreatePayment getCreatePayment() {
            return createPayment;
        }

        public void setCreatePayment(CreatePayment createPayment) {
            this.createPayment = createPayment;
        }

        public class CreatePayment {

            @SerializedName("client_secret")
            @Expose
            private String clientSecret;
            @SerializedName("order_id")
            @Expose
            private String orderId;
            @SerializedName("payment_id")
            @Expose
            private String paymentId;
            @SerializedName("transaction_id")
            @Expose
            private String transactionId;
            @SerializedName("transaction_status")
            @Expose
            private String transactionStatus;

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getPaymentId() {
                return paymentId;
            }

            public void setPaymentId(String paymentId) {
                this.paymentId = paymentId;
            }

            public String getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
            }

            public String getTransactionStatus() {
                return transactionStatus;
            }

            public void setTransactionStatus(String transactionStatus) {
                this.transactionStatus = transactionStatus;
            }
        }
    }
}