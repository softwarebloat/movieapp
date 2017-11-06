package com.softwarebloat.themovieapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.squareup.picasso.Picasso;

import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_BASE_URL;

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
            mReleaseData.setText(movie.getReleaseDate().substring(0,4));
            mVoteAverage.setText(movie.getVoteAverage());
            mPlotSynopsis.setText(movie.getPlotSynopsis());

            String posterUrl = POSTER_BASE_URL + POSTER + movie.getPosterPath();

            //todo: handle placeholder and error
            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.drawable.not_found)
                    .error(R.drawable.not_found)
                    .into(mPosterMovie);
        }
    }
}
