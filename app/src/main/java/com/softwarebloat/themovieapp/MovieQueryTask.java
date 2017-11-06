package com.softwarebloat.themovieapp;

import android.os.AsyncTask;
import static android.support.v7.widget.RecyclerView.Adapter;


import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieQueryTask extends AsyncTask<URL, Void, String> {

    private OnTaskCompleted listener;

    MovieQueryTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

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

        List<MovieDAO> movieList = new ArrayList<>();

        try {
            JSONObject result = new JSONObject(searchResult);
            JSONArray movies = result.getJSONArray("results");

            for(int i = 0; i < movies.length(); i++) {
                String posterPath = movies.getJSONObject(i).get("poster_path").toString();

                String movieTitle = movies.getJSONObject(i).get("title").toString();
                String relaseDate = movies.getJSONObject(i).get("release_date").toString();
                String voteAverage = movies.getJSONObject(i).get("vote_average").toString();
                String plotSynopsis = movies.getJSONObject(i).get("overview").toString();

                movieList.add(new MovieDAO(posterPath, movieTitle, relaseDate, voteAverage, plotSynopsis));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener.onTaskCompleted(movieList);

    }
}