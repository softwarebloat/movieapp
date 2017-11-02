package com.softwarebloat.themovieapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {


    private List<String> mUrls = new ArrayList<>();

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView listItem;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.iv_poster_movie);

        }
    }

    public MoviesAdapter(List<String> posterPaths) {
        mUrls = posterPaths;
    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String posterUrl = mUrls.get(position);
        Context context = holder.listItem.getContext();

        Picasso.with(context).load(posterUrl)

                .into(holder.listItem);
    }


    @Override
    public int getItemCount() {
        return mUrls == null ? 0 : mUrls.size();
    }
}
