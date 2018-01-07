package com.softwarebloat.themovieapp.async;

import com.softwarebloat.themovieapp.DAO.TrailerDAO;

import java.util.List;

public interface OnTrailerTaskCompleted {
    void onTrailerTaskCompleted(List<TrailerDAO> trailers);
}
