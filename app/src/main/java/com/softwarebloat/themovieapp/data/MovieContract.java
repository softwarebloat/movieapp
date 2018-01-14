package com.softwarebloat.themovieapp.data;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    static final String CONTENT_AUTHORITY = "com.softwarebloat.themovieapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "favorites_movies";
        public static final String MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COlUMN_POSTER_PATH = "poster_path";
        public static final String COlUMN_RELEASE_DATE = "release_date";
        public static final String COlUMN_VOTE_AVERAGE = "vote_average";
        public static final String COlUMN_OVERVIEW = "overview";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
