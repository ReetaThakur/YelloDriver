package com.app.yellodriver.model.ModelEarning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelEarningWeek {

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

        @SerializedName("total_tip_week")
        @Expose
        private TotalTipWeek totalTipWeek;

        public TotalTipWeek getTotalTipWeek() {
            return totalTipWeek;
        }

        public void setTotalTipWeek(TotalTipWeek totalTipWeek) {
            this.totalTipWeek = totalTipWeek;
        }

        public class TotalTipWeek {

            @SerializedName("__typename")
            @Expose
            private String typename;
            @SerializedName("aggregate")
            @Expose
            private Aggregate aggregate;

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public Aggregate getAggregate() {
                return aggregate;
            }

            public void setAggregate(Aggregate aggregate) {
                this.aggregate = aggregate;
            }

            public class Aggregate {

                @SerializedName("__typename")
                @Expose
                private String typename;
                @SerializedName("sum")
                @Expose
                private Sum sum;
                @SerializedName("count")
                @Expose
                private Integer count;

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public Sum getSum() {
                    return sum;
                }

                public void setSum(Sum sum) {
                    this.sum = sum;
                }

                public Integer getCount() {
                    return count;
                }

                public void setCount(Integer count) {
                    this.count = count;
                }

                public class Sum {

                    @SerializedName("__typename")
                    @Expose
                    private String typename;
                    @SerializedName("amount")
                    @Expose
                    private Integer amount;
                    @SerializedName("ride_distance")
                    @Expose
                    private Double rideDistance;
                    @SerializedName("ride_duration")
                    @Expose
                    private Double rideDuration;

                    public String getTypename() {
                        return typename;
                    }

                    public void setTypename(String typename) {
                        this.typename = typename;
                    }

                    public Integer getAmount() {
                        return amount;
                    }

                    public void setAmount(Integer amount) {
                        this.amount = amount;
                    }

                    public Double getRideDistance() {
                        return rideDistance;
                    }

                    public void setRideDistance(Double rideDistance) {
                        this.rideDistance = rideDistance;
                    }

                    public Double getRideDuration() {
                        return rideDuration;
                    }

                    public void setRideDuration(Double rideDuration) {
                        this.rideDuration = rideDuration;
                    }
                }
            }
        }
    }
}
