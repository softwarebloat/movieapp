package com.softwarebloat.themovieapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.INVISIBLE;
import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.VISIBLE;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private Adapter mAdapter;
    private LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    private List<MovieDAO> movieList = new ArrayList<>();

    private Toast mToast;
    private ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);


        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        loadMoviesListIfConnectionIsAvailable(cm, MovieNetworkUtils.SortMethod.DEFAULT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        switch (itemClickedId) {
            case R.id.action_sort_popular :
                item.setChecked(true);
                clearGridData();
                loadMoviesListIfConnectionIsAvailable(cm, MovieNetworkUtils.SortMethod.POPULAR);
                break;
            case R.id.action_sort_toprated :
                item.setChecked(true);
                clearGridData();
                loadMoviesListIfConnectionIsAvailable(cm, MovieNetworkUtils.SortMethod.TOPRATED);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearGridData() {
        movieList.clear();
        mAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    //todo: polish needed
    private void loadMoviesListIfConnectionIsAvailable(final ConnectivityManager cm, final MovieNetworkUtils.SortMethod sortMethod) {

        LinearLayout noConnectionItems = findViewById(R.id.no_internet_container);
        Button retryButton = findViewById(R.id.btn_retry);

        if(MovieNetworkUtils.isDeviceOnline(cm)) {
            noConnectionItems.setVisibility(INVISIBLE);
            loadMoviesData(sortMethod);
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

    private void loadMoviesData(MovieNetworkUtils.SortMethod sortMethod) {
        URL movieSearchUrl = MovieNetworkUtils.buildUrl(sortMethod);
        new MovieQueryTask().execute(movieSearchUrl);
    }

    @Override
    public void onListItemClick(MovieDAO movie) {

        Intent MovieDetailActivityIntent = new Intent(this, MovieDetailActivity.class);

        MovieDetailActivityIntent.putExtra("movie_data", movie);

        startActivity(MovieDetailActivityIntent);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String moviesSearchResults = null;

            try {
                moviesSearchResults = MovieNetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return moviesSearchResults;
        }

        @Override
        protected void onPostExecute(String searchResult) {

            try {
                JSONObject result = new JSONObject(searchResult);
                JSONArray movies = result.getJSONArray("results");

                for(int i = 0; i < movies.length(); i++) {
                    String posterPath = movies.getJSONObject(i).get("poster_path").toString();
                    String posterUrl = "http://image.tmdb.org/t/p/w185/" + posterPath;

                    String movieTitle = movies.getJSONObject(i).get("title").toString();
                    String relaseDate = movies.getJSONObject(i).get("release_date").toString();
                    String voteAverage = movies.getJSONObject(i).get("vote_average").toString();
                    String plotSynopsis = movies.getJSONObject(i).get("overview").toString();

                    movieList.add(new MovieDAO(posterUrl, movieTitle, relaseDate, voteAverage, plotSynopsis));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mAdapter.notifyDataSetChanged();
        }
    }

}
