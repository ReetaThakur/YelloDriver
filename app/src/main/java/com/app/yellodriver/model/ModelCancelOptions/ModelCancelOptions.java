package com.app.yellodriver.model.ModelCancelOptions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelCancelOptions {

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

        @SerializedName("yt_system_setting")
        @Expose
        private ArrayList<YtSystemSetting> ytSystemSetting = null;

        public ArrayList<YtSystemSetting> getYtSystemSetting() {
            return ytSystemSetting;
        }

        public void setYtSystemSetting(ArrayList<YtSystemSetting> ytSystemSetting) {
            this.ytSystemSetting = ytSystemSetting;
        }

        public class YtSystemSetting {

            @SerializedName("content")
            @Expose
            private Content content;
            @SerializedName("slug")
            @Expose
            private String slug;
            @SerializedName("id")
            @Expose
            private String id;

            public Content getContent() {
                return content;
            }

            public void setContent(Content content) {
                this.content = content;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }


            public class Content {

                @SerializedName("type")
                @Expose
                private String type;
                @SerializedName("options")
                @Expose
                private List<Option> options = null;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<Option> getOptions() {
                    return options;
                }

                public void setOptions(List<Option> options) {
                    this.options = options;
                }

                public class Option {

                    @SerializedName("value")
                    @Expose
                    private String value;
                    @SerializedName("caption")
                    @Expose
                    private String caption;
                    private boolean isSelected;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public boolean isSelected() {
                        return isSelected;
                    }

                    public void setSelected(boolean selected) {
                        isSelected = selected;
                    }

                    public String getCaption() {
                        return caption;
                    }

                    public void setCaption(String caption) {
                        this.caption = caption;
                    }

                }
            }
        }
    }
}
