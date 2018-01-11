package com.softwarebloat.themovieapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_BASE_URL;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_W342;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.REVIEW_ENDPOINT;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.TRAILER_KEY;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.VIDEO_TRAILER_ENDPOINT;

public class MovieDetailActivity extends AppCompatActivity implements OnTrailerTaskCompleted, OnReviewTaskCompleted {

    private MovieDAO movie;
    private String mTrailerUrl;
    private RecyclerView mReviewsRecyclerViews;
    private Adapter mReviewsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mReviewsRecyclerViews = findViewById(R.id.rv_reviews);
        mReviewsRecyclerViews.setLayoutManager(new LinearLayoutManager(this));


        TextView mMovieTitle = findViewById(R.id.tv_movie_title);
        TextView mReleaseData = findViewById(R.id.tv_release_data);
        TextView mVoteAverage = findViewById(R.id.tv_vote_average);
        TextView mPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        ImageView mPosterMovie = findViewById(R.id.iv_poster_movie);

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

            loadMovieTrailer(movieId);
            loadMovieReviews(movieId);
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

    public void addMovieToFavorites(View view) {
        Toast.makeText(this, "Added to favorite!", Toast.LENGTH_SHORT).show();
    }

    public void playMovieTrailer(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailerUrl));
        startActivity(intent);
    }

    @Override
    public void onTrailerTaskCompleted(List<TrailerDAO> trailers) {

        //TODO: find better way to filter trailer from list
        for (TrailerDAO trailer : trailers) {
            if(trailer.getType().equals(TRAILER_KEY)) {
                mTrailerUrl = MovieNetworkUtils.buildYoutubeTrailerUrl(trailer.getTrailerId());
            }
        }
    }


    @Override
    public void onReviewTaskCompleted(List<ReviewDAO> reviews) {

        if (reviews.size() > 0) {
            mReviewsAdapter = new ReviewsAdapter(reviews);
            mReviewsRecyclerViews.setAdapter(mReviewsAdapter);
        }
    }
}
