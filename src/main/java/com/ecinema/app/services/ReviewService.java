package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Review service.
 */
public interface ReviewService extends AbstractService<Review>, EntityDtoConverter<Review, ReviewDto> {

    /**
     * Submit review form.
     *
     * @param reviewForm the review form
     */
    void submitReviewForm(ReviewForm reviewForm)
            throws NoEntityFoundException, InvalidArgsException;

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

    /**
     * Find page of dtos page.
     *
     * @param movieId  the movie id
     * @param pageable the pageable
     * @return the page
     */
    Page<ReviewDto> findPageOfDtos(Long movieId, Pageable pageable);

    /**
     * Find average rating of movie double.
     *
     * @param movie the movie
     * @return the double
     */
    Double findAverageRatingOfMovie(Movie movie);

    /**
     * Find average rating of movie with id double.
     *
     * @param movieId the movie id
     * @return the double
     */
    Double findAverageRatingOfMovieWithId(Long movieId);

}
