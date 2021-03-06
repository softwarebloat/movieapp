package com.softwarebloat.themovieapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.async.MovieQueryTask;
import com.softwarebloat.themovieapp.async.OnMovieTaskCompleted;
import com.softwarebloat.themovieapp.data.MovieContract.MovieEntry;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.SortMethod;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.INVISIBLE;
import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.VISIBLE;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener, OnMovieTaskCompleted {

    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoConnectionItems;

    private List<MovieDAO> movieList = new ArrayList<>();

    private ConnectivityManager cm;
    LayoutManager mLayoutManager;


    private static final String SELECTED_SORT_METHOD = "SORT_METHOD";
    private int sortMethodSelected = SortMethod.DEFAULT.ordinal();

    private static final String SELECTED_MENU = "SELECTED_MENU";
    private int menuOptionSelectedId = -1;

    private static final String LIST_POSITION = "LIST_POSITION";
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState != null) {
            menuOptionSelectedId = savedInstanceState.getInt(SELECTED_MENU);
            sortMethodSelected = savedInstanceState.getInt(SELECTED_SORT_METHOD);
            position = savedInstanceState.getInt(LIST_POSITION);
        }

        mNoConnectionItems = findViewById(R.id.no_internet_container);
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);


        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        loadMovieList();
    }

    private void loadMovieList() {
        if (favoritesIsNotSelected()) {
            loadMoviesListIfConnectionIsAvailable(cm, SortMethod.values()[sortMethodSelected]);
        } else {
            loadFavoriteMovies();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovieList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_MENU, menuOptionSelectedId);
        outState.putInt(SELECTED_SORT_METHOD, sortMethodSelected);
        outState.putInt(LIST_POSITION, ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if (menuOptionSelectedId == -1) {
            menu.getItem(0).setChecked(true);
            return true;
        }

        menu.findItem(menuOptionSelectedId).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        switch (itemClickedId) {
            case R.id.action_sort_popular:
                item.setChecked(true);
                menuOptionSelectedId = itemClickedId;
                sortMethodSelected = SortMethod.POPULAR.ordinal();
                clearGridData();
                loadMoviesListIfConnectionIsAvailable(cm, SortMethod.POPULAR);
                break;
            case R.id.action_sort_toprated:
                item.setChecked(true);
                menuOptionSelectedId = itemClickedId;
                sortMethodSelected = SortMethod.TOPRATED.ordinal();
                clearGridData();
                loadMoviesListIfConnectionIsAvailable(cm, SortMethod.TOPRATED);
                break;
            case R.id.action_sort_favorites:
                item.setChecked(true);
                menuOptionSelectedId = itemClickedId;
                sortMethodSelected = SortMethod.FAVORITES.ordinal();
                clearGridData();
                loadFavoriteMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean favoritesIsNotSelected() {
        return SortMethod.values()[sortMethodSelected] != SortMethod.FAVORITES;
    }

    private void clearGridData() {
        movieList.clear();
        mAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadMoviesListIfConnectionIsAvailable(final ConnectivityManager cm, final SortMethod sortMethod) {


        Button retryButton = findViewById(R.id.btn_retry);

        if (MovieNetworkUtils.isDeviceOnline(cm)) {
            mNoConnectionItems.setVisibility(INVISIBLE);
            if (favoritesIsNotSelected()) {
                loadMoviesData(sortMethod);
            }
        } else {
            mNoConnectionItems.setVisibility(VISIBLE);
        }

        retryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoviesListIfConnectionIsAvailable(cm, sortMethod);
            }
        });
    }

    private void loadMoviesData(SortMethod sortMethod) {
        URL movieSearchUrl = MovieNetworkUtils.buildUrl(sortMethod);
        new MovieQueryTask(this).execute(movieSearchUrl);
    }

    private void loadFavoriteMovies() {

        mNoConnectionItems.setVisibility(INVISIBLE);

        String[] projection = {
                MovieEntry.MOVIE_ID,
                MovieEntry.COLUMN_MOVIE_TITLE,
                MovieEntry.COlUMN_POSTER_PATH,
                MovieEntry.COlUMN_RELEASE_DATE,
                MovieEntry.COlUMN_VOTE_AVERAGE,
                MovieEntry.COlUMN_OVERVIEW
        };

        Cursor query = getContentResolver().query(
                MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        List<MovieDAO> movies = new ArrayList<>();
        if (query != null) {

            int movieIdIndex = query.getColumnIndex(MovieEntry.MOVIE_ID);
            int movieTitleIndex = query.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE);
            int moviePosterIndex = query.getColumnIndex(MovieEntry.COlUMN_POSTER_PATH);
            int movieReleaseIndex = query.getColumnIndex(MovieEntry.COlUMN_RELEASE_DATE);
            int movieVoteIndex = query.getColumnIndex(MovieEntry.COlUMN_VOTE_AVERAGE);
            int movieOverviewIndex = query.getColumnIndex(MovieEntry.COlUMN_OVERVIEW);

            while (query.moveToNext()) {
                MovieDAO movie = new MovieDAO(
                        query.getString(movieIdIndex),
                        query.getString(moviePosterIndex),
                        query.getString(movieTitleIndex),
                        query.getString(movieReleaseIndex),
                        query.getString(movieVoteIndex),
                        query.getString(movieOverviewIndex));

                movies.add(movie);
            }
        }

        if (query != null) {
            query.close();
        }

        mRecyclerView.setAdapter(new MoviesAdapter(movies, this));
    }

    @Override
    public void onListItemClick(MovieDAO movie) {

        Intent MovieDetailActivityIntent = new Intent(this, MovieDetailActivity.class);

        MovieDetailActivityIntent.putExtra("movie_data", movie);

        startActivity(MovieDetailActivityIntent);
    }


    @Override
    public void onTaskCompleted(List<MovieDAO> movies) {
        movieList = movies;
        mAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(position);
//        mAdapter.notifyDataSetChanged();
    }
}
