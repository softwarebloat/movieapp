package com.softwarebloat.themovieapp.DAO;

public class MovieDAO {

    private String posterUrl;
    private String movieTitle;

    public MovieDAO(String posterUrl, String movieTitle) {
        this.posterUrl = posterUrl;
        this.movieTitle = movieTitle;
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
}
