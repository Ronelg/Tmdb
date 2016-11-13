package com.tmdb.android.data.source;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.io.model.Trailer;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ronel on 11/11/2016.
 */

public class TmdbRepository implements TmdbDataSource {

    private static TmdbRepository sInstance = null;

    private final TmdbDataSource mTmdbRemoteDataSource;
    private final TmdbDataSource mTmdbLocalDataSource;

    // Prevent direct instantiation.
    private TmdbRepository(@NonNull TmdbDataSource tmdbRemoteDataSource,
            @NonNull TmdbDataSource tmdbLocalDataSource) {
        mTmdbRemoteDataSource = checkNotNull(tmdbRemoteDataSource);
        mTmdbLocalDataSource = checkNotNull(tmdbLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tmdbRemoteDataSource the backend data source
     * @param tmdbLocalDataSource  the device storage data source
     * @return the {@link TmdbRepository} instance
     */
    public static TmdbRepository getInstance(TmdbDataSource tmdbRemoteDataSource,
            TmdbDataSource tmdbLocalDataSource) {
        if (sInstance == null) {
            sInstance = new TmdbRepository(tmdbRemoteDataSource, tmdbLocalDataSource);
        }
        return sInstance;
    }

    /**
     * Used to force {@link #getInstance(TmdbDataSource, TmdbDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public void getMovies(final boolean forceUpdate, @NonNull final GetMoviesCallback callback) {
        checkNotNull(callback);

        // Load from server
        mTmdbRemoteDataSource.getMovies(forceUpdate, new GetMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                if(forceUpdate){
                    deleteAllMovies();
                }
                refreshLocalDataSource(movies);
                callback.onMoviesLoaded(movies);
            }

            @Override
            public void onError() {
                callback.onError();
            }

            @Override
            public void onDataNotAvailable() {
                mTmdbLocalDataSource.getMovies(forceUpdate, callback);
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMovieTrailers(@NonNull final String movieId,
            @NonNull final GetMovieTrailersCallback callback) {

        // Load from server
        mTmdbRemoteDataSource.getMovieTrailers(movieId, new GetMovieTrailersCallback() {
            @Override
            public void onMovieTrailersLoaded(List<Trailer> trailers) {
                refreshLocalDataSource(movieId, trailers);
                callback.onMovieTrailersLoaded(trailers);
            }

            @Override
            public void onError() {
                callback.onError();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void saveMovies(@NonNull List<Movie> movies) {
        checkNotNull(movies);
        mTmdbRemoteDataSource.saveMovies(movies);
        mTmdbLocalDataSource.saveMovies(movies);
    }

    @Override
    public void saveMovieTrailers(@NonNull String movieId, List<Trailer> trailers) {
        checkNotNull(movieId);
        checkNotNull(trailers);
        mTmdbRemoteDataSource.saveMovieTrailers(movieId, trailers);
        mTmdbLocalDataSource.saveMovieTrailers(movieId, trailers);
    }

    @Override
    public void deleteAllMovies() {
        mTmdbRemoteDataSource.deleteAllMovies();
        mTmdbLocalDataSource.deleteAllMovies();
    }

    @Override
    public void deleteAllMovieTrailers(@NonNull String movieId) {
        checkNotNull(movieId);
        mTmdbRemoteDataSource.deleteAllMovieTrailers(movieId);
        mTmdbLocalDataSource.deleteAllMovieTrailers(movieId);
    }

    private void refreshLocalDataSource(List<Movie> movies) {
        mTmdbLocalDataSource.saveMovies(movies);
    }

    private void refreshLocalDataSource(String movieId, List<Trailer> trailers) {
        mTmdbLocalDataSource.saveMovieTrailers(movieId, trailers);
    }

    public interface LoadDataCallback {
        void onDataLoaded(Cursor data);

        void onDataEmpty();

        void onDataNotAvailable();

        void onDataReset();
    }
}
