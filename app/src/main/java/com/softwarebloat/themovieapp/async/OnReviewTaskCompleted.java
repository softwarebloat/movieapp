package com.softwarebloat.themovieapp.async;


import com.softwarebloat.themovieapp.DAO.ReviewDAO;

import java.util.List;

public interface OnReviewTaskCompleted {
    void onReviewTaskCompleted(List<ReviewDAO> reviews);
}
