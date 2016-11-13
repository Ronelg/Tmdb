package com.tmdb.android.ui.moviedetails;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import com.google.gson.Gson;
import com.tmdb.android.R;
import com.tmdb.android.data.source.LoaderProvider;
import com.tmdb.android.data.source.TmdbRepositoryProvider;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.ui.movies.MovieListActivity;
import com.tmdb.android.util.ImageLoader;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private MovieDetailsPresenter mPresenter;

    private ImageLoader mImageLoader;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailFragment.ARG_MOVIE,
                    getIntent().getStringExtra(MovieDetailFragment.ARG_MOVIE));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment, "MovieDetail")
                    .commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        mMovie = new Gson().fromJson(getIntent().getStringExtra(MovieDetailFragment.ARG_MOVIE),
                Movie.class);

        if (fragment instanceof MovieDetailFragment) {
            MovieDetailFragment frag = (MovieDetailFragment) fragment;

            // Create the presenter
            LoaderProvider loaderProvider = new LoaderProvider(this);

            mPresenter = new MovieDetailsPresenter(String.valueOf(mMovie.id),
                    frag,
                    TmdbRepositoryProvider.provideMoviesRepository(getApplicationContext()),
                    getLoaderManager(),
                    loaderProvider);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
