package com.softwarebloat.themovieapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.squareup.picasso.Picasso;

import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_W342;
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
        ImageButton mFavoriteMovie = findViewById(R.id.btn_favorite_movie);

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity.hasExtra("movie_data")) {
            MovieDAO movie = intentThatStartedThisActivity.getParcelableExtra("movie_data");

            mMovieTitle.setText(movie.getMovieTitle());
            mReleaseData.setText(movie.getReleaseDate().substring(0,4));
            mVoteAverage.setText(movie.getVoteAverage());
            mPlotSynopsis.setText(movie.getPlotSynopsis());

            String posterUrl = POSTER_BASE_URL + POSTER_W342 + movie.getPosterPath();

            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.mipmap.ic_placeholder_icon)
                    .error(R.mipmap.ic_placeholder_icon)
                    .into(mPosterMovie);
        }
    }

    public void addMovieToFavorites(View view) {
        Toast.makeText(this, "Added to favorite!", Toast.LENGTH_SHORT).show();
    }
}
