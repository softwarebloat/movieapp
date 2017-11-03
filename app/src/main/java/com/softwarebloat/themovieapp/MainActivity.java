package com.softwarebloat.themovieapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import static android.support.v7.widget.RecyclerView.*;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private Adapter mAdapter;
    private LayoutManager mLayoutManager;

    private List<MovieDAO> movieList = new ArrayList<>();

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview_movies);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        loadMoviesListIfConnectionIsAvailable(cm);

    }

    //todo: polish needed
    private void loadMoviesListIfConnectionIsAvailable(final ConnectivityManager cm) {

        LinearLayout noConnectionItems = findViewById(R.id.no_internet_container);
        Button retryButton = findViewById(R.id.btn_retry);

        if(MovieNetworkUtils.isDeviceOnline(cm)) {
            noConnectionItems.setVisibility(INVISIBLE);
            loadMoviesData();
        } else {
            noConnectionItems.setVisibility(VISIBLE);
        }

        retryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoviesListIfConnectionIsAvailable(cm);
            }
        });
    }

    private void loadMoviesData() {
        URL movieSearchUrl = MovieNetworkUtils.buildUrl();
        new MovieQueryTask().execute(movieSearchUrl);
    }

    @Override
    public void onListItemClick(String clickedItem) {

        if(mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, clickedItem, Toast.LENGTH_LONG);
        mToast.show();
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
                    movieList.add(new MovieDAO(posterUrl, movieTitle));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mAdapter.notifyDataSetChanged();
        }
    }

}
