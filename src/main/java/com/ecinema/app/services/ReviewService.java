package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.exceptions.ClashException;
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
     * Report review.
     *
     * @param customerId the customer id
     * @param reviewId   the review id
     * @throws NoEntityFoundException the no entity found exception
     * @throws ClashException         the clash exception
     */
    void reportReview(Long customerId, Long reviewId)
        throws NoEntityFoundException, ClashException;

    /**
     * Exists by user id and movie id boolean.
     *
     * @param userId  the user id
     * @param movieId the movie id
     * @return the boolean
     * @throws NoEntityFoundException the no entity found exception
     */
    boolean existsByUserIdAndMovieId(Long userId, Long movieId)
            throws NoEntityFoundException;

    /**
     * Submit review form.
     *
     * @param reviewForm the review form
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid args exception
     * @throws ClashException         the clash exception
     */
    void submitReviewForm(ReviewForm reviewForm)
            throws NoEntityFoundException, InvalidArgsException, ClashException;

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
    Page<ReviewDto> findPageByMovieId(Long movieId, Pageable pageable);

    /**
     * Find average rating of movie double.
     *
     * @param movie the movie
     * @return the double
     */
    Integer findAverageRatingOfMovie(Movie movie);

    /**
     * Find average rating of movie with id double.
     *
     * @param movieId the movie id
     * @return the double
     */
    Integer findAverageRatingOfMovieWithId(Long movieId);

}
