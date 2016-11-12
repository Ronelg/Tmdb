package com.tmdb.android.ui;

import com.tmdb.android.BasePresenter;
import com.tmdb.android.BaseView;
import com.tmdb.android.io.model.Movie;
import java.util.List;

/**
 * Created by ronel on 11/11/2016.
 */

public class MoviesContract {

    public interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showLoadingMoviesError();

        void showNoMovies();
    }

    public interface Presenter extends BasePresenter {

        void loadMovies();

    }
}
