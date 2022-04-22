package com.ecinema.app.repositories;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.entities.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void findAllByMovie() {
        // given
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Review review = new Review();
            Movie movie = i % 2 == 0 ? movie1 : movie2;
            review.setMovie(movie);
            movie.getReviews().add(review);
            reviewRepository.save(review);
            reviews.add(review);
        }
        List<Review> control = reviews.stream()
                .filter(review -> review.getMovie().equals(movie1))
                .collect(Collectors.toList());
        // when
        List<Review> test = reviewRepository.findAllByMovie(movie1);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByMovieWithId() {
        // given
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Review review = new Review();
            Movie movie = i % 2 == 0 ? movie1 : movie2;
            review.setMovie(movie);
            movie.getReviews().add(review);
            reviewRepository.save(review);
            reviews.add(review);
        }
        List<Review> control = reviews.stream()
                .filter(review -> review.getMovie().getId().equals(movie1.getId()))
                .collect(Collectors.toList());
        // when
        List<Review> test = reviewRepository.findAllByMovieWithId(movie1.getId());
        // then
        assertEquals(control, test);
    }

}