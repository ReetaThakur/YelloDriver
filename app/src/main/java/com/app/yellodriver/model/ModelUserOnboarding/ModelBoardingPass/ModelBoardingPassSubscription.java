package com.app.yellodriver.model.ModelUserOnboarding.ModelBoardingPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelBoardingPassSubscription {

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

        @SerializedName("yt_boarding_pass")
        @Expose
        private ArrayList<YtBoardingPas> ytBoardingPass = null;

        public ArrayList<YtBoardingPas> getYtBoardingPass() {
            return ytBoardingPass;
        }

        public void setYtBoardingPass(ArrayList<YtBoardingPas> ytBoardingPass) {
            this.ytBoardingPass = ytBoardingPass;
        }

        public class YtBoardingPas {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("pass_type")
            @Expose
            private String passType;
            @SerializedName("plan_id")
            @Expose
            private String planId;
            @SerializedName("purchased_at")
            @Expose
            private String purchasedAt;
            @SerializedName("plan")
            @Expose
            private Plan plan;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPassType() {
                return passType;
            }

            public void setPassType(String passType) {
                this.passType = passType;
            }

            public String getPlanId() {
                return planId;
            }

            public void setPlanId(String planId) {
                this.planId = planId;
            }

            public String getPurchasedAt() {
                return purchasedAt;
            }

            public void setPurchasedAt(String purchasedAt) {
                this.purchasedAt = purchasedAt;
            }

            public Plan getPlan() {
                return plan;
            }

            public void setPlan(Plan plan) {
                this.plan = plan;
            }

            public class Plan {

                @SerializedName("validity_days")
                @Expose
                private Integer validityDays;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("price")
                @Expose
                private Integer price;
                @SerializedName("title")
                @Expose
                private String title;

                public Integer getValidityDays() {
                    return validityDays;
                }

                public void setValidityDays(Integer validityDays) {
                    this.validityDays = validityDays;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Integer getPrice() {
                    return price;
                }

                public void setPrice(Integer price) {
                    this.price = price;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }
        }
    }
}
