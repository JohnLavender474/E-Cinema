package com.ecinema.app.services;

import com.ecinema.app.dtos.ReviewDto;
import com.ecinema.app.entities.Movie;
import com.ecinema.app.entities.Review;
import com.ecinema.app.utils.Converter;

import java.util.List;

/**
 * The interface Review service.
 */
public interface ReviewService extends AbstractService<Review>, Converter<ReviewDto, Long> {

    /**
     * Find all by movie list.
     *
     * @param movie the movie
     * @return the list
     */
    List<Review> findAllByMovie(Movie movie);

    /**
     * Find all by movie with id list.
     *
     * @param movieId the movie id
     * @return the list
     */
    List<Review> findAllByMovieWithId(Long movieId);

}
