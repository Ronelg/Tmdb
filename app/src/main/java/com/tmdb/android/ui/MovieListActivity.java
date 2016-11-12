package com.tmdb.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.gson.Gson;
import com.tmdb.android.R;
import com.tmdb.android.data.TmdbItem;
import com.tmdb.android.data.source.LoaderProvider;
import com.tmdb.android.data.source.TmdbRepositoryProvider;
import com.tmdb.android.io.model.Movie;
import com.tmdb.android.util.ImageLoader;
import java.util.ArrayList;
import java.util.List;

import static com.tmdb.android.util.LogUtils.makeLogTag;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements MoviesContract.View{

    private static final String TAG = makeLogTag(MovieListActivity.class);
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private MoviesAdapter mAdapter;

    private MoviesPresenter mMoviesPresenter;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mImageLoader = new ImageLoader(getApplicationContext(), R.drawable.ic_image_24dp);

        // Create the presenter
        LoaderProvider loaderProvider = new LoaderProvider(this);

        mMoviesPresenter = new MoviesPresenter(this,
                TmdbRepositoryProvider.provideMoviesRepository(getApplicationContext()),
                getLoaderManager(),
                loaderProvider);

        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;

        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoviesPresenter.start();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int columns = getResources().getInteger(R.integer.num_columns);
        mAdapter = new MoviesAdapter(this, columns);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.setItems(movies);
    }

    @Override
    public void showLoadingMoviesError() {

    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void setPresenter(@NonNull MoviesContract.Presenter presenter) {

    }

    public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_MOVIE = 0;
        private static final int TYPE_LOADING_MORE = -1;

        private final Activity host;
        private final LayoutInflater layoutInflater;
        private final int columns;

        private List<TmdbItem> items;
        private boolean showLoadingMore = false;

        public MoviesAdapter(Activity hostActivity,
                int columns){
            this.host = hostActivity;
            this.columns = columns;
            layoutInflater = LayoutInflater.from(host);
            items = new ArrayList<>();
            setHasStableIds(true);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case TYPE_MOVIE:
                    return createMovieHolder(parent);
                case TYPE_LOADING_MORE:
                    return new LoadingMoreHolder(
                            layoutInflater.inflate(R.layout.infinite_loading, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_MOVIE:
                    bindMovieHolder((Movie) getItem(position), (MovieHolder) holder, position);
                    break;
                case TYPE_LOADING_MORE:{
                    bindLoadingViewHolder((LoadingMoreHolder) holder, position);
                    break;
                }
            }
        }

        private TmdbItem getItem(int position) {
            return items.get(position);
        }

        public void addItems(List<? extends TmdbItem> newItems){
            this.items.addAll(newItems);
            notifyDataSetChanged();
        }

        public void setItems(List<? extends TmdbItem> newItems){
            this.items = (List<TmdbItem>) newItems;
            notifyDataSetChanged();
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }

        public int getItemPosition(final long itemId) {
            for (int position = 0; position < items.size(); position++) {
                if (getItem(position).id == itemId) return position;
            }
            return RecyclerView.NO_POSITION;
        }

        @Override
        public long getItemId(int position) {
            if (getItemViewType(position) == TYPE_LOADING_MORE) {
                return -1L;
            }
            return getItem(position).id;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < getDataItemCount()
                    && getDataItemCount() > 0) {
                TmdbItem item = getItem(position);
                if (item instanceof Movie) {
                    return TYPE_MOVIE;
                }
            }
            return TYPE_LOADING_MORE;
        }

        @Override
        public int getItemCount() {
            return getDataItemCount() + (showLoadingMore ? 1 : 0);
        }

        public int getDataItemCount() {
            return items.size();
        }

        private MovieHolder createMovieHolder(ViewGroup parent) {
            final MovieHolder holder = new MovieHolder(layoutInflater.inflate(
                    R.layout.movie_list_content, parent, false));

            return holder;
        }

        private void bindMovieHolder(final Movie movie, final MovieHolder holder, int position) {
            mImageLoader.loadImage(movie.posterPath, holder.thumbnail, true);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_MOVIE, new Gson().toJson(movie));
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    }else {
                        Intent intent = new Intent(host, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_MOVIE,new Gson().toJson(movie));

                        host.startActivity(intent);
                    }
                }
            });

        }

        private void bindLoadingViewHolder(LoadingMoreHolder holder, int position) {
            // only show the infinite load progress spinner if there are already items in the
            // grid i.e. it's not the first item & data is being loaded
            //holder.progress.setVisibility((position > 0 && dataLoading.isDataLoading())
            //        ? View.VISIBLE : View.INVISIBLE);
        }


        /* package */  class MovieHolder extends RecyclerView.ViewHolder {

            ImageView thumbnail;

            MovieHolder(View itemView) {
                super(itemView);
                thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            }
        }

        /* package */  class LoadingMoreHolder extends RecyclerView.ViewHolder {

            ProgressBar progress;

            LoadingMoreHolder(View itemView) {
                super(itemView);
                progress = (ProgressBar) itemView;
            }

        }
    }
}
