package com.app.yellodriver.model.ModelProfile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyProfileModel implements Parcelable {


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

        @SerializedName("yt_user_by_pk")
        @Expose
        private YtUser ytUser = null;

        public YtUser getYtUser() {
            return ytUser;
        }

        public void setYtUser(YtUser ytUser) {
            this.ytUser = ytUser;
        }


        public static class YtUser implements Parcelable {

            @SerializedName("active")
            @Expose
            private Boolean active;
            @SerializedName("address")
            @Expose
            private Object address;
            @SerializedName("country_code")
            @Expose
            private String countryCode;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("full_name")
            @Expose
            private String fullName;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("average_rate")
            @Expose
            private String average_rate;
            @SerializedName("profile")
            @Expose
            private Object profile;
            @SerializedName("profile_photo")
            @Expose
            private ProfilePhoto profilePhoto;

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public Object getProfile() {
                return profile;
            }

            public void setProfile(Object profile) {
                this.profile = profile;
            }

            public ProfilePhoto getProfilePhoto() {
                return profilePhoto;
            }

            public void setProfilePhoto(ProfilePhoto profilePhoto) {
                this.profilePhoto = profilePhoto;
            }

            public String getAverage_rate() {
                return average_rate;
            }

            public void setAverage_rate(String average_rate) {
                this.average_rate = average_rate;
            }

            public static class FileObject implements Parcelable {

                @SerializedName("key")
                @Expose
                private String key;
                @SerializedName("original_filename")
                @Expose
                private String originalFilename;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getOriginalFilename() {
                    return originalFilename;
                }

                public void setOriginalFilename(String originalFilename) {
                    this.originalFilename = originalFilename;
                }


                protected FileObject(Parcel in) {
                    key = in.readString();
                    originalFilename = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(key);
                    dest.writeString(originalFilename);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<FileObject> CREATOR = new Parcelable.Creator<FileObject>() {
                    @Override
                    public FileObject createFromParcel(Parcel in) {
                        return new FileObject(in);
                    }

                    @Override
                    public FileObject[] newArray(int size) {
                        return new FileObject[size];
                    }
                };
            }

            public static class ProfilePhoto implements Parcelable {

                @SerializedName("file_object")
                @Expose
                private FileObject fileObject;
                @SerializedName("id")
                @Expose
                private String id;

                public FileObject getFileObject() {
                    return fileObject;
                }

                public void setFileObject(FileObject fileObject) {
                    this.fileObject = fileObject;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                protected ProfilePhoto(Parcel in) {
                    fileObject = (FileObject) in.readValue(FileObject.class.getClassLoader());
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeValue(fileObject);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<ProfilePhoto> CREATOR = new Parcelable.Creator<ProfilePhoto>() {
                    @Override
                    public ProfilePhoto createFromParcel(Parcel in) {
                        return new ProfilePhoto(in);
                    }

                    @Override
                    public ProfilePhoto[] newArray(int size) {
                        return new ProfilePhoto[size];
                    }
                };
            }

            protected YtUser(Parcel in) {
                byte activeVal = in.readByte();
                active = activeVal == 0x02 ? null : activeVal != 0x00;
                address = (Object) in.readValue(Object.class.getClassLoader());
                countryCode = in.readString();
                email = in.readString();
                fullName = in.readString();
                id = in.readString();
                mobile = in.readString();
                createdAt = in.readString();
                average_rate = in.readString();
                profile = (Object) in.readValue(Object.class.getClassLoader());
                profilePhoto = (ProfilePhoto) in.readValue(ProfilePhoto.class.getClassLoader());
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                if (active == null) {
                    dest.writeByte((byte) (0x02));
                } else {
                    dest.writeByte((byte) (active ? 0x01 : 0x00));
                }
                dest.writeValue(address);
                dest.writeString(countryCode);
                dest.writeString(email);
                dest.writeString(fullName);
                dest.writeString(id);
                dest.writeString(mobile);
                dest.writeString(createdAt);
                dest.writeString(average_rate);
                dest.writeValue(profile);
                dest.writeValue(profilePhoto);
            }

            @SuppressWarnings("unused")
            public final Parcelable.Creator<YtUser> CREATOR = new Parcelable.Creator<YtUser>() {
                @Override
                public YtUser createFromParcel(Parcel in) {
                    return new YtUser(in);
                }

                @Override
                public YtUser[] newArray(int size) {
                    return new YtUser[size];
                }
            };
        }
    }


    protected MyProfileModel(Parcel in) {
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
    public static final Parcelable.Creator<MyProfileModel> CREATOR = new Parcelable.Creator<MyProfileModel>() {
        @Override
        public MyProfileModel createFromParcel(Parcel in) {
            return new MyProfileModel(in);
        }

        @Override
        public MyProfileModel[] newArray(int size) {
            return new MyProfileModel[size];
        }
    };
}