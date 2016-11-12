package com.tmdb.android.data.api.model;

import com.google.gson.annotations.SerializedName;
import com.tmdb.android.io.model.Movie;
import java.util.List;

/**
 * Created by ronel on 11/11/2016.
 */

public class MoviesResponse {

    @SerializedName("page")
    public int page;
    @SerializedName("total_results")
    public int totalResults;
    @SerializedName("total_pages")
    public int totalPages;
    @SerializedName("results")
    public List<Movie> movies;
}
