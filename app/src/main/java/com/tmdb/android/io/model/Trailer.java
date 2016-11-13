package com.tmdb.android.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ronel on 10/11/2016.
 */
public class Trailer {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("site")
    @Expose
    public String site;
    @SerializedName("size")
    @Expose
    public int size;
    @SerializedName("type")
    @Expose
    public String type;

    public Trailer(String id, String key, String name, String site, int size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public static class Builder{
        private String id;
        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSite(String site) {
            this.site = site;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Trailer build(){
            return new Trailer(id, key, name, site, size, type);
        }
    }
}
