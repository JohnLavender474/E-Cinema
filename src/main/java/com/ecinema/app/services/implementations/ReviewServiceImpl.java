package com.ecinema.app.services.implementations;

import ch.qos.logback.classic.pattern.Abbreviator;
import com.ecinema.app.domain.dtos.CustomerRoleDefDto;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.domain.validators.ReviewValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ReviewRepository;
import com.ecinema.app.services.CustomerRoleDefService;
import com.ecinema.app.services.ReviewService;
import org.modelmapper.internal.asm.tree.ModuleExportNode;
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
public class ReviewServiceImpl extends AbstractServiceImpl<Review, ReviewRepository>
        implements ReviewService {

    private final CustomerRoleDefRepository customerRoleDefRepository;
    private final MovieRepository movieRepository;
    private final ReviewValidator reviewValidator;

    public ReviewServiceImpl(ReviewRepository repository, MovieRepository movieRepository,
                             CustomerRoleDefRepository customerRoleDefRepository, ReviewValidator reviewValidator) {
        super(repository);
        this.movieRepository = movieRepository;
        this.customerRoleDefRepository = customerRoleDefRepository;
        this.reviewValidator = reviewValidator;
    }

    @Override
    protected void onDelete(Review review) {
        // detach Customer
        CustomerRoleDef customerRoleDef = review.getWriter();
        if (customerRoleDef != null) {
            customerRoleDef.getReviews().remove(review);
            review.setWriter(null);
        }
        // detatch Movie
        Movie movie = review.getMovie();
        if (movie != null) {
            movie.getReviews().remove(review);
            review.setMovie(null);
        }
    }

    @Override
    public void submitReviewForm(ReviewForm reviewForm)
            throws NoEntityFoundException, InvalidArgsException {
        CustomerRoleDef customerRoleDef = customerRoleDefRepository.findByUserWithId(reviewForm.getUserId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "user id", reviewForm.getUserId()));
        Movie movie = movieRepository.findById(reviewForm.getMovieId()).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", reviewForm.getMovieId()));
        List<String> errors = new ArrayList<>();
        reviewValidator.validate(reviewForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        Review review = new Review();
        review.setMovie(movie);
        movie.getReviews().add(review);
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        review.setRating(reviewForm.getRating());
        review.setReview(reviewForm.getReview());
        review.setCreationDateTime(LocalDateTime.now());
        review.setIsCensored(false);
        save(review);
    }

    @Override
    public List<ReviewDto> findAllDtosByMovie(Movie movie) {
        return repository.findAllByMovie(movie)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> findAllDtosByMovieWithId(Long movieId) {
        return repository.findAllByMovieWithId(movieId)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewDto> findPageOfDtos(Long movieId, Pageable pageable) {
        return repository.findAllByMovieWithId(movieId, pageable)
                         .map(this::convertToDto);
    }

    @Override
    public Double findAverageRatingOfMovie(Movie movie) {
        Double avgRating = repository.findAverageOfReviewRatingsForMovie(movie);
        return avgRating == null ? 0D : avgRating;
    }

    @Override
    public Double findAverageRatingOfMovieWithId(Long movieId) {
        Double avgRating = repository.findAverageOfReviewRatingsForMovieWithId(movieId);
        return avgRating == null ? 0D : avgRating;
    }

    @Override
    public ReviewDto convertToDto(Long id)
            throws NoEntityFoundException {
        Review review = findById(id).orElseThrow(
                () -> new NoEntityFoundException("review", "id", id));
        ReviewDto reviewDTO = new ReviewDto();
        reviewDTO.setId(review.getId());
        reviewDTO.setReview(review.getReview());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setIsCensored(review.getIsCensored());
        reviewDTO.setCreationDateTime(review.getCreationDateTime());
        reviewDTO.setCustomerId(review.getWriter().getId());
        reviewDTO.setWriter(review.getWriter().getUser().getUsername());
        return reviewDTO;
    }

}
