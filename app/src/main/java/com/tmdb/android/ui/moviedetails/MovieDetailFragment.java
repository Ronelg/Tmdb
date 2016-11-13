package com.tmdb.android.ui.moviedetails;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tmdb.android.R;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.io.model.Trailer;
import com.tmdb.android.ui.movies.MovieListActivity;
import com.tmdb.android.util.ImageLoader;
import java.util.List;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailsContract.View{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE = "movie";

    private ImageLoader mImageLoader;

    private Movie mMovie;
    private Toolbar mMovieTitle;
    private ImageView mMovieImage;
    private TextView mMovieYear;
    private TextView mMovieDuration;
    private TextView mMovieRating;
    private TextView mMovieOverview;

    private TrailersAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = new ImageLoader(getActivity().getApplicationContext(), R.drawable.ic_image_24dp);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = new Gson().fromJson(getArguments().getString(ARG_MOVIE),Movie.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        mMovieTitle = (Toolbar) rootView.findViewById(R.id.movie_title);
        mMovieImage = (ImageView) rootView.findViewById(R.id.movie_image);
        mMovieYear = (TextView) rootView.findViewById(R.id.movie_year);
        mMovieDuration = (TextView) rootView.findViewById(R.id.movie_duration);
        mMovieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        mMovieOverview = (TextView) rootView.findViewById(R.id.movie_overview);

        View recyclerView = rootView.findViewById(R.id.trailers_list);
        setupRecyclerView((RecyclerView) recyclerView);


        bindUiElements();
        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new TrailersAdapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

    }

    private void bindUiElements(){
        if(mMovie != null){
            mImageLoader.loadImage(mMovie.posterPath, mMovieImage, true);

            mMovieTitle.setTitle(mMovie.title);
            mMovieYear.setText(mMovie.getReleaseDate());
            mMovieRating.setText(String.format("%s/10", mMovie.voteAverage));
            mMovieOverview.setText(mMovie.overview);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showTrailers(List<Trailer> trailers) {
        mAdapter.setItems(trailers);
    }

    @Override
    public void showLoadingTrailersError() {

    }

    @Override
    public void showNoTrailers() {

    }

    @Override
    public void setPresenter(MovieDetailsContract.Presenter presenter) {

    }
}
