package com.app.yellodriver.model.ModelPushNotify;

public class DeleteDeviceModel {
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }
//==================================
    public class Data {
        private Delete_yt_push_registration delete_yt_push_registration;

        public void setDelete_yt_push_registration(Delete_yt_push_registration delete_yt_push_registration) {
            this.delete_yt_push_registration = delete_yt_push_registration;
        }

        public Delete_yt_push_registration getDelete_yt_push_registration() {
            return this.delete_yt_push_registration;
        }
        //==================================
        public class Delete_yt_push_registration {
            private int affected_rows;

            public void setAffected_rows(int affected_rows) {
                this.affected_rows = affected_rows;
            }

            public int getAffected_rows() {
                return this.affected_rows;
            }
        }
    }
}
