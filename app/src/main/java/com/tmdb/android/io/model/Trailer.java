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
}
