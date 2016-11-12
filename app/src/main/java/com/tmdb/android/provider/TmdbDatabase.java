package com.tmdb.android.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.tmdb.android.provider.TmdbContract.*;
import static com.tmdb.android.util.LogUtils.makeLogTag;

/**
 * Helper for managing {@link SQLiteDatabase} that stores data for
 * {@link TmdbProvider}.
 *
 * Created by ronel on 10/11/2016.
 */
public class TmdbDatabase extends SQLiteOpenHelper {
    private static final String TAG = makeLogTag(TmdbDatabase.class);

    private static final String DATABASE_NAME = "tikal.db";

    private static final int RELEASE_A = 1;
    private static final int CUR_DATABASE_VERSION = RELEASE_A;

    private final Context mContext;

    interface Tables {
        String MOVIES = "movies";
        String TRAILERS = "trailers";
        String MOVIE_TRAILERS = "movie_trailers";

        String MOVIE_TRAILERS_JOIN_TRAILERS = "movie_trailers "
                + "LEFT OUTER JOIN trailers ON movie_trailers.trailer_id=trailers.trailer_id";
    }

    public interface MovieTrailers {
        String MOVIE_ID = "movie_id";
        String TRAILER_ID = "trailer_id";
    }

    private interface References {
        String MOVIE_ID = "REFERENCES " + Tables.MOVIES + "(" + Movies.MOVIE_ID + ")";
        String TRAILER_ID = "REFERENCES " + Tables.TRAILERS + "(" + Trailers.TRAILER_ID + ")";
    }

    public TmdbDatabase(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.MOVIES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesColumns.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesColumns.MOVIE_TITLE + " TEXT,"
                + MoviesColumns.MOVIE_OVERVIEW + " TEXT,"
                + MoviesColumns.MOVIE_POSTER_PATH + " TEXT,"
                + MoviesColumns.MOVIE_RELEASE_DATE + " INTEGER,"
                + MoviesColumns.MOVIE_VOTE_AVERAGE + " DOUBLE DEFAULT 0,"
                + "UNIQUE (" + MoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.TRAILERS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TrailersColumns.TRAILER_ID + " TEXT NOT NULL,"
                + TrailersColumns.TRAILER_NAME + " TEXT,"
                + TrailersColumns.TRAILER_KEY + " TEXT,"
                + TrailersColumns.TRAILER_SITE + " TEXT,"
                + TrailersColumns.TRAILER_TYPE + " TEXT,"
                + "UNIQUE (" + TrailersColumns.TRAILER_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.MOVIE_TRAILERS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieTrailers.MOVIE_ID + " TEXT NOT NULL " + References.MOVIE_ID + ","
                + MovieTrailers.TRAILER_ID + " TEXT NOT NULL " + References.TRAILER_ID + ","
                + "UNIQUE (" + MovieTrailers.MOVIE_ID + ","
                + MovieTrailers.TRAILER_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This is demo app so not implement update db
    }
}
