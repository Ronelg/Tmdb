package com.tmdb.android.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import com.tmdb.android.data.source.local.TmdbLocalDataSource;
import com.tmdb.android.data.source.remote.TmdbRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ronel on 11/11/2016.
 */
public class TmdbRepositoryProvider {

    public static TmdbRepository provideMoviesRepository(@NonNull Context context) {
        checkNotNull(context);
        return TmdbRepository.getInstance(provideRemoteDataSource(), provideLocalDataSource(context));
    }

    public static TmdbDataSource provideRemoteDataSource() {
        return TmdbRemoteDataSource.getInstance();
    }

    public static TmdbLocalDataSource provideLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        return TmdbLocalDataSource.getInstance(context, context.getContentResolver());
    }
}
