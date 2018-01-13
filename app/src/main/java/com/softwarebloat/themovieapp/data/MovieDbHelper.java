package com.softwarebloat.themovieapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.softwarebloat.themovieapp.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "favorites_movies.db";
    private static final int DATABASE_VERSION = 3;

    private static final String SQL_CREATE_FAVORITES_MOVIES_TABLE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MovieEntry.MOVIE_ID + " TEXT UNIQUE," +
                    MovieEntry.COLUMN_MOVIE_TITLE + " TEXT," +
                    MovieEntry.COlUMN_POSTER_PATH + " TEXT," +
                    MovieEntry.COlUMN_RELEASE_DATE + " TEXT," +
                    MovieEntry.COlUMN_VOTE_AVERAGE + " TEXT," +
                    MovieEntry.COlUMN_OVERVIEW + " TEXT"
                    + ");";

    private static final String SQL_DELETE_FAVORITES_MOVIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITES_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITES_MOVIES);
        onCreate(db);
    }
}
