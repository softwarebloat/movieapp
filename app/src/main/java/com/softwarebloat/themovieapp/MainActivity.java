package com.softwarebloat.themovieapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.async.MovieQueryTask;
import com.softwarebloat.themovieapp.async.OnMovieTaskCompleted;
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

    private List<MovieDAO> movieList = new ArrayList<>();

    private ConnectivityManager cm;

    private static final String SELECTED_SORT_METHOD = "SORT_METHOD";
    private int sortMethodSelected = SortMethod.DEFAULT.ordinal();

    private static final String SELECTED_MENU = "SELECTED_MENU";
    private int menuOptionSelectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            menuOptionSelectedId = savedInstanceState.getInt(SELECTED_MENU);
            sortMethodSelected = savedInstanceState.getInt(SELECTED_SORT_METHOD);
        }

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mRecyclerView.setHasFixedSize(true);

        LayoutManager mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!isFavoritesSelected()) {
            loadMoviesListIfConnectionIsAvailable(cm, SortMethod.values()[sortMethodSelected]);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_MENU, menuOptionSelectedId);
        outState.putInt(SELECTED_SORT_METHOD, sortMethodSelected);
        super.onSaveInstanceState(outState);
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
                //TODO load data from cursor
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isFavoritesSelected() {
        return SortMethod.values()[sortMethodSelected] == SortMethod.FAVORITES;
    }

    private void clearGridData() {
        movieList.clear();
        mAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadMoviesListIfConnectionIsAvailable(final ConnectivityManager cm, final SortMethod sortMethod) {

        LinearLayout noConnectionItems = findViewById(R.id.no_internet_container);
        Button retryButton = findViewById(R.id.btn_retry);

        if (MovieNetworkUtils.isDeviceOnline(cm)) {
            noConnectionItems.setVisibility(INVISIBLE);
            if (!isFavoritesSelected()) {
                loadMoviesData(sortMethod);
            }
        } else {
            noConnectionItems.setVisibility(VISIBLE);
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
//        mAdapter.notifyDataSetChanged();
    }
}
