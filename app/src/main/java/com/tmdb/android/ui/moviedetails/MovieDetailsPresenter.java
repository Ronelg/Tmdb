package com.tmdb.android.ui.moviedetails;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.tmdb.android.data.source.LoaderProvider;
import com.tmdb.android.data.source.TmdbDataSource;
import com.tmdb.android.data.source.TmdbRepository;
import com.tmdb.android.io.model.Trailer;
import com.tmdb.android.util.TrailerUtils;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ronel on 12/11/2016.
 */

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter,
        TmdbDataSource.GetMovieTrailersCallback,
        TmdbRepository.LoadDataCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    public final static int MOVIE_TRAILERS_LOADER = 0x2;

    private final MovieDetailsContract.View mMovieTrailersView;

    @NonNull
    private final TmdbRepository mTmdbRepository;

    @NonNull
    private final LoaderManager mLoaderManager;

    @NonNull
    private final LoaderProvider mLoaderProvider;

    private String mMovieId;


    public MovieDetailsPresenter(@NonNull String movieId,
            MovieDetailsContract.View movieTrailersView,
            @NonNull TmdbRepository tmdbRepository,
            @NonNull LoaderManager loaderManager,
            @NonNull LoaderProvider loaderProvider) {
        this.mMovieId =  checkNotNull(movieId, "movieId cannot be null!");
        this.mMovieTrailersView = checkNotNull(movieTrailersView, "movieTrailersView cannot be null!");
        this.mTmdbRepository = checkNotNull(tmdbRepository,"tmdbRepository provider cannot be null");;
        this.mLoaderManager = checkNotNull(loaderManager,"loaderManager provider cannot be null");
        this.mLoaderProvider = checkNotNull(loaderProvider,"loaderProvider provider cannot be null");
        movieTrailersView.setPresenter(this);
    }

    @Override
    public void loadMovieTrailers(String movieId) {
        mMovieTrailersView.setLoadingIndicator(true);
        mTmdbRepository.getMovieTrailers(movieId, this);
    }

    @Override
    public void start() {
        loadMovieTrailers(mMovieId);
    }

    @Override
    public void onMovieTrailersLoaded(List<Trailer> trailers) {
        mLoaderManager.restartLoader(MOVIE_TRAILERS_LOADER, null, this);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onDataLoaded(Cursor data) {
        mMovieTrailersView.setLoadingIndicator(false);
        mMovieTrailersView.showTrailers(TrailerUtils.readTrailersFromCursor(data));
    }

    @Override
    public void onDataEmpty() {
        mMovieTrailersView.setLoadingIndicator(false);
        mMovieTrailersView.showNoTrailers();
    }

    @Override
    public void onDataNotAvailable() {
        mMovieTrailersView.setLoadingIndicator(false);
        mMovieTrailersView.showLoadingTrailersError();
    }

    @Override
    public void onDataReset() {
        mMovieTrailersView.showTrailers(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return mLoaderProvider.createMovieTrailersLoader(mMovieId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            } else {
                onDataEmpty();
            }
        } else {
            onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
