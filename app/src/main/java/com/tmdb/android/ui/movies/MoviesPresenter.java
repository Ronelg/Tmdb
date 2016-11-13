package com.tmdb.android.ui.movies;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.tmdb.android.data.source.LoaderProvider;
import com.tmdb.android.data.source.TmdbDataSource;
import com.tmdb.android.data.source.TmdbRepository;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.ui.recyclerview.DataLoadingSubject;
import com.tmdb.android.util.MovieUtils;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ronel on 11/11/2016.
 */

public class MoviesPresenter implements MoviesContract.Presenter,
        TmdbDataSource.GetMoviesCallback,
        TmdbRepository.LoadDataCallback,
        LoaderManager.LoaderCallbacks<Cursor>,
        DataLoadingSubject {

    public final static int MOVIES_LOADER = 1;

    private final MoviesContract.View mMoviesView;

    @NonNull
    private final TmdbRepository mTmdbRepository;

    @NonNull
    private final LoaderManager mLoaderManager;

    @NonNull
    private final LoaderProvider mLoaderProvider;

    private boolean mIsDataLoading;

    private List<DataLoadingSubject.DataLoadingCallbacks> mDataLoadingCallbacks;


    public MoviesPresenter(MoviesContract.View moviesView, @NonNull TmdbRepository tmdbRepository,
            @NonNull LoaderManager loaderManager, @NonNull LoaderProvider loaderProvider) {
        this.mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");
        this.mTmdbRepository = checkNotNull(tmdbRepository,"tmdbRepository provider cannot be null");;
        this.mLoaderManager = checkNotNull(loaderManager,"loaderManager provider cannot be null");
        this.mLoaderProvider = checkNotNull(loaderProvider,"loaderProvider provider cannot be null");
        moviesView.setPresenter(this);
    }

    @Override
    public void loadMovies(boolean forceUpdate) {
        if(!isDataLoading()) {
            mIsDataLoading = true;
            dispatchLoadingStartedCallbacks();

            mMoviesView.setLoadingIndicator(forceUpdate);
            mTmdbRepository.getMovies(forceUpdate, this);
        }
    }

    @Override
    public void start() {
        loadMovies(true);
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        if (mLoaderManager.getLoader(MOVIES_LOADER) == null) {
            mLoaderManager.initLoader(MOVIES_LOADER, null, this);
        } else {
            mLoaderManager.restartLoader(MOVIES_LOADER, null, this);
        }
        dispatchLoadingFinishedCallbacks();
        mIsDataLoading = false;
    }

    @Override
    public void onError() {
    }

    @Override
    public void onDataLoaded(Cursor data) {
        mMoviesView.setLoadingIndicator(false);
        mMoviesView.showMovies(MovieUtils.readMoviesFromCursor(data));
    }


    @Override
    public void onDataEmpty() {
        mMoviesView.setLoadingIndicator(false);
        mMoviesView.showNoMovies();
    }

    @Override
    public void onDataNotAvailable() {
        mMoviesView.setLoadingIndicator(false);
        mMoviesView.showLoadingMoviesError();
    }

    @Override
    public void onDataReset() {
        mMoviesView.showMovies(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return mLoaderProvider.createMoviesLoader();
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
        onDataReset();
    }

    @Override
    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    @Override
    public void registerCallback(DataLoadingCallbacks callback) {
        if (mDataLoadingCallbacks == null) {
            mDataLoadingCallbacks = new ArrayList<>(1);
        }
        mDataLoadingCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(DataLoadingCallbacks callback) {
        if (mDataLoadingCallbacks != null && mDataLoadingCallbacks.contains(callback)) {
            mDataLoadingCallbacks.remove(callback);
        }
    }

    private void dispatchLoadingStartedCallbacks() {
        if (mDataLoadingCallbacks == null || mDataLoadingCallbacks.isEmpty()) return;
        for (DataLoadingCallbacks loadingCallback : mDataLoadingCallbacks) {
            loadingCallback.dataStartedLoading();
        }
    }

    private void dispatchLoadingFinishedCallbacks() {
        if (mDataLoadingCallbacks == null || mDataLoadingCallbacks.isEmpty()) return;
        for (DataLoadingCallbacks loadingCallback : mDataLoadingCallbacks) {
            loadingCallback.dataFinishedLoading();
        }
    }
}
