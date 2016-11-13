package com.tmdb.android.ui.moviedetails;

import com.tmdb.android.BasePresenter;
import com.tmdb.android.BaseView;
import com.tmdb.android.io.model.Trailer;
import java.util.List;

/**
 * Created by ronel on 12/11/2016.
 */

public class MovieDetailsContract {
    public interface View extends BaseView<MovieDetailsContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showTrailers(List<Trailer> trailers);

        void showLoadingTrailersError();

        void showNoTrailers();
    }

    public interface Presenter extends BasePresenter {

        void loadMovieTrailers(String movieId);

    }
}
