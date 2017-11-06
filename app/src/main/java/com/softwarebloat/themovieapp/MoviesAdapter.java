package com.softwarebloat.themovieapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.softwarebloat.themovieapp.DAO.MovieDAO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.softwarebloat.themovieapp.utilities.MovieNetworkUtils.*;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    final private ListItemClickListener mOnClickListener;

    public MoviesAdapter(List<MovieDAO> posterPaths, ListItemClickListener listener) {
        movies = posterPaths;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(MovieDAO movie);
    }

    private List<MovieDAO> movies = new ArrayList<>();

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        final ImageView listItem;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.iv_poster_movie);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

            int item = getAdapterPosition();

            String posterUrl = movies.get(item).getPosterPath();

            String movieTitle = movies.get(item).getMovieTitle();
            String releaseDate = movies.get(item).getReleaseDate();
            String voteAverage = movies.get(item).getVoteAverage();
            String plotSynopsis = movies.get(item).getPlotSynopsis();

            MovieDAO movie = new MovieDAO(posterUrl, movieTitle, releaseDate, voteAverage, plotSynopsis);
            mOnClickListener.onListItemClick(movie);
        }

    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutIdForListItem = R.layout.movie_list_item;

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String posterUrl = POSTER_BASE_URL + POSTER_W185 + movies.get(position).getPosterPath();
        Context context = holder.listItem.getContext();

        //todo: handle placeholder and error
        Picasso.with(context)
                .load(posterUrl)

                .into(holder.listItem);
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }
}
