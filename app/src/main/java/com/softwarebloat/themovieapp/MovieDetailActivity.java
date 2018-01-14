package com.softwarebloat.themovieapp;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.DAO.ReviewDAO;
import com.softwarebloat.themovieapp.DAO.TrailerDAO;
import com.softwarebloat.themovieapp.async.MovieReviewQueryTask;
import com.softwarebloat.themovieapp.async.MovieTrailerQueryTask;
import com.softwarebloat.themovieapp.async.OnReviewTaskCompleted;
import com.softwarebloat.themovieapp.async.OnTrailerTaskCompleted;
import com.softwarebloat.themovieapp.data.MovieContract.MovieEntry;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_BASE_URL;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_W342;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.REVIEW_ENDPOINT;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.VIDEO_TRAILER_ENDPOINT;

public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.ListItemClickListener, OnTrailerTaskCompleted, OnReviewTaskCompleted {

    private MovieDAO movie;
    private RecyclerView mReviewsRecyclerViews;
    private RecyclerView mTrailerRecyclerView;

    private TextView mMovieTitle;
    private ImageButton mBtnFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mReviewsRecyclerViews = findViewById(R.id.rv_reviews);
        mReviewsRecyclerViews.setLayoutManager(new LinearLayoutManager(this));

        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);


        mMovieTitle = findViewById(R.id.tv_movie_title);
        TextView mReleaseData = findViewById(R.id.tv_release_data);
        TextView mVoteAverage = findViewById(R.id.tv_vote_average);
        TextView mPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        ImageView mPosterMovie = findViewById(R.id.iv_poster_movie);
        mBtnFavorite = findViewById(R.id.btn_favorite_movie);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("movie_data")) {
            movie = intentThatStartedThisActivity.getParcelableExtra("movie_data");
            String movieId = movie.getMovieId();

            mMovieTitle.setText(movie.getMovieTitle());
            mReleaseData.setText(movie.getReleaseDate().substring(0, 4));
            mVoteAverage.setText(movie.getVoteAverage());
            mPlotSynopsis.setText(movie.getPlotSynopsis());

            String posterUrl = POSTER_BASE_URL + POSTER_W342 + movie.getPosterPath();

            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.mipmap.ic_placeholder_icon)
                    .error(R.mipmap.ic_placeholder_icon)
                    .into(mPosterMovie);

            if (movieIsAlreadyInFavorites()) {
                setFavoriteChecked();
            } else {
                setFavoriteNotChecked();
            }

            mBtnFavorite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (movieIsAlreadyInFavorites()) {
                        removeMovieFromFavorites();
                    } else {
                        addMovieToFavorites();
                    }
                }
            });

            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (MovieNetworkUtils.isDeviceOnline(cm)) {
                loadMovieTrailer(movieId);
                loadMovieReviews(movieId);
            }
        }
    }

    private void loadMovieReviews(String movieId) {
        URL movieReviewUrl = MovieNetworkUtils
                .buildMovieAdditionalInfoEndpointUrl(movieId, REVIEW_ENDPOINT);
        new MovieReviewQueryTask(this).execute(movieReviewUrl);
    }

    private void loadMovieTrailer(String movieId) {
        URL movieTrailerUrl = MovieNetworkUtils
                .buildMovieAdditionalInfoEndpointUrl(movieId, VIDEO_TRAILER_ENDPOINT);
        new MovieTrailerQueryTask(this).execute(movieTrailerUrl);
    }

    private boolean movieIsAlreadyInFavorites() {

        String[] projection = {MovieEntry.MOVIE_ID};
        String where = MovieEntry.MOVIE_ID + " LIKE ?";
        String[] selectionArgs = {movie.getMovieId()};

        Cursor query = getContentResolver().query(
                MovieEntry.CONTENT_URI,
                projection,
                where,
                selectionArgs,
                null
        );

        boolean cursorsHasElements = false;
        if(query != null) {
            cursorsHasElements = query.getCount() > 0;
            query.close();
        }

        return cursorsHasElements;
    }

    private void addMovieToFavorites() {

        ContentValues values = new ContentValues();
        values.put(MovieEntry.MOVIE_ID, movie.getMovieId());
        values.put(MovieEntry.COLUMN_MOVIE_TITLE, movie.getMovieTitle());
        values.put(MovieEntry.COlUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COlUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COlUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieEntry.COlUMN_OVERVIEW, movie.getPlotSynopsis());


        getContentResolver().insert(
                MovieEntry.CONTENT_URI,
                values
        );

        Toast.makeText(this, mMovieTitle.getText().toString() + " added to favorite!", Toast.LENGTH_SHORT).show();
        setFavoriteChecked();
    }

    private void removeMovieFromFavorites() {

        String where = MovieEntry.MOVIE_ID + " LIKE ?";
        String[] selectionArgs = {movie.getMovieId()};

        int rowsDeleted = getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                where,
                selectionArgs
        );

        if (rowsDeleted == 1) {
            setFavoriteNotChecked();
        } else {
            Toast.makeText(this, "Error removing movie from favorites", Toast.LENGTH_SHORT).show();
        }

    }

    private void setFavoriteChecked() {
        mBtnFavorite.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));
    }

    private void setFavoriteNotChecked() {
        mBtnFavorite.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_off));
    }


    @Override
    public void onTrailerTaskCompleted(List<TrailerDAO> trailers) {

        if(trailers.size() > 0) {
            TrailersAdapter trailersAdapter = new TrailersAdapter(trailers, this);
            mTrailerRecyclerView.setAdapter(trailersAdapter);
        }
    }


    @Override
    public void onTrailerItemClick(String trailerUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
        startActivity(intent);
    }

    @Override
    public void onReviewTaskCompleted(List<ReviewDAO> reviews) {

        if (reviews.size() > 0) {
            Adapter mReviewsAdapter = new ReviewsAdapter(reviews);
            mReviewsRecyclerViews.setAdapter(mReviewsAdapter);
        }
    }
}
