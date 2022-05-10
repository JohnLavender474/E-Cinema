package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Review repository.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, AbstractRepository {

    /**
     * Exists by user id and movie id boolean.
     *
     * @param userId  the user id
     * @param movieId the movie id
     * @return the boolean
     */
    @Query("SELECT CASE WHEN count(r) > 0 THEN true ELSE false END " +
            "FROM Review r WHERE r.writer.user.id = ?1 AND r.movie.id = ?2")
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * Exists by writer and movie boolean.
     *
     * @param writer the writer
     * @param movie  the movie
     * @return the boolean
     */
    boolean existsByWriterAndMovie(CustomerRoleDef writer, Movie movie);

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
    @Query("SELECT r FROM Review r JOIN r.movie m WHERE m.id = ?1")
    List<Review> findAllByMovieWithId(Long movieId);

    /**
     * Find all by movie with id page.
     *
     * @param movieId  the movie id
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT r FROM Review r JOIN r.movie m WHERE m.id = ?1")
    Page<Review> findAllByMovieWithId(Long movieId, Pageable pageable);

    /**
     * Find average of review ratings for movie double.
     *
     * @param movie the movie
     * @return the double
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie = ?1")
    Integer findAverageOfReviewRatingsForMovie(Movie movie);

    /**
     * Find average of review rating for movie with id double.
     *
     * @param movieId the movie id
     * @return the double
     */
    @Query("SELECT AVG (r.rating) FROM Review r WHERE r.movie.id = ?1")
    Integer findAverageOfReviewRatingsForMovieWithId(Long movieId);

}
