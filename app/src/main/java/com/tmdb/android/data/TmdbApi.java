package com.tmdb.android.data;

import com.tikaldemo.android.BuildConfig;
import com.tmdb.android.data.api.ClientAuthInterceptor;
import com.tmdb.android.data.api.TmdbService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ronel on 11/11/2016.
 */

public class TmdbApi {

    private static volatile TmdbApi sSingleton;
    private TmdbService mApi;

    public static TmdbApi getInstanse() {
        if (sSingleton == null) {
            synchronized (TmdbApi.class) {
                sSingleton = new TmdbApi();
            }
        }
        return sSingleton;
    }

    public TmdbService getApi() {
        if (mApi == null) createApi();
        return mApi;
    }

    private void createApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ClientAuthInterceptor(BuildConfig.IMDB_API_KEY))
                .addInterceptor(logging)
                .build();

        mApi = new Retrofit.Builder()
                .baseUrl(TmdbService.ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TmdbService.class);
    }
}

