package com.app.yellodriver.model.ModelEarning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelEarningCommission {

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
        @SerializedName("yt_get_driver_commission_plan_wise")
        @Expose
        private List<YtGetDriverCommissionPlanWise> ytGetDriverCommissionPlanWise = null;

        public List<YtGetDriverCommissionPlanWise> getYtGetDriverCommissionPlanWise() {
            return ytGetDriverCommissionPlanWise;
        }

        public void setYtGetDriverCommissionPlanWise(List<YtGetDriverCommissionPlanWise> ytGetDriverCommissionPlanWise) {
            this.ytGetDriverCommissionPlanWise = ytGetDriverCommissionPlanWise;
        }

        public class YtGetDriverCommissionPlanWise {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("plan_title")
            @Expose
            private String planTitle;
            @SerializedName("total_amount")
            @Expose
            private Double totalAmount;
            @SerializedName("total_user")
            @Expose
            private Integer totalUser;
            @SerializedName("plan_validity_days")
            @Expose
            private Integer planValidityDays;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPlanTitle() {
                return planTitle;
            }

            public void setPlanTitle(String planTitle) {
                this.planTitle = planTitle;
            }

            public Double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(Double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public Integer getTotalUser() {
                return totalUser;
            }

            public void setTotalUser(Integer totalUser) {
                this.totalUser = totalUser;
            }

            public Integer getPlanValidityDays() {
                return planValidityDays;
            }

            public void setPlanValidityDays(Integer planValidityDays) {
                this.planValidityDays = planValidityDays;
            }
        }
    }
}
