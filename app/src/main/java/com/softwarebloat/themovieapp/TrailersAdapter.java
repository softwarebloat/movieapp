package com.softwarebloat.themovieapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softwarebloat.themovieapp.DAO.TrailerDAO;
import com.softwarebloat.themovieapp.utilities.MovieNetworkUtils;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private final List<TrailerDAO> trailers;
    final private ListItemClickListener mOnClickListener;

    public TrailersAdapter(List<TrailerDAO> trailers, ListItemClickListener mOnClickListener) {
        this.trailers = trailers;
        this.mOnClickListener = mOnClickListener;
    }

    public interface ListItemClickListener {
        void onTrailerItemClick(String trailerUrl);
    }

    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.trailer_list_item, parent, false);

        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersAdapterViewHolder holder, int position) {
        holder.trailer.setText(String.format("Trailer %s", position+1));
    }

    @Override
    public int getItemCount() {
        return trailers == null ? 0 : trailers.size();
    }


    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailer;

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            trailer = itemView.findViewById(R.id.tv_trailer_item);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            String trailerUrl = MovieNetworkUtils
                    .buildYoutubeTrailerUrl(trailers.get(position).getTrailerId());
            mOnClickListener.onTrailerItemClick(trailerUrl);

        }
    }
}
