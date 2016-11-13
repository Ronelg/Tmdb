package com.tmdb.android.io;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import com.tmdb.android.io.model.Trailer;
import com.tmdb.android.provider.TmdbContract;
import com.tmdb.android.provider.TmdbDatabase;
import java.util.ArrayList;
import java.util.List;

import static com.tmdb.android.util.LogUtils.makeLogTag;

/**
 * Created by ronel on 10/11/2016.
 */
public class TrailersHandler extends BaseHandler<List<Trailer>> {

    private static final String TAG = makeLogTag(TrailersHandler.class);

    private ArrayList<Trailer> mTrailer = new ArrayList<Trailer>();
    private String mMovieId;

    public TrailersHandler(Context context) {
        super(context);
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = TmdbContract.Trailers.CONTENT_URI;

        //list.add(ContentProviderOperation.newDelete(uri).build());

        for (Trailer trailer : mTrailer) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(uri);
            builder.withValue(TmdbContract.Trailers.TRAILER_ID, trailer.id);
            builder.withValue(TmdbContract.Trailers.TRAILER_NAME, trailer.name);
            builder.withValue(TmdbContract.Trailers.TRAILER_KEY, trailer.key);
            builder.withValue(TmdbContract.Trailers.TRAILER_SITE, trailer.site);
            builder.withValue(TmdbContract.Trailers.TRAILER_TYPE, trailer.type);
            list.add(builder.build());
        }

        buildMovieTrailerMapping(list);
    }

    @Override
    public void process(List<Trailer> elements) {
        mTrailer.addAll(elements);
    }

    private void buildMovieTrailerMapping(ArrayList<ContentProviderOperation> list) {
        Uri uri = TmdbContract.Movies.buildTrailersDirUri(mMovieId);

        // delete any existing relationship between this movie and trailers
        list.add(ContentProviderOperation.newDelete(uri).build());

        // add relationship records to indicate the trailer for movie
        for (Trailer trailer : mTrailer) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(uri);
            builder.withValue(TmdbDatabase.MovieTrailers.MOVIE_ID, mMovieId);
            builder.withValue(TmdbDatabase.MovieTrailers.TRAILER_ID, trailer.id);
            list.add(builder.build());
        }
    }

    public void setMovieId(String movieId) {
        this.mMovieId = movieId;
    }
}
