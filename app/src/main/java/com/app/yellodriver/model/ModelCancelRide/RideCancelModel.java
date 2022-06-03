package com.app.yellodriver.model.ModelCancelRide;

public class RideCancelModel {
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    //==================================
    public class Data {
        private CancelRide CancelRide;

        public void setCancelRide(CancelRide CancelRide) {
            this.CancelRide = CancelRide;
        }

        public CancelRide getCancelRide() {
            return this.CancelRide;
        }
        //==================================

        public class CancelRide {
            private String id;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }
        }
    //==================================
    }
}




