package com.example.samplewiki.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mita on 8/19/2017.
 */

public class SearchResult {

    @SerializedName("query")
    @Expose
    private SearchQuery query;

    public SearchQuery getQuery() {
        return query;
    }

    public class SearchQuery {
        @SerializedName("pages")
        @Expose
        private ArrayList<Pages> pagesList;

        public ArrayList<Pages> getPagesList() {
            return pagesList;
        }

        public class Pages {
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("terms")
            @Expose
            private Terms terms;
            @SerializedName("thumbnail")
            @Expose
            private Thumbnail thumbnail;
            @SerializedName("pageid")
            @Expose
            private int pageId;

            public String getTitle() {
                return title;
            }

            public Thumbnail getThumbnail() {
                return thumbnail;
            }

            public Terms getTerms() {
                return terms;
            }

            public int getPageId() {
                return pageId;
            }

            public void setPageId(int pageId) {
                this.pageId = pageId;
            }

            public class Terms {
                @SerializedName("description")
                @Expose
                private String description;

                public String getDescription() {
                    return description;
                }
            }

            public class Thumbnail {
                @SerializedName("source")
                @Expose
                private String source;

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }
            }
        }
    }
}
