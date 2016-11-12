package com.tmdb.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.tikaldemo.android.R;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.util.ImageUtils;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE = "movie";

    private Movie mMovie;
    private ImageView mMovieImage;
    private TextView mMovieYear;
    private TextView mMovieDuration;
    private TextView mMovieRating;
    private TextView mMovieOverview;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_MOVIE));
            mMovie = new Gson().fromJson(getArguments().getString(ARG_MOVIE),Movie.class);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(
                    R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mMovie.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        mMovieImage = (ImageView) rootView.findViewById(R.id.movie_image);
        mMovieYear = (TextView) rootView.findViewById(R.id.movie_year);
        mMovieDuration = (TextView) rootView.findViewById(R.id.movie_duration);
        mMovieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        mMovieOverview = (TextView) rootView.findViewById(R.id.movie_overview);

        bindUiElements();
        return rootView;
    }

    private void bindUiElements(){
        if(mMovie != null){
            Glide.with(this)
                    .load(ImageUtils.getImagePath(mMovie.posterPath))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mMovieImage);

            mMovieYear.setText(mMovie.releaseDate);
            mMovieRating.setText(String.format("%s/10", mMovie.voteAverage));
            mMovieOverview.setText(mMovie.overview);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
