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

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    final private ListItemClickListener mOnClickListener;

    public MoviesAdapter(List<MovieDAO> posterPaths, ListItemClickListener listener) {
        movies = posterPaths;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(String clickedItem);
    }

    private List<MovieDAO> movies = new ArrayList<>();

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView listItem;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.iv_poster_movie);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(movies.get(getAdapterPosition()).getMovieTitle());
        }

    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutIdForListItem = R.layout.movie_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String posterUrl = movies.get(position).getPosterUrl();
        Context context = holder.listItem.getContext();

        Picasso.with(context)
                .load(posterUrl)
                .into(holder.listItem);
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }
}
