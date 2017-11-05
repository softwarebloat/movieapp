package com.softwarebloat.themovieapp.utilities;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.softwarebloat.themovieapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MovieNetworkUtils {

    private final static String TMDB_BASE_URL = "http://api.themoviedb.org/3/";
    final static String POPULAR_ENDPOINT = "movie/popular";
    private final static String TOPRATED_ENDPOINT = "movie/top_rated";

    private final static String API_KEY_QUERY_PARAM = "api_key";
    private final static String API_KEY = BuildConfig.MOVIE_API_KEY;



    final static String POSTER_SIZE = "w185";

    public enum SortMethod {
        DEFAULT, TOPRATED, POPULAR
    }


    public static boolean isDeviceOnline(ConnectivityManager connectivityManager) {

        assert connectivityManager != null;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    public static URL buildUrl(SortMethod sortMethod) {

        String sort_endpoint = getSortEndpoint(sortMethod);

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendEncodedPath(sort_endpoint)
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

    private static String getSortEndpoint(SortMethod sortMethod) {

        if(SortMethod.TOPRATED.equals(sortMethod)) {
            return TOPRATED_ENDPOINT;
        }

        return POPULAR_ENDPOINT;
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
