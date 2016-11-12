package com.tmdb.android.data.source;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.tmdb.android.provider.TmdbContract;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ronel on 11/11/2016.
 */

public class LoaderProvider {

    @NonNull
    private final Context mContext;

    public LoaderProvider(@NonNull Context context) {
        mContext = checkNotNull(context, "context cannot be null");
    }

    public Loader<Cursor> createMoviesLoader() {
        return new CursorLoader(mContext, TmdbContract.Movies.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    public Loader<Cursor> createMovieTrailersLoader(String movieId) {
        return new CursorLoader(mContext, TmdbContract.Movies.buildTrailersDirUri(movieId),
                null,
                null,
                null,
                null
        );
    }
}