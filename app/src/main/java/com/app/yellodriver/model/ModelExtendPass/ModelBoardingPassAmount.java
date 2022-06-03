package com.app.yellodriver.model.ModelExtendPass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBoardingPassAmount {

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

        @SerializedName("yt_boarding_pass_by_pk")
        @Expose
        private YtBoardingPassByPk ytBoardingPassByPk;

        public YtBoardingPassByPk getYtBoardingPassByPk() {
            return ytBoardingPassByPk;
        }

        public void setYtBoardingPassByPk(YtBoardingPassByPk ytBoardingPassByPk) {
            this.ytBoardingPassByPk = ytBoardingPassByPk;
        }

        public class YtBoardingPassByPk {

            @SerializedName("plan")
            @Expose
            private Plan plan;

            public Plan getPlan() {
                return plan;
            }

            public void setPlan(Plan plan) {
                this.plan = plan;
            }

            public class Plan {

                @SerializedName("extension_price_1_day")
                @Expose
                private Integer extensionPrice1Day;
                @SerializedName("extension_price_2_day")
                @Expose
                private Integer extensionPrice2Day;
                @SerializedName("extension_price_3_day")
                @Expose
                private Integer extensionPrice3Day;

                public Integer getExtensionPrice1Day() {
                    return extensionPrice1Day;
                }

                public void setExtensionPrice1Day(Integer extensionPrice1Day) {
                    this.extensionPrice1Day = extensionPrice1Day;
                }

                public Integer getExtensionPrice2Day() {
                    return extensionPrice2Day;
                }

                public void setExtensionPrice2Day(Integer extensionPrice2Day) {
                    this.extensionPrice2Day = extensionPrice2Day;
                }

                public Integer getExtensionPrice3Day() {
                    return extensionPrice3Day;
                }

                public void setExtensionPrice3Day(Integer extensionPrice3Day) {
                    this.extensionPrice3Day = extensionPrice3Day;
                }
            }
        }
    }
}
