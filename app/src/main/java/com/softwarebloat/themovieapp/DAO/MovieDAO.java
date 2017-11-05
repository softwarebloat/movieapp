package com.softwarebloat.themovieapp.DAO;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDAO implements Parcelable {

    private String posterUrl;
    private String movieTitle;
    private String releaseDate;
    private String voteAverage;
    private String plotSynopsis;

    public MovieDAO(String posterUrl, String movieTitle, String releaseDate, String voteAverage,
                    String plotSynopsis) {
        this.posterUrl = posterUrl;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public MovieDAO(Parcel in) {

        String[] data = new String[5];

        in.readStringArray(data);
        this.posterUrl = data[0];
        this.movieTitle = data[1];
        this.releaseDate = data[2];
        this.voteAverage = data[3];
        this.plotSynopsis = data[4];
    }


    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {
                this.posterUrl,
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
