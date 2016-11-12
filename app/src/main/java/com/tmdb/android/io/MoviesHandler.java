package com.tmdb.android.io;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.provider.TmdbContract;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronel on 10/11/2016.
 */
public class MoviesHandler extends BaseHandler<List<Movie>>{

    private ArrayList<Movie> mMovie = new ArrayList<Movie>();

    public MoviesHandler(Context context) {
        super(context);
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = TmdbContract.Movies.CONTENT_URI;

        list.add(ContentProviderOperation.newDelete(uri).build());
        for (Movie movie : mMovie) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(uri);
            builder.withValue(TmdbContract.Movies.MOVIE_ID, movie.movieId);
            builder.withValue(TmdbContract.Movies.MOVIE_TITLE, movie.title);
            builder.withValue(TmdbContract.Movies.MOVIE_VOTE_AVERAGE, movie.voteAverage);
            builder.withValue(TmdbContract.Movies.MOVIE_OVERVIEW, movie.overview);
            builder.withValue(TmdbContract.Movies.MOVIE_RELEASE_DATE, movie.releaseDate);
            builder.withValue(TmdbContract.Movies.MOVIE_POSTER_PATH, movie.posterPath);
            list.add(builder.build());
        }
    }

    @Override
    public void process(List<Movie> elements) {
        mMovie.addAll(elements);
    }

}
