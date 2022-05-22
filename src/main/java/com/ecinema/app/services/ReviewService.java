package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.domain.validators.ReviewValidator;
import com.ecinema.app.exceptions.*;
import com.ecinema.app.repositories.CustomerRepository;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ReviewRepository;
import com.ecinema.app.util.UtilMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService extends AbstractEntityService<Review, ReviewRepository, ReviewDto> {

    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;
    private final ReviewValidator reviewValidator;

    public ReviewService(ReviewRepository repository, MovieRepository movieRepository,
                         CustomerRepository customerRepository, ReviewValidator reviewValidator) {
        super(repository);
        this.movieRepository = movieRepository;
        this.reviewValidator = reviewValidator;
        this.customerRepository = customerRepository;
    }

    @Override
    public void onDelete(Review review) {
        logger.debug("Review on delete");
        // detach Customer
        Customer writer = review.getWriter();
        if (writer != null) {
            logger.debug("Detach customer role def: " + writer);
            writer.getReviews().remove(review);
            review.setWriter(null);
        }
        // detatch Movie
        Movie movie = review.getMovie();
        if (movie != null) {
            logger.debug("Detach movie: " + movie);
            movie.getReviews().remove(review);
            review.setMovie(null);
        }
    }

    @Override
    public ReviewDto convertToDto(Review review) {
        ReviewDto reviewDTO = new ReviewDto();
        reviewDTO.setId(review.getId());
        reviewDTO.setReview(review.getReview());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setIsCensored(review.getIsCensored());
        reviewDTO.setCreationDateTime(review.getCreationDateTime());
        reviewDTO.setCustomerId(review.getWriter().getId());
        reviewDTO.setWriter(review.getWriter().getUser().getUsername());
        logger.debug("convert review to DTO: " + reviewDTO);
        return reviewDTO;
    }

    public boolean existsByUserIdAndMovieId(Long userId, Long movieId)
            throws NoEntityFoundException {
        return repository.existsByUserIdAndMovieId(userId, movieId);
    }

    public void submitReviewForm(ReviewForm reviewForm)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit review form");
        Customer customer = customerRepository
                .findByUserWithId(reviewForm.getUserId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "user id", reviewForm.getUserId()));
        Movie movie = movieRepository.findById(reviewForm.getMovieId()).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", reviewForm.getMovieId()));
        if (repository.existsByWriterAndMovie(customer, movie)) {
            throw new ClashException(customer.getUser().getUsername() + " has already written " +
                                             "a review for " + movie.getTitle());
        }
        List<String> errors = new ArrayList<>();
        reviewValidator.validate(reviewForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        logger.debug("Submit Review Form: passed validation checks");
        Review review = new Review();
        review.setMovie(movie);
        movie.getReviews().add(review);
        review.setWriter(customer);
        customer.getReviews().add(review);
        review.setRating(reviewForm.getRating());
        review.setReview(reviewForm.getReview());
        review.setCreationDateTime(LocalDateTime.now());
        review.setIsCensored(false);
        repository.save(review);
        logger.debug("Instantiated and saved review for " + movie.getTitle() +
                             " by " + customer.getUser().getUsername());
    }

    public List<ReviewDto> findAllDtosByMovie(Movie movie) {
        return repository.findAllByMovie(movie)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    public List<ReviewDto> findAllDtosByMovieWithId(Long movieId) {
        return repository.findAllByMovieWithId(movieId)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    public Page<ReviewDto> findPageByMovieId(Long movieId, Pageable pageable) {
        return repository.findAllByMovieWithId(movieId, pageable)
                         .map(this::convertToDto);
    }

    public Integer findAverageRatingOfMovie(Movie movie) {
        Integer avgRating = repository.findAverageOfReviewRatingsForMovie(movie);
        return avgRating == null ? 0 : avgRating;
    }

    public Integer findAverageRatingOfMovieWithId(Long movieId) {
        Integer avgRating = repository.findAverageOfReviewRatingsForMovieWithId(movieId);
        return avgRating == null ? 0 : avgRating;
    }

}

