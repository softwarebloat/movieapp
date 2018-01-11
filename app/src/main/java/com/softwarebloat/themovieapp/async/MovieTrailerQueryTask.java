package com.softwarebloat.themovieapp.async;

import android.os.AsyncTask;

import com.softwarebloat.themovieapp.DAO.TrailerDAO;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieTrailerQueryTask extends AsyncTask<URL, Void, String> {

    private final OnTrailerTaskCompleted listener;

    public MovieTrailerQueryTask(OnTrailerTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        String movieTrailerSearchResults = null;

        try {
            movieTrailerSearchResults = MovieNetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieTrailerSearchResults;
    }

    @Override
    protected void onPostExecute(String searchResult) {

        List<TrailerDAO> trailerList = new ArrayList<>();

        try {
            JSONObject result = new JSONObject(searchResult);
            JSONArray trailers = result.getJSONArray("results");

            for (int i = 0; i < trailers.length(); i++) {
                String trailerId = trailers.getJSONObject(i).get("key").toString();
                String type = trailers.getJSONObject(i).get("type").toString();

                trailerList.add(new TrailerDAO(trailerId, type));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener.onTrailerTaskCompleted(trailerList);
    }

}
