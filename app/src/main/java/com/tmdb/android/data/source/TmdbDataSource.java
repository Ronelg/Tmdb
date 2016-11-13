package com.tmdb.android.data.source;

import android.support.annotation.NonNull;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.io.model.Trailer;
import java.util.List;

/**
 * Created by ronel on 11/11/2016.
 */

public interface TmdbDataSource {

    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onError();

        void onDataNotAvailable();
    }

    interface GetMovieTrailersCallback {

        void onMovieTrailersLoaded(List<Trailer> trailers);

        void onError();

        void onDataNotAvailable();
    }

    void getMovies(boolean forceUpdate, @NonNull GetMoviesCallback callback);

    void getMovieTrailers(@NonNull String movieId, @NonNull GetMovieTrailersCallback callback);

    void saveMovies(@NonNull List<Movie> movies);

    void saveMovieTrailers(@NonNull String movieId,List<Trailer> trailers);

    void deleteAllMovies();

    void deleteAllMovieTrailers(@NonNull String movieId);
}
