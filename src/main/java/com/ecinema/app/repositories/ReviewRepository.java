package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
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

}
