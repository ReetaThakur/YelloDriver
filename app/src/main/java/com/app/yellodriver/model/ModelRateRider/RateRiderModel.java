package com.app.yellodriver.model.ModelRateRider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateRiderModel {
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
        @SerializedName("insert_yt_rating_one")
        @Expose
        private Insert_yt_rating_one insert_yt_rating_one;

        public void setInsert_yt_rating_one(Insert_yt_rating_one insert_yt_rating_one) {
            this.insert_yt_rating_one = insert_yt_rating_one;
        }

        public Insert_yt_rating_one getInsert_yt_rating_one() {
            return this.insert_yt_rating_one;
        }

        //==================================
        public class Insert_yt_rating_one {
            @SerializedName("id")
            @Expose
            private String id;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }
        }
    }

//==================================
}
