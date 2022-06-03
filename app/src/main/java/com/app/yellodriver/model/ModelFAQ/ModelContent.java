package com.app.yellodriver.model.ModelFAQ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelContent {

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

        @SerializedName("yt_content")
        @Expose
        private List<YtContent> ytContent = null;

        public List<YtContent> getYtContent() {
            return ytContent;
        }

        public void setYtContent(List<YtContent> ytContent) {
            this.ytContent = ytContent;
        }

        public class YtContent {

            @SerializedName("__typename")
            @Expose
            private String typename;
            @SerializedName("content")
            @Expose
            private Content content;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("slug")
            @Expose
            private String slug;
            @SerializedName("title")
            @Expose
            private String title;

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public Content getContent() {
                return content;
            }

            public void setContent(Content content) {
                this.content = content;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public class Content {

                @SerializedName("data")
                @Expose
                private Data_ data;
                @SerializedName("type")
                @Expose
                private String type;

                public Data_ getData() {
                    return data;
                }

                public void setData(Data_ data) {
                    this.data = data;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public class Data_ {

                    @SerializedName("A")
                    @Expose
                    private String a;
                    @SerializedName("Q")
                    @Expose
                    private String q;

                    public String getA() {
                        return a;
                    }

                    public void setA(String a) {
                        this.a = a;
                    }

                    public String getQ() {
                        return q;
                    }

                    public void setQ(String q) {
                        this.q = q;
                    }

                }
            }

        }
    }
}