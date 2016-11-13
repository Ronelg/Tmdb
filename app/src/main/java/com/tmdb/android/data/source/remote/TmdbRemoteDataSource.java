package com.tmdb.android.data.source.remote;

import android.support.annotation.NonNull;
import com.tmdb.android.data.TmdbApi;
import com.tmdb.android.data.api.model.MoviesResponse;
import com.tmdb.android.data.api.model.TrailersResponse;
import com.tmdb.android.data.source.TmdbDataSource;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.io.model.Trailer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ronel on 11/11/2016.
 */
public class TmdbRemoteDataSource implements TmdbDataSource {

    private static TmdbRemoteDataSource sInstance;
    private static TmdbApi mTmdbApi;

    private final static Map<String, Movie> mMovies = new LinkedHashMap<>();
    private final static Map<String, List<Trailer>> mMovieTrailers = new LinkedHashMap<>(2);

    private int mPage = 1;
    private int mTotalPages;

    // Prevent direct instantiation.
    private TmdbRemoteDataSource() {
        mTmdbApi = TmdbApi.getInstanse();
    }

    public static TmdbRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TmdbRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getMovies(@NonNull final GetMoviesCallback callback) {
        mTmdbApi.getApi().getPopularMovies(mPage).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.isSuccessful()) {
                    List<Movie> movies = response.body().movies;
                    saveMovies(movies);

                    ++mPage;
                    mTotalPages = response.body().totalPages;

                    callback.onMoviesLoaded(new ArrayList<Movie>(mMovies.values()));
                }else{
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMovieTrailers(@NonNull final String movieId,
            @NonNull final GetMovieTrailersCallback callback) {

        mTmdbApi.getApi()
                .getMovieTrailers(Long.valueOf(movieId))
                .enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(Call<TrailersResponse> call,
                            Response<TrailersResponse> response) {
                        if (response.isSuccessful()) {
                            int movId = response.body().movieId;
                            List<Trailer> trailers = response.body().trailers;

                            mMovieTrailers.put(movieId, trailers);

                            callback.onMovieTrailersLoaded(trailers);
                        } else {
                            callback.onError();
                        }
                    }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void saveMovies(@NonNull List<Movie> movies) {
        for (Movie movie : movies) {
            mMovies.put(String.valueOf(movie.movieId), movie);
        }
    }

    @Override
    public void saveMovieTrailers(@NonNull String movieId, List<Trailer> trailers) {
        for (Trailer trailer : trailers) {
            mMovieTrailers.put(movieId, trailers);
        }
    }

    @Override
    public void deleteAllMovies() {
        mPage = 1;
        mTotalPages = 0;
        mMovies.clear();
    }

    @Override
    public void deleteAllMovieTrailers(@NonNull String movieId) {
        mMovieTrailers.put(movieId, null);
    }
}
