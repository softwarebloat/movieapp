package com.softwarebloat.themovieapp.async;

import com.softwarebloat.themovieapp.DAO.MovieDAO;

import java.util.List;

public interface OnMovieTaskCompleted {
    void onTaskCompleted(List<MovieDAO> movies);
}
