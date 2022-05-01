package com.ecinema.app.services;

import com.ecinema.app.domain.EntityToDtoConverter;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;

import java.util.List;

/**
 * The interface Review service.
 */
public interface ReviewService extends AbstractService<Review>, EntityToDtoConverter<Review, ReviewDto> {

    /**
     * Find all dtos by movie list.
     *
     * @param movie the movie
     * @return the list
     */
    List<ReviewDto> findAllDtosByMovie(Movie movie);

    /**
     * Find all dtos by movie with id list.
     *
     * @param movieId the movie id
     * @return the list
     */
    List<ReviewDto> findAllDtosByMovieWithId(Long movieId);

}
