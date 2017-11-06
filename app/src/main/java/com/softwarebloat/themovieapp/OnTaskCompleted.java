package com.softwarebloat.themovieapp;

import com.softwarebloat.themovieapp.DAO.MovieDAO;

import java.util.List;

interface OnTaskCompleted {
    void onTaskCompleted(List<MovieDAO> movies);
}
