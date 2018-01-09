package com.softwarebloat.themovieapp.async;


import android.os.AsyncTask;

import com.softwarebloat.themovieapp.DAO.ReviewDAO;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewQueryTask extends AsyncTask<URL, Void, String> {

    private final OnReviewTaskCompleted listener;

    public MovieReviewQueryTask(OnReviewTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        String movieReviewsSearchResults = null;

        try {
            movieReviewsSearchResults = MovieNetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieReviewsSearchResults;
    }

    @Override
    protected void onPostExecute(String searchResult) {
        List<ReviewDAO> reviewDAOList = new ArrayList<>();

        try {
            JSONObject result = new JSONObject(searchResult);
            JSONArray reviews = result.getJSONArray("results");

            for(int i = 0; i < reviews.length(); i++) {
                String author = reviews.getJSONObject(i).get("author").toString();
                String content = reviews.getJSONObject(i).get("content").toString();

                reviewDAOList.add(new ReviewDAO(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener.onReviewTaskCompleted(reviewDAOList);
    }
}
