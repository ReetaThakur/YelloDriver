package com.app.yellodriver.model.ModelUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUser {

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

        @SerializedName("yt_user_by_pk")
        @Expose
        private YtUserByPk ytUserByPk;

        public YtUserByPk getYtUserByPk() {
            return ytUserByPk;
        }

        public void setYtUserByPk(YtUserByPk ytUserByPk) {
            this.ytUserByPk = ytUserByPk;
        }

        public class YtUserByPk {

            @SerializedName("active")
            @Expose
            private Boolean active;
            @SerializedName("block")
            @Expose
            private Boolean block;

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public Boolean getBlock() {
                return block;
            }

            public void setBlock(Boolean block) {
                this.block = block;
            }

        }
    }
}