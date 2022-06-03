package com.app.yellodriver.model.ModelUserOnboarding.ModelSendOtp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSendOtp implements Parcelable {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Parcelable {

        @SerializedName("SendOtp")
        @Expose
        private SendOtp sendOtp;

        public SendOtp getSendOtp() {
            return sendOtp;
        }

        public void setSendOtp(SendOtp sendOtp) {
            this.sendOtp = sendOtp;
        }

        public class SendOtp {

            @SerializedName("user_otp")
            @Expose
            private UserOtp userOtp;

            public UserOtp getUserOtp() {
                return userOtp;
            }

            public void setUserOtp(UserOtp userOtp) {
                this.userOtp = userOtp;
            }

            public class UserOtp implements Parcelable {

                @SerializedName("mobile")
                @Expose
                private String mobile;
                @SerializedName("fullName")
                @Expose
                private String fullName;
                @SerializedName("secret")
                @Expose
                private String secret;
                @SerializedName("valid_to")
                @Expose
                private String validTo;
                @SerializedName("valid_from")
                @Expose
                private String validFrom;
                @SerializedName("country_code")
                @Expose
                private String countryCode;

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getValidTo() {
                    return validTo;
                }

                public void setValidTo(String validTo) {
                    this.validTo = validTo;
                }

                public String getValidFrom() {
                    return validFrom;
                }

                public void setValidFrom(String validFrom) {
                    this.validFrom = validFrom;
                }

                public String getCountryCode() {
                    return countryCode;
                }

                public void setCountryCode(String countryCode) {
                    this.countryCode = countryCode;
                }

                public String getFullName() {
                    return fullName;
                }

                public void setFullName(String fullName) {
                    this.fullName = fullName;
                }

                public UserOtp(){

                }

                protected UserOtp(Parcel in) {
                    mobile = in.readString();
                    fullName = in.readString();
                    secret = in.readString();
                    validTo = in.readString();
                    validFrom = in.readString();
                    countryCode = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(mobile);
                    dest.writeString(fullName);
                    dest.writeString(secret);
                    dest.writeString(validTo);
                    dest.writeString(validFrom);
                    dest.writeString(countryCode);
                }

                @SuppressWarnings("unused")
                public final Parcelable.Creator<UserOtp> CREATOR = new Parcelable.Creator<UserOtp>() {
                    @Override
                    public UserOtp createFromParcel(Parcel in) {
                        return new UserOtp(in);
                    }

                    @Override
                    public UserOtp[] newArray(int size) {
                        return new UserOtp[size];
                    }
                };
            }
        }

        protected Data(Parcel in) {
            sendOtp = (SendOtp) in.readValue(SendOtp.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(sendOtp);
        }

        @SuppressWarnings("unused")
        public final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }

    protected ModelSendOtp(Parcel in) {
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
    public static final Parcelable.Creator<ModelSendOtp> CREATOR = new Parcelable.Creator<ModelSendOtp>() {
        @Override
        public ModelSendOtp createFromParcel(Parcel in) {
            return new ModelSendOtp(in);
        }

        @Override
        public ModelSendOtp[] newArray(int size) {
            return new ModelSendOtp[size];
        }
    };
}