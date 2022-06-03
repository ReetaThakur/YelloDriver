package com.app.yellodriver.model.ModelRideRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RideRequestModel implements Parcelable {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("yt_ride_request")
        @Expose
        private ArrayList<YtRideRequest> ytRideRequest = null;

        public ArrayList<YtRideRequest> getYtRideRequest() {
            return ytRideRequest;
        }

        public void setYtRideRequest(ArrayList<YtRideRequest> ytRideRequest) {
            this.ytRideRequest = ytRideRequest;
        }

        public static class EndAddress implements Parcelable {

            @SerializedName("line1")
            @Expose
            private String line1;

            public String getLine1() {
                return line1;
            }

            public void setLine1(String line1) {
                this.line1 = line1;
            }

            protected EndAddress(Parcel in) {
                line1 = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(line1);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<EndAddress> CREATOR = new Parcelable.Creator<EndAddress>() {
                @Override
                public EndAddress createFromParcel(Parcel in) {
                    return new EndAddress(in);
                }

                @Override
                public EndAddress[] newArray(int size) {
                    return new EndAddress[size];
                }
            };
        }

        public static class Ride implements Parcelable {

            @SerializedName("end_address")
            @Expose
            private EndAddress endAddress;
            @SerializedName("end_location")
            @Expose
            private String endLocation;
            @SerializedName("start_address")
            @Expose
            private StartAddress startAddress;
            @SerializedName("start_location")
            @Expose
            private String startLocation;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("boarding_pass_id")
            @Expose
            private String boarding_pass_id;
            @SerializedName("user")
            @Expose
            private User user;

            public EndAddress getEndAddress() {
                return endAddress;
            }

            public void setEndAddress(EndAddress endAddress) {
                this.endAddress = endAddress;
            }

            public String getEndLocation() {
                return endLocation;
            }

            public void setEndLocation(String endLocation) {
                this.endLocation = endLocation;
            }

            public StartAddress getStartAddress() {
                return startAddress;
            }

            public void setStartAddress(StartAddress startAddress) {
                this.startAddress = startAddress;
            }

            public String getStartLocation() {
                return startLocation;
            }

            public void setStartLocation(String startLocation) {
                this.startLocation = startLocation;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }

            public String getBoarding_pass_id() {
                return boarding_pass_id;
            }

            public void setBoarding_pass_id(String boarding_pass_id) {
                this.boarding_pass_id = boarding_pass_id;
            }

            protected Ride(Parcel in) {
                endAddress = (EndAddress) in.readValue(EndAddress.class.getClassLoader());
                endLocation = in.readString();
                startAddress = (StartAddress) in.readValue(StartAddress.class.getClassLoader());
                startLocation = in.readString();
                status = in.readString();
                userId = in.readString();
                boarding_pass_id = in.readString();
                user = (User) in.readValue(User.class.getClassLoader());
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeValue(endAddress);
                dest.writeString(endLocation);
                dest.writeValue(startAddress);
                dest.writeString(startLocation);
                dest.writeString(status);
                dest.writeString(userId);
                dest.writeString(boarding_pass_id);
                dest.writeValue(user);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Ride> CREATOR = new Parcelable.Creator<Ride>() {
                @Override
                public Ride createFromParcel(Parcel in) {
                    return new Ride(in);
                }

                @Override
                public Ride[] newArray(int size) {
                    return new Ride[size];
                }
            };
        }

        public static class StartAddress implements Parcelable {

            @SerializedName("line1")
            @Expose
            private String line1;

            public String getLine1() {
                return line1;
            }

            public void setLine1(String line1) {
                this.line1 = line1;
            }

            protected StartAddress(Parcel in) {
                line1 = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(line1);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<StartAddress> CREATOR = new Parcelable.Creator<StartAddress>() {
                @Override
                public StartAddress createFromParcel(Parcel in) {
                    return new StartAddress(in);
                }

                @Override
                public StartAddress[] newArray(int size) {
                    return new StartAddress[size];
                }
            };
        }

        public static class YtRideRequest implements Parcelable {

            @SerializedName("available")
            @Expose
            private Boolean available;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("driver_user_id")
            @Expose
            private String driverUserId;
            @SerializedName("eta_number")
            @Expose
            private Double etaNumber;
            @SerializedName("eta_unit")
            @Expose
            private String etaUnit;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("is_accepted")
            @Expose
            private Boolean isAccepted;
            @SerializedName("is_rejected")
            @Expose
            private Boolean isRejected;
            @SerializedName("rejected_at")
            @Expose
            private Object rejectedAt;
            @SerializedName("ride_id")
            @Expose
            private String rideId;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("ride")
            @Expose
            private Ride ride;

            public Boolean getAvailable() {
                return available;
            }

            public void setAvailable(Boolean available) {
                this.available = available;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getDriverUserId() {
                return driverUserId;
            }

            public void setDriverUserId(String driverUserId) {
                this.driverUserId = driverUserId;
            }

            public Double getEtaNumber() {
                return etaNumber;
            }

            public void setEtaNumber(Double etaNumber) {
                this.etaNumber = etaNumber;
            }

            public String getEtaUnit() {
                return etaUnit;
            }

            public void setEtaUnit(String etaUnit) {
                this.etaUnit = etaUnit;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Boolean getIsAccepted() {
                return isAccepted;
            }

            public void setIsAccepted(Boolean isAccepted) {
                this.isAccepted = isAccepted;
            }

            public Boolean getIsRejected() {
                return isRejected;
            }

            public void setIsRejected(Boolean isRejected) {
                this.isRejected = isRejected;
            }

            public Object getRejectedAt() {
                return rejectedAt;
            }

            public void setRejectedAt(Object rejectedAt) {
                this.rejectedAt = rejectedAt;
            }

            public String getRideId() {
                return rideId;
            }

            public void setRideId(String rideId) {
                this.rideId = rideId;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public Ride getRide() {
                return ride;
            }

            public void setRide(Ride ride) {
                this.ride = ride;
            }

            YtRideRequest(Parcel in) {
                byte availableVal = in.readByte();
                available = availableVal == 0x02 ? null : availableVal != 0x00;
                createdAt = in.readString();
                driverUserId = in.readString();
                etaNumber = in.readByte() == 0x00 ? null : in.readDouble();
                etaUnit = in.readString();
                id = in.readString();
                byte isAcceptedVal = in.readByte();
                isAccepted = isAcceptedVal == 0x02 ? null : isAcceptedVal != 0x00;
                byte isRejectedVal = in.readByte();
                isRejected = isRejectedVal == 0x02 ? null : isRejectedVal != 0x00;
                rejectedAt = (Object) in.readValue(Object.class.getClassLoader());
                rideId = in.readString();
                updatedAt = in.readString();
                ride = (Ride) in.readValue(Ride.class.getClassLoader());
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                if (available == null) {
                    dest.writeByte((byte) (0x02));
                } else {
                    dest.writeByte((byte) (available ? 0x01 : 0x00));
                }
                dest.writeString(createdAt);
                dest.writeString(driverUserId);
                if (etaNumber == null) {
                    dest.writeByte((byte) (0x00));
                } else {
                    dest.writeByte((byte) (0x01));
                    dest.writeDouble(etaNumber);
                }
                dest.writeString(etaUnit);
                dest.writeString(id);
                if (isAccepted == null) {
                    dest.writeByte((byte) (0x02));
                } else {
                    dest.writeByte((byte) (isAccepted ? 0x01 : 0x00));
                }
                if (isRejected == null) {
                    dest.writeByte((byte) (0x02));
                } else {
                    dest.writeByte((byte) (isRejected ? 0x01 : 0x00));
                }
                dest.writeValue(rejectedAt);
                dest.writeString(rideId);
                dest.writeString(updatedAt);
                dest.writeValue(ride);
            }

            @SuppressWarnings("unused")
            public final static Parcelable.Creator<YtRideRequest> CREATOR = new Parcelable.Creator<YtRideRequest>() {
                @Override
                public YtRideRequest createFromParcel(Parcel in) {
                    return new YtRideRequest(in);
                }

                @Override
                public YtRideRequest[] newArray(int size) {
                    return new YtRideRequest[size];
                }
            };
        }


        public static class User implements Parcelable {

            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("full_name")
            @Expose
            private String fullName;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("profile_photo")
            @Expose
            private Object profilePhoto;
            @SerializedName("average_rate")
            @Expose
            private Double averageRate;
            @SerializedName("profile_photo_file_id")
            @Expose
            private String profilePhotoFileId;

            protected User(Parcel in) {
                email = in.readString();
                fullName = in.readString();
                mobile = in.readString();
                if (in.readByte() == 0) {
                    averageRate = 0.0;
                } else {
                    averageRate = in.readDouble();
                }
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(email);
                dest.writeString(fullName);
                dest.writeString(mobile);
                if (averageRate == null) {
                    dest.writeByte((byte) 0);
                } else {
                    dest.writeByte((byte) 1);
                    dest.writeDouble(averageRate);
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<User> CREATOR = new Creator<User>() {
                @Override
                public User createFromParcel(Parcel in) {
                    return new User(in);
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public Object getProfilePhoto() {
                return profilePhoto;
            }

            public void setProfilePhoto(Object profilePhoto) {
                this.profilePhoto = profilePhoto;
            }

            public Double getAverageRate() {
                if(null == averageRate)
                    return 0.0;
                return averageRate;
            }

            public void setAverageRate(Double averageRate) {
                this.averageRate = averageRate;
            }

            public String getProfilePhotoFileId() {
                return profilePhotoFileId;
            }

            public void setProfilePhotoFileId(String profilePhotoFileId) {
                this.profilePhotoFileId = profilePhotoFileId;
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }


    protected RideRequestModel(Parcel in) {
        data = (Data) in.readValue(Data.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RideRequestModel> CREATOR = new Parcelable.Creator<RideRequestModel>() {
        @Override
        public RideRequestModel createFromParcel(Parcel in) {
            return new RideRequestModel(in);
        }

        @Override
        public RideRequestModel[] newArray(int size) {
            return new RideRequestModel[size];
        }
    };
}