package com.app.yellodriver.model.ModelUserOnboarding.ModelPlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelPlan {

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

        @SerializedName("yt_plan")
        @Expose
        private ArrayList<YtPlan> ytPlan = null;

        public ArrayList<YtPlan> getYtPlan() {
            return ytPlan;
        }

        public void setYtPlan(ArrayList<YtPlan> ytPlan) {
            this.ytPlan = ytPlan;
        }


        public class YtPlan {

            @SerializedName("active")
            @Expose
            private Boolean active;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("extension_price_1_day")
            @Expose
            private Integer extensionPrice1Day;
            @SerializedName("extension_price_2_day")
            @Expose
            private Integer extensionPrice2Day;
            @SerializedName("extension_price_3_day")
            @Expose
            private Integer extensionPrice3Day;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("only_airport_service")
            @Expose
            private Boolean onlyAirportService;
            @SerializedName("price")
            @Expose
            private Integer price;
            @SerializedName("sales_commission")
            @Expose
            private Object salesCommission;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("total_daily_trips")
            @Expose
            private Object totalDailyTrips;
            @SerializedName("total_trips")
            @Expose
            private Integer totalTrips;
            @SerializedName("unlimited_trips")
            @Expose
            private Boolean unlimitedTrips;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("validity_days")
            @Expose
            private String validityDays;
            @SerializedName("isSelected")
            @Expose
            private boolean isSelected;

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Boolean getOnlyAirportService() {
                return onlyAirportService;
            }

            public void setOnlyAirportService(Boolean onlyAirportService) {
                this.onlyAirportService = onlyAirportService;
            }

            public Integer getPrice() {
                return price;
            }

            public void setPrice(Integer price) {
                this.price = price;
            }

            public Object getSalesCommission() {
                return salesCommission;
            }

            public void setSalesCommission(Object salesCommission) {
                this.salesCommission = salesCommission;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getTotalDailyTrips() {
                return totalDailyTrips;
            }

            public void setTotalDailyTrips(Object totalDailyTrips) {
                this.totalDailyTrips = totalDailyTrips;
            }

            public Integer getTotalTrips() {
                return totalTrips;
            }

            public void setTotalTrips(Integer totalTrips) {
                this.totalTrips = totalTrips;
            }

            public Boolean getUnlimitedTrips() {
                return unlimitedTrips;
            }

            public void setUnlimitedTrips(Boolean unlimitedTrips) {
                this.unlimitedTrips = unlimitedTrips;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getValidityDays() {
                return validityDays;
            }

            public void setValidityDays(String validityDays) {
                this.validityDays = validityDays;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }
        }
    }
}
