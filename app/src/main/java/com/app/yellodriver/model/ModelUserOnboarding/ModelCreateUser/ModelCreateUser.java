package com.app.yellodriver.model.ModelUserOnboarding.ModelCreateUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCreateUser {
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

        @SerializedName("CreateUser")
        @Expose
        private CreateUser createUser;

        public CreateUser getCreateUser() {
            return createUser;
        }

        public void setCreateUser(CreateUser createUser) {
            this.createUser = createUser;
        }

        //==================================
        public class CreateUser {

            @SerializedName("user_id")
            @Expose
            private String user_id;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }
        }
    }
//==================================

}
