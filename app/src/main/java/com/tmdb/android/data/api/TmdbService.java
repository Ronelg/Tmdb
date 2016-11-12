package com.tmdb.android.data.api;

import com.tmdb.android.data.api.model.MoviesResponse;
import com.tmdb.android.data.api.model.TrailersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Models the The Movie DB API.
 *
 * Created by ronel on 11/11/2016.
 */
public interface TmdbService {

    String ENDPOINT = "https://api.themoviedb.org/3/";

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("page") Integer page);

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getMovieTrailers(@Path("movie_id") long movieId);
}
