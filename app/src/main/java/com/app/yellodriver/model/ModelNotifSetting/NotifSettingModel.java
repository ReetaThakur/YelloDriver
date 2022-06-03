package com.app.yellodriver.model.ModelNotifSetting;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotifSettingModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    //==================================
    public class Data {
        @SerializedName("update_yt_user_setting")
        @Expose
        public Update_yt_user_setting update_yt_user_setting;

        public void setUpdate_yt_user_setting(Update_yt_user_setting update_yt_user_setting) {
            this.update_yt_user_setting = update_yt_user_setting;
        }

        public Update_yt_user_setting getUpdate_yt_user_setting() {
            return this.update_yt_user_setting;
        }

        //==================================
        @SerializedName("insert_yt_user_setting_one")
        @Expose
        private Yt_user_setting ytone_user_setting;

        public void setOneYt_user_setting(Yt_user_setting yt_user_setting) {
            this.ytone_user_setting = yt_user_setting;
        }

        public Yt_user_setting getOneYt_user_setting() {
            return this.ytone_user_setting;
        }

        //==================================
        @SerializedName("yt_user_setting")
        @Expose
        private List<Yt_user_setting> yt_user_setting;

        public void setYt_user_setting(List<Yt_user_setting> yt_user_setting) {
            this.yt_user_setting = yt_user_setting;
        }

        public List<Yt_user_setting> getYt_user_setting() {
            return this.yt_user_setting;
        }

        //=============QUERY CLASS=====================
        public class Yt_user_setting {

            @SerializedName("params")
            @Expose
            private String params;

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("type")
            @Expose
            private String type;

            public void setParams(String params) {
                this.params = params;
            }

            public Params getParams() {

                JSONObject notifParamsObj = null;
                Params notiparams = null;
                try {
                    notifParamsObj = new JSONObject(params);
                    notiparams = new Params(notifParamsObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return notiparams;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            //==================================
        }


        //============MUTATION CLASS======================
        public class Update_yt_user_setting {
            @SerializedName("returning")
            @Expose
            private List<Returning> returning;

            public void setReturning(List<Returning> returning) {
                this.returning = returning;
            }

            public List<Returning> getReturning() {
                return this.returning;
            }

            //=========================

            public class Returning {
                @SerializedName("params")
                @Expose
                private String params;

                @SerializedName("id")
                @Expose
                private String id;

                @SerializedName("type")
                @Expose
                private String type;

                public void setParams(String params) {
                    this.params = params;
                }

                public Params getParams() {

                    JSONObject notifParamsObj = null;
                    Params notiparams = null;
                    try {
                        notifParamsObj = new JSONObject(params);
                        notiparams = new Params(notifParamsObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return notiparams;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return this.id;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                //==================================

            }
        }
    }
    public static class Params {
        @SerializedName("sms")
        @Expose
        private boolean sms;

        @SerializedName("push")
        @Expose
        private boolean push;

        @SerializedName("email")
        @Expose
        private boolean email;

        @SerializedName("promotional")
        @Expose
        private boolean promotional;

        public Params(JSONObject notifParamsObj) {
            this.email = notifParamsObj.optBoolean("email",false);
            this.promotional = notifParamsObj.optBoolean("promotional",false);
            this.push = notifParamsObj.optBoolean("push",false);
            this.sms = notifParamsObj.optBoolean("sms",false);
        }

        public void setSms(boolean sms) {
            this.sms = sms;
        }

        public boolean getSms() {
            return this.sms;
        }

        public void setPush(boolean push) {
            this.push = push;
        }

        public boolean getPush() {
            return this.push;
        }

        public void setEmail(boolean email) {
            this.email = email;
        }

        public boolean getEmail() {
            return this.email;
        }

        public void setPromotional(boolean promotional) {
            this.promotional = promotional;
        }

        public boolean getPromotional() {
            return this.promotional;
        }
    }
}
//==================================

