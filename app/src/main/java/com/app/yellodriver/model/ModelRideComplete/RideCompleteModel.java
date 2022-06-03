package com.app.yellodriver.model.ModelRideComplete;

public class RideCompleteModel {
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }
    //==================================

    public class Data {
        private CompleteRide CompleteRide;

        public void setCompleteRide(CompleteRide CompleteRide) {
            this.CompleteRide = CompleteRide;
        }

        public CompleteRide getCompleteRide() {
            return this.CompleteRide;
        }

        //==================================
        public class CompleteRide {
            private double distance;

            private String id;

            private String route_map_file_id;

            private String status;

            private String boarding_pass_id;

            private String user_id;

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public double getDistance() {
                return this.distance;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setRoute_map_file_id(String route_map_file_id) {
                this.route_map_file_id = route_map_file_id;
            }

            public String getRoute_map_file_id() {
                return this.route_map_file_id;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setBoarding_pass_id(String boarding_pass_id) {
                this.boarding_pass_id = boarding_pass_id;
            }

            public String getBoarding_pass_id() {
                return this.boarding_pass_id;
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
