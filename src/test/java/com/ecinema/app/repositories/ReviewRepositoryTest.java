package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.utils.UtilMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.CommonDataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private CustomerRoleDefRepository customerRoleDefRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Test
    void findAverageOfReviews() {
        // given
        Movie movie = new Movie();
        movieRepository.save(movie);
        List<Integer> ratings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Review review = new Review();
            review.setMovie(movie);
            movie.getReviews().add(review);
            Integer rating = UtilMethods.randomIntBetween(0, 10);
            ratings.add(rating);
            review.setRating(rating);
            reviewRepository.save(review);
        }
        Double avgRating = 0D;
        for (Integer rating : ratings) {
            avgRating += rating;
        }
        avgRating /= ratings.size();
        Integer controlAvgRating = (int) Math.round(avgRating);
        // when
        Integer testAvgRating = reviewRepository.findAverageOfReviewRatingsForMovie(movie);
        // then
        assertEquals(controlAvgRating, testAvgRating);
    }

    @Test
    void findByUserIdAndMovieId() {
        // given
        User user = new User();
        userRepository.save(user);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
        customerRoleDefRepository.save(customerRoleDef);
        Movie movie = new Movie();
        movieRepository.save(movie);
        Review review = new Review();
        review.setMovie(movie);
        movie.getReviews().add(review);
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        reviewRepository.save(review);
        // when
        boolean test = reviewRepository.existsByUserIdAndMovieId(user.getId(), movie.getId());
        // then
        assertTrue(test);
    }

}