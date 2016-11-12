package com.tmdb.android.data.source.local;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.tmdb.android.data.source.TmdbDataSource;
import com.tmdb.android.io.MoviesHandler;
import com.tmdb.android.io.TrailersHandler;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.io.model.Trailer;
import com.tmdb.android.provider.TmdbContract;
import com.tmdb.android.util.MovieUtils;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ronel on 11/11/2016.
 */

public class TmdbLocalDataSource implements TmdbDataSource {

    private static TmdbLocalDataSource INSTANCE;
    private ContentResolver mContentResolver;
    private Context mContext;

    // Prevent direct instantiation.
    private TmdbLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public static TmdbLocalDataSource getInstance(@NonNull Context context,
            @NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new TmdbLocalDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getMovies(@NonNull GetMoviesCallback callback) {
        // no-op since the data is loader via Cursor Loader

        Cursor query =
                mContentResolver.query(TmdbContract.Movies.CONTENT_URI, null, null, null, null);

        callback.onMoviesLoaded(MovieUtils.readMoviesFromCursor(query));
    }

    @Override
    public void getMovieTrailers(@NonNull String movieId,
            @NonNull GetMovieTrailersCallback callback) {
        // no-op since the data is loader via Cursor Loader

    }


    @Override
    public void saveMovies(@NonNull List<Movie> movies) {
        MoviesHandler handler = new MoviesHandler(mContext);
        handler.process(movies);

        ArrayList<ContentProviderOperation> list = new ArrayList<>();
        handler.makeContentProviderOperations(list);
        try {
            mContentResolver.applyBatch(TmdbContract.CONTENT_AUTHORITY, list);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMovieTrailers(@NonNull String movieId, List<Trailer> trailers) {
        TrailersHandler handler = new TrailersHandler(mContext);
        handler.setMovieId(movieId);
        handler.process(trailers);

        ArrayList<ContentProviderOperation> list = new ArrayList<>();
        handler.makeContentProviderOperations(list);
        try {
            mContentResolver.applyBatch(TmdbContract.CONTENT_AUTHORITY, list);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllMovies() {
        mContentResolver.delete(TmdbContract.Movies.CONTENT_URI, null, null);
    }

    @Override
    public void deleteAllMovieTrailers(@NonNull String movieId) {
        mContentResolver.delete(TmdbContract.Movies.buildMovieUri(movieId), null, null);
    }
}
