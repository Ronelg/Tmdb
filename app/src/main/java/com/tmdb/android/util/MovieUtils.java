package com.tmdb.android.util;

import android.database.Cursor;
import android.net.Uri;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.provider.TmdbContract;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronel on 12/11/2016.
 */

public class MovieUtils {

    private static final String BASE_IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/";
    private static final String PATTERN = "__w-300-500__";

    public static List<Movie> readMoviesFromCursor(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Movie.Builder builder = new Movie.Builder();
                builder.setMovieId(
                        cursor.getLong(cursor.getColumnIndex(TmdbContract.Movies.MOVIE_ID)));
                builder.setPosterPath(cursor.getString(
                        cursor.getColumnIndex(TmdbContract.Movies.MOVIE_POSTER_PATH)));
                builder.setOverview(cursor.getString(
                        cursor.getColumnIndex(TmdbContract.Movies.MOVIE_OVERVIEW)));
                builder.setReleaseDate(cursor.getString(
                        cursor.getColumnIndex(TmdbContract.Movies.MOVIE_RELEASE_DATE)));
                builder.setTitle(
                        cursor.getString(cursor.getColumnIndex(TmdbContract.Movies.MOVIE_TITLE)));
                builder.setVoteAverage(cursor.getDouble(
                        cursor.getColumnIndex(TmdbContract.Movies.MOVIE_VOTE_AVERAGE)));

                movies.add(builder.build());
                cursor.moveToNext();
            }
        }
        return movies;
    }

    public static String patchPosterPath(String posterPath){
        return BASE_IMAGE_URL_PREFIX + PATTERN + posterPath;
    }
}
