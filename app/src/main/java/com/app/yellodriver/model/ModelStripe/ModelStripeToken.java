package com.app.yellodriver.model.ModelStripe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelStripeToken {

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

        @SerializedName("CreateDeviceToken")
        @Expose
        private CreateDeviceToken createDeviceToken;

        public CreateDeviceToken getCreateDeviceToken() {
            return createDeviceToken;
        }

        public void setCreateDeviceToken(CreateDeviceToken createDeviceToken) {
            this.createDeviceToken = createDeviceToken;
        }

        public class CreateDeviceToken {

            @SerializedName("secret")
            @Expose
            private String secret;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }
        }
    }
}
