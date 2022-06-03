package com.app.yellodriver.model.ModelRideStart;

//==================================
public class RideStartModel {
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    //==================================
    public class Data {
        private StartRide StartRide;

        public void setStartRide(StartRide StartRide) {
            this.StartRide = StartRide;
        }

        public StartRide getStartRide() {
            return this.StartRide;
        }

        public class StartRide {
            private String boarding_pass_id;

            private double distance;

            private String driver_user_id;

            private String id;

            private String status;

            private String user_id;

            public void setBoarding_pass_id(String boarding_pass_id) {
                this.boarding_pass_id = boarding_pass_id;
            }

            public String getBoarding_pass_id() {
                return this.boarding_pass_id;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public double getDistance() {
                return this.distance;
            }

            public void setDriver_user_id(String driver_user_id) {
                this.driver_user_id = driver_user_id;
            }

            public String getDriver_user_id() {
                return this.driver_user_id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_id() {
                return this.user_id;
            }
        }


    }

//==================================

}
