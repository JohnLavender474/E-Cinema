package com.ecinema.app.domain.contracts;

public interface IReview {
    void setReview(String review);
    String getReview();
    void setRating(Integer rating);
    Integer getRating();
}
