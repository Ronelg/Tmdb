package com.tmdb.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ronel on 10/11/2016.
 */
public final class TmdbContract {

    public static final String CONTENT_TYPE_APP_BASE = "tikal.";

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd." + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd." + CONTENT_TYPE_APP_BASE;

    interface MoviesColumns {
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_POSTER_PATH = "movie_poster_path";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_VOTE_AVERAGE = "movie_vote_average";
    }

    interface TrailersColumns {
        String TRAILER_ID = "trailer_id";
        String TRAILER_KEY = "trailer_key";
        String TRAILER_NAME = "trailer_name";
        String TRAILER_SITE = "trailer_site";
        String TRAILER_TYPE = "trailer_type";
    }

    public static final String CONTENT_AUTHORITY = "com.tmdb.android";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_MOVIES = "movies";
    private static final String PATH_TRAILERS = "trailers";


    public static String makeContentType(String id) {
        if (id != null) {
            return CONTENT_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static String makeContentItemType(String id) {
        if (id != null) {
            return CONTENT_ITEM_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static class Movies implements MoviesColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE_ID = "movie";

        /** Build {@link Uri} for requested {@link #MOVIE_ID}. */
        public static Uri buildMovieUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        public static Uri buildTrailersDirUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).appendPath(PATH_TRAILERS).build();
        }

        /** Read {@link #MOVIE_ID} from {@link Movies} {@link Uri}. */
        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Trailers implements TrailersColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE_ID = "trailer";

        /** Build {@link Uri} for requested {@link #TRAILER_ID}. */
        public static Uri buildTrialerUri(String trailerId) {
            return CONTENT_URI.buildUpon().appendPath(trailerId).build();
        }

        /** Read {@link #TRAILER_ID} from {@link Movies} {@link Uri}. */
        public static String getTrailerId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    private TmdbContract() {
    }
}
