package com.softwarebloat.themovieapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        TextView mMovieTitle = findViewById(R.id.tv_movie_title);
        TextView mReleaseData = findViewById(R.id.tv_release_data);
        TextView mVoteAverage = findViewById(R.id.tv_vote_average);
        TextView mPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        ImageView mPosterMovie = findViewById(R.id.iv_poster_movie);

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity.hasExtra("movie_data")) {
            MovieDAO movie = intentThatStartedThisActivity.getParcelableExtra("movie_data");

            mMovieTitle.setText(movie.getMovieTitle());
            mReleaseData.setText(movie.getReleaseDate());
            mVoteAverage.setText(movie.getVoteAverage());
            mPlotSynopsis.setText(movie.getPlotSynopsis());

            Picasso.with(this)
                    .load(movie.getPosterUrl())
                    .resize(185, 278)
                    .into(mPosterMovie);
        }
    }
}
