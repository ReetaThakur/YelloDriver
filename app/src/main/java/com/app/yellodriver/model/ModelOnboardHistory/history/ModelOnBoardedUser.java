package com.app.yellodriver.model.ModelOnboardHistory.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelOnBoardedUser {

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

        @SerializedName("yt_user")
        @Expose
        private List<YtUser> ytUser = null;

        public List<YtUser> getYtUser() {
            return ytUser;
        }

        public void setYtUser(List<YtUser> ytUser) {
            this.ytUser = ytUser;
        }

        public class YtUser {

            @SerializedName("full_name")
            @Expose
            private String fullName;
            @SerializedName("country_code")
            @Expose
            private String countryCode;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("boarding_passes")
            @Expose
            private List<BoardingPass> boardingPasses = null;

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public List<BoardingPass> getBoardingPasses() {
                return boardingPasses;
            }

            public void setBoardingPasses(List<BoardingPass> boardingPasses) {
                this.boardingPasses = boardingPasses;
            }

            public class BoardingPass {

                @SerializedName("pass_type")
                @Expose
                private String passType;
                @SerializedName("purchased_at")
                @Expose
                private String purchasedAt;
                @SerializedName("plan")
                @Expose
                private Plan plan;

                public String getPassType() {
                    return passType;
                }

                public void setPassType(String passType) {
                    this.passType = passType;
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

                public class Plan{
                    @SerializedName("title")
                    @Expose
                    private String title;

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
}