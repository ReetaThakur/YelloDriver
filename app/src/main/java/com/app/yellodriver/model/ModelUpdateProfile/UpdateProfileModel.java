package com.app.yellodriver.model.ModelUpdateProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateProfileModel {

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

        @SerializedName("UpdateUser")
        @Expose
        private UpdateUser updateUser;

        public UpdateUser getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(UpdateUser updateUser) {
            this.updateUser = updateUser;
        }

        public class UpdateUser {

            @SerializedName("user")
            @Expose
            private User user;
            @SerializedName("user_id")
            @Expose
            private String userId;

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public class User {

                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("email")
                @Expose
                private String email;
                @SerializedName("mobile")
                @Expose
                private String mobile;
                @SerializedName("full_name")
                @Expose
                private String fullName;
                @SerializedName("average_rate")
                @Expose
                private Double averageRate;
                @SerializedName("country_code")
                @Expose
                private String countryCode;
                @SerializedName("profile_photo_file_id")
                @Expose
                private String profilePhotoFileId;
                @Expose
                private String created_at;
                @Expose
                private String updated_at;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getFullName() {
                    return fullName;
                }

                public void setFullName(String fullName) {
                    this.fullName = fullName;
                }

                public Double getAverageRate() {
                    return averageRate;
                }

                public void setAverageRate(Double averageRate) {
                    this.averageRate = averageRate;
                }

                public String getCountryCode() {
                    return countryCode;
                }

                public void setCountryCode(String countryCode) {
                    this.countryCode = countryCode;
                }

                public String getProfilePhotoFileId() {
                    return profilePhotoFileId;
                }

                public void setProfilePhotoFileId(String profilePhotoFileId) {
                    this.profilePhotoFileId = profilePhotoFileId;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }
            }
        }
    }
}