package com.tmdb.android.provider;

/**
 * The list of {@code Uri}s recognised by the {@code ContentProvider} of the app.
 * <p />
 * It is important to order them in the order that follows {@link android.content.UriMatcher}
 * matching rules: wildcard {@code *} applies to one segment only and it processes matching per
 * segment in a tree manner over the list of {@code Uri} in the order they are added. The first
 * rule means that {@code movies / *} would not match {@code movies / id / trailers}.
 */
public enum TmdbUriEnum {
    MOVIES(100, "movies", TmdbContract.Movies.CONTENT_TYPE_ID, false, TmdbDatabase.Tables.MOVIES),
    MOVIES_ID(101, "movies/*", TmdbContract.Movies.CONTENT_TYPE_ID, true, null),
    MOVIES_ID_TRAILERS(102, "movies/*/trailers", TmdbContract.Trailers.CONTENT_TYPE_ID, false,
            TmdbDatabase.Tables.MOVIE_TRAILERS),
    TRAILERS(200, "trailers", TmdbContract.Trailers.CONTENT_TYPE_ID, false, TmdbDatabase.Tables.TRAILERS),
    TRAILERS_ID(201, "trailers/*", TmdbContract.Movies.CONTENT_TYPE_ID, true, null);


    public int code;

    /**
     * The path to the {@link android.content.UriMatcher} will use to match. * may be used as a
     * wild card for any text, and # may be used as a wild card for numbers.
     */
    public String path;

    public String contentType;

    public String table;

    TmdbUriEnum(int code, String path, String contentTypeId, boolean item, String table) {
        this.code = code;
        this.path = path;
        this.contentType = item ? TmdbContract.makeContentItemType(contentTypeId)
                : TmdbContract.makeContentType(contentTypeId);
        this.table = table;
    }
}
