package com.app.yellodriver.model.ModelRideReject;
//==================================

public class RideRejectModel {
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    //==================================

    public class Data {
        private RejectRide RejectRide;

        public void setRejectRide(RejectRide RejectRide) {
            this.RejectRide = RejectRide;
        }

        public RejectRide getRejectRide() {
            return this.RejectRide;
        }
        //==================================

        public class RejectRide {
            private String id;

            private boolean is_rejected;

            private String rejected_at;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setIs_rejected(boolean is_rejected) {
                this.is_rejected = is_rejected;
            }

            public boolean getIs_rejected() {
                return this.is_rejected;
            }

            public void setRejected_at(String rejected_at) {
                this.rejected_at = rejected_at;
            }

            public String getRejected_at() {
                return this.rejected_at;
            }
        }
    }

//==================================

}