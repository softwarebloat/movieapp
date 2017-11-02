package com.softwarebloat.themovieapp.utilities;


import android.net.Uri;

import com.softwarebloat.themovieapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MovieNetworkUtils {

    //http://api.themoviedb.org/3/movie/popular?

    final static String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/popular";
    final static String POPULAR_ENDPOINT = "movie/popular";
    final static String TOPRATED_ENDPOINT = "movie/top_rated";

    final static String API_KEY_QUERY_PARAM = "api_key";
    //todo: remove from here
    final static String API_KEY = BuildConfig.MOVIE_API_KEY;



    final static String POSTER_SIZE = "w185";


    public static URL buildUrl() {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");//don't know if needed

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
