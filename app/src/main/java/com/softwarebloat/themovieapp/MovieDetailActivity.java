package com.softwarebloat.themovieapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.softwarebloat.themovieapp.DAO.TrailerDAO;
import com.softwarebloat.themovieapp.async.MovieTrailerQueryTask;
import com.softwarebloat.themovieapp.async.OnTrailerTaskCompleted;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_BASE_URL;
import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.POSTER_W342;

public class MovieDetailActivity extends AppCompatActivity implements OnTrailerTaskCompleted {

    MovieDAO movie;
    String mTrailerUrl;

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

        if (intentThatStartedThisActivity.hasExtra("movie_data")) {
            movie = intentThatStartedThisActivity.getParcelableExtra("movie_data");

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

            loadMovieTrailer(movie.getMovieId());
        }
    }

    private void loadMovieTrailer(String movieId) {
        URL movieTrailerUrl = MovieNetworkUtils.buildMovieTrailerEndpoint(movieId);
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
        String trailerId = trailers.get(0).getTrailerId();
        mTrailerUrl = MovieNetworkUtils.buildYoutubeTrailerUrl(trailerId);
    }



}
