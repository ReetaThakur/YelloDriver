package com.app.yellodriver.model.ModelEarning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelEarningLastTip {

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

        @SerializedName("yt_user_wallet")
        @Expose
        private List<YtUserWallet> ytUserWallet = null;

        public List<YtUserWallet> getYtUserWallet() {
            return ytUserWallet;
        }

        public void setYtUserWallet(List<YtUserWallet> ytUserWallet) {
            this.ytUserWallet = ytUserWallet;
        }

        public class YtUserWallet {

            @SerializedName("__typename")
            @Expose
            private String typename;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("amount")
            @Expose
            private Integer amount;
            @SerializedName("ride_distance")
            @Expose
            private Double rideDistance;
            @SerializedName("ride_duration")
            @Expose
            private Double rideDuration;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("ride")
            @Expose
            private Ride ride;

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public Ride getRide() {
                return ride;
            }

            public void setRide(Ride ride) {
                this.ride = ride;
            }

            public class Ride {

                @SerializedName("__typename")
                @Expose
                private String typename;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("user")
                @Expose
                private User user;

                public String getTypename() {
                    return typename;
                }

                public void setTypename(String typename) {
                    this.typename = typename;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public User getUser() {
                    return user;
                }

                public void setUser(User user) {
                    this.user = user;
                }

                public class User {

                    @SerializedName("__typename")
                    @Expose
                    private String typename;
                    @SerializedName("full_name")
                    @Expose
                    private String fullName;

                    public String getTypename() {
                        return typename;
                    }

                    public void setTypename(String typename) {
                        this.typename = typename;
                    }

                    public String getFullName() {
                        return fullName;
                    }

                    public void setFullName(String fullName) {
                        this.fullName = fullName;
                    }
                }
            }
        }
    }

}
