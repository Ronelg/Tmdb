package com.tmdb.android.data.api.model;

import com.google.gson.annotations.SerializedName;
import com.tmdb.android.io.model.Trailer;
import java.util.List;

/**
 * Created by ronel on 11/11/2016.
 */

public class TrailersResponse {
    @SerializedName("id")
    public int movieId;
    @SerializedName("results")
    public List<Trailer> trailers;
}
