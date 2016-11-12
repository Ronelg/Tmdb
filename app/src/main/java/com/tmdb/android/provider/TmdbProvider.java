package com.tmdb.android.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import com.tmdb.android.util.SelectionBuilder;
import java.util.ArrayList;
import java.util.Arrays;

import static com.tmdb.android.provider.TmdbContract.*;
import static com.tmdb.android.provider.TmdbDatabase.*;
import static com.tmdb.android.util.LogUtils.LOGV;
import static com.tmdb.android.util.LogUtils.makeLogTag;

/**
 * Created by ronel on 10/11/2016.
 */

public class TmdbProvider extends ContentProvider {

    private static final String TAG = makeLogTag(TmdbProvider.class);

    private TmdbDatabase mOpenHelper;

    private TmdbProviderUriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        mOpenHelper = new TmdbDatabase(getContext());
        mUriMatcher = new TmdbProviderUriMatcher();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        TmdbUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
        return matchingUriEnum.contentType;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        TmdbUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

        // Avoid the expensive string concatenation below if not loggable.
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "uri=" + uri + " code=" + matchingUriEnum.code + " proj=" +
                    Arrays.toString(projection) + " selection=" + selection + " args="
                    + Arrays.toString(selectionArgs) + ")");
        }

        switch (matchingUriEnum) {
            default: {
                // Most cases are handled with simple SelectionBuilder.
                final SelectionBuilder builder = buildExpandedSelection(uri, matchingUriEnum.code);

                Cursor cursor = builder
                        .where(selection, selectionArgs)
                        .query(db, false, projection, sortOrder, null);

                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LOGV(TAG, "insert(uri=" + uri + ", values=" + values.toString() +")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        TmdbUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
        if (matchingUriEnum.table != null) {
            db.insertOrThrow(matchingUriEnum.table, null, values);
            notifyChange(uri);
        }

        switch (matchingUriEnum) {
            case MOVIES: {
                return Movies.buildMovieUri(values.getAsString(Movies.MOVIE_ID));
            }
            case TRAILERS: {
                return Trailers.buildTrialerUri(values.getAsString(Trailers.TRAILER_ID));
            }
            case MOVIES_ID_TRAILERS: {
                return Trailers.buildTrialerUri(values.getAsString(MovieTrailers.TRAILER_ID));
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LOGV(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        TmdbUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

        final SelectionBuilder builder = buildSimpleSelection(uri);

        int retVal = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return retVal;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        LOGV(TAG, "delete(uri=" + uri + ")");

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        TmdbUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

        int retVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return retVal;
    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Notifies the system that the given {@code uri} data has changed.
     */
    private void notifyChange(Uri uri) {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);
    }
    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        TmdbUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
        // The main Uris, corresponding to the root of each type of Uri, do not have any selection
        // criteria so the full table is used. The others apply a selection criteria.
        switch (matchingUriEnum) {
            case MOVIES:
            case TRAILERS:
                return builder.table(matchingUriEnum.table);
            case MOVIES_ID: {
                final String movieId = Movies.getMovieId(uri);
                return builder.table(Tables.MOVIES)
                        .where(Movies.MOVIE_ID + "=?", movieId);
            }
            case TRAILERS_ID: {
                final String trailerId = Trailers.getTrailerId(uri);
                return builder.table(Tables.TRAILERS)
                        .where(Trailers.TRAILER_ID + "=?", trailerId);
            }
            case MOVIES_ID_TRAILERS: {
                final String movieId = Movies.getMovieId(uri);
                return builder.table(Tables.MOVIE_TRAILERS)
                        .where(Movies.MOVIE_ID + "=?", movieId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri for " + uri);
            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        TmdbUriEnum matchingUriEnum = mUriMatcher.matchCode(match);
        if (matchingUriEnum == null) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        switch (matchingUriEnum) {
            case MOVIES: {
                return builder.table(Tables.MOVIES);
            }
            case MOVIES_ID: {
                final String movieId = Movies.getMovieId(uri);
                return builder.table(Tables.MOVIES)
                        .where(Movies.MOVIE_ID + "=?", movieId);
            }
            case MOVIES_ID_TRAILERS: {
                final String movieId = Movies.getMovieId(uri);
                return builder.table(Tables.MOVIE_TRAILERS_JOIN_TRAILERS)
                        .mapToTable(Trailers._ID, Tables.TRAILERS)
                        .mapToTable(Trailers.TRAILER_ID, Tables.TRAILERS)
                        .where(Qualified.MOVIE_TRAILERS_MOVIE_ID + "=?", movieId);
            }
            case TRAILERS: {
                return builder.table(Tables.TRAILERS);
            }
            case TRAILERS_ID: {
                final String trailerId = Trailers.getTrailerId(uri);
                return builder.table(Tables.TRAILERS)
                        .where(Trailers.TRAILER_ID + "=?", trailerId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * {@link TmdbContract} fields that are fully qualified with a specific
     * parent {@link Tables}. Used when needed to work around SQL ambiguity.
     */
    private interface Qualified {
        String MOVIE_TRAILERS_MOVIE_ID = Tables.MOVIE_TRAILERS + "."
                + MovieTrailers.MOVIE_ID;
    }
}
