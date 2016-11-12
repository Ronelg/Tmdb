package com.tmdb.android.io.model;

import android.text.TextUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tmdb.android.data.TmdbItem;

/**
 * Created by ronel on 10/11/2016.
 */
public class Movie extends TmdbItem {
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("id")
    @Expose
    public long movieId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("vote_average")
    @Expose
    public double voteAverage;

    public Movie(long id) {
        super(id);
    }

    public Movie( String posterPath, String overview, String releaseDate, long movieId,
            String title, double voteAverage) {
        super(movieId);
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate(){
        if(!TextUtils.isEmpty(releaseDate)) {
            String[] segments = releaseDate.split("-");
            return segments[0];
        }
        return releaseDate;
    }

    public static class Builder{
        private String posterPath;
        private String overview;
        private String releaseDate;
        private long movieId;
        private String title;
        private double voteAverage;

        public Movie.Builder setPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public Movie.Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Movie.Builder setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Movie.Builder setMovieId(long movieId) {
            this.movieId = movieId;
            return this;
        }

        public Movie.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Movie.Builder setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Movie build(){
            return new Movie(posterPath, overview, releaseDate,  movieId,
             title,  voteAverage);
        }
    }
}
