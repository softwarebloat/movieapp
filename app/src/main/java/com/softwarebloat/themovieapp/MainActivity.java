package com.softwarebloat.themovieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<MovieDAO> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter(movieList);
        mRecyclerView.setAdapter(mAdapter);

        loadMoviesData();
    }

    private void loadMoviesData() {
        URL movieSearchUrl = MovieNetworkUtils.buildUrl();
        new MovieQueryTask().execute(movieSearchUrl);
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
