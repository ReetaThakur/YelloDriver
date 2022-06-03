package com.app.yellodriver.model.ModelVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleModel {

    @SerializedName("data")
    @Expose
    private VehicleData data;

    public VehicleData getData() {
        return data;
    }

    public void setData(VehicleData data) {
        this.data = data;
    }

    public class VehicleData {

        @SerializedName("yt_vehicle")
        @Expose
        private List<YtVehicle> ytVehicles = null;

        public List<YtVehicle> getYtVehicle() {
            return ytVehicles;
        }

        public void setYtVehicle(List<YtVehicle> ytNotification) {
            this.ytVehicles = ytNotification;
        }


        public class YtVehicle {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("registration_date")
            @Expose
            private String registrationDate;

            @SerializedName("photo_file_id")
            @Expose
            private String photo_file_id;

            @SerializedName("registration_number")
            @Expose
            private String registrationNumber;

            @SerializedName("vehicle_make")
            @Expose
            private Vehicle_make vehicleMake;

            @SerializedName("vehicle_model")
            @Expose
            private Vehicle_model vehicleModel;

            @SerializedName("vehicle_type")
            @Expose
            private Vehicle_type vehicleType;
            @SerializedName("vehicle_docs")
            @Expose
            private List<VehicleDoc> vehicleDocs = null;

            public String getRegistrationDate() {
                return registrationDate;
            }

            public void setRegistrationDate(String registrationDate) {
                this.registrationDate = registrationDate;
            }

            public String getRegistrationNumber() {
                return registrationNumber;
            }

            public void setRegistrationNumber(String registrationNumber) {
                this.registrationNumber = registrationNumber;
            }

            public void setVehicleMake(Vehicle_make vehicle_make) {
                this.vehicleMake = vehicle_make;
            }

            public Vehicle_make getVehicleMake() {
                return this.vehicleMake;
            }

            public Vehicle_model getVehicleModel() {
                return vehicleModel;
            }

            public void setVehicleModel(Vehicle_model vehicleModel) {
                this.vehicleModel = vehicleModel;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Vehicle_type getVehicleType() {
                return vehicleType;
            }

            public void setVehicleType(Vehicle_type vehicleType) {
                this.vehicleType = vehicleType;
            }

            public String getPhoto_file_id() {
                return photo_file_id;
            }

            public void setPhoto_file_id(String photo_file_id) {
                this.photo_file_id = photo_file_id;
            }

            public List<VehicleDoc> getVehicleDocs() {
                return vehicleDocs;
            }

            public void setVehicleDocs(List<VehicleDoc> vehicleDocs) {
                this.vehicleDocs = vehicleDocs;
            }
            /*@SerializedName("user_id")
    @Expose
    private Object userId;*/

            /*public Object getUserId() {
                return userId;
            }

            public void setUserId(Object userId) {
                this.userId = userId;
            }*/
            public class VehicleDoc {

                @SerializedName("doc_type")
                @Expose
                private DocType docType;

                public DocType getDocType() {
                    return docType;
                }

                public void setDocType(DocType docType) {
                    this.docType = docType;
                }

                public class DocType {

                    @SerializedName("slug")
                    @Expose
                    private String slug;

                    public String getSlug() {
                        return slug;
                    }

                    public void setSlug(String slug) {
                        this.slug = slug;
                    }
                }
            }

        }

        public class Vehicle_make {
            @SerializedName("title")
            @Expose
            private String title;

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return this.title;
            }
        }

        public class Vehicle_model {
            @SerializedName("title")
            @Expose
            private String title;

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return this.title;
            }
        }

        public class Vehicle_type {
            @SerializedName("title")
            @Expose
            private String title;

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return this.title;
            }
        }
    }

}