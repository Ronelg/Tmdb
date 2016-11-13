package com.tmdb.android.util;

import android.database.Cursor;
import com.tmdb.android.io.model.Trailer;
import com.tmdb.android.provider.TmdbContract;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronel on 13/11/2016.
 */

public class TrailerUtils {

    public static List<Trailer> readTrailersFromCursor(Cursor cursor) {
        List<Trailer> trailers = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Trailer.Builder builder = new Trailer.Builder();
                builder.setId(
                        cursor.getString(cursor.getColumnIndex(TmdbContract.Trailers.TRAILER_ID)));
                builder.setKey(
                        cursor.getString(cursor.getColumnIndex(TmdbContract.Trailers.TRAILER_KEY)));
                builder.setName(
                        cursor.getString(cursor.getColumnIndex(TmdbContract.Trailers.TRAILER_NAME)));
                builder.setSite(
                        cursor.getString(cursor.getColumnIndex(TmdbContract.Trailers.TRAILER_SITE)));
                builder.setType(
                        cursor.getString(cursor.getColumnIndex(TmdbContract.Trailers.TRAILER_TYPE)));
                builder.setSize(
                        cursor.getInt(cursor.getColumnIndex(TmdbContract.Trailers.TRAILER_SITE)));

                trailers.add(builder.build());
                cursor.moveToNext();
            }
        }
        return trailers;
    }
}
