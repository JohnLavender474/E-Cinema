package com.ecinema.app.repositories;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMovie(Movie movie);

    @Query("SELECT r FROM Review r JOIN r.movie m WHERE m.id = ?1")
    List<Review> findAllByMovieWithId(Long movieId);

}
