package com.softwarebloat.themovieapp.DAO;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDAO implements Parcelable {

    private final String posterPath;
    private final String movieTitle;
    private final String releaseDate;
    private final String voteAverage;
    private final String plotSynopsis;

    public MovieDAO(String posterPath, String movieTitle, String releaseDate, String voteAverage,
                    String plotSynopsis) {
        this.posterPath = posterPath;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    private MovieDAO(Parcel in) {

        String[] data = new String[5];

        in.readStringArray(data);
        this.posterPath = data[0];
        this.movieTitle = data[1];
        this.releaseDate = data[2];
        this.voteAverage = data[3];
        this.plotSynopsis = data[4];
    }


    public String getPosterPath() {
        return posterPath;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {
                this.posterPath,
                this.movieTitle,
                this.releaseDate,
                this.voteAverage,
                this.plotSynopsis
        } );

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieDAO createFromParcel(Parcel in) {
            return new MovieDAO(in);
        }

        public MovieDAO[] newArray(int size) {
            return new MovieDAO[size];
        }
    };
}
