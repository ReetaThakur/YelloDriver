package com.app.yellodriver.model.ModelFAQ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelHtmlContent {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    //==================================
    public class Data {
        @SerializedName("yt_content")
        @Expose
        private List<YtHtmlContent> ytHtmlContents;

        public void setYtHtmlContents(List<YtHtmlContent> yt_content) {
            this.ytHtmlContents = yt_content;
        }

        public List<YtHtmlContent> getYtHtmlContents() {
            return this.ytHtmlContents;
        }
//==================================

        public class YtHtmlContent {
            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("title")
            @Expose
            private String title;

            @SerializedName("content")
            @Expose
            private Content content;

            @SerializedName("slug")
            @Expose
            private String slug;

            private int version;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return this.title;
            }

            public void setContent(Content content) {
                this.content = content;
            }

            public Content getContent() {
                return this.content;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public String getSlug() {
                return this.slug;
            }

            public void setVersion(int version) {
                this.version = version;
            }

            public int getVersion() {
                return this.version;
            }

            //==================================
            public class Content {
                @SerializedName("data")
                @Expose
                private String data;


                public void setData(String data) {
                    this.data = data;
                }

                public String getData() {
                    return this.data;
                }
            }
        }
    }
}
