package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.domain.validators.ReviewValidator;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerAuthorityRepository;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ReviewRepository;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Review service.
 */
@Service
@Transactional
public class ReviewServiceImpl extends AbstractServiceImpl<Review, ReviewRepository>
        implements ReviewService {

    private final CustomerAuthorityRepository customerAuthorityRepository;
    private final MovieRepository movieRepository;
    private final ReviewValidator reviewValidator;

    /**
     * Instantiates a new Review service.
     *
     * @param repository                the repository
     * @param movieRepository           the movie repository
     * @param customerAuthorityRepository the customer role def repository
     * @param reviewValidator           the review validator
     */
    public ReviewServiceImpl(ReviewRepository repository, MovieRepository movieRepository,
                             CustomerAuthorityRepository customerAuthorityRepository, ReviewValidator reviewValidator) {
        super(repository);
        this.movieRepository = movieRepository;
        this.customerAuthorityRepository = customerAuthorityRepository;
        this.reviewValidator = reviewValidator;
    }

    @Override
    protected void onDelete(Review review) {
        logger.debug("Review on delete");
        // detach Customer
        CustomerAuthority customerAuthority = review.getWriter();
        if (customerAuthority != null) {
            logger.debug("Detach customer role def: " + customerAuthority);
            customerAuthority.getReviews().remove(review);
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
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        Review review = findById(id).orElseThrow(
                () -> new NoEntityFoundException("review", "id", id));
        onDeleteInfo(review, info);
    }

    @Override
    public void onDeleteInfo(Review review, Collection<String> info) {
        info.add("Review for \"" + review.getMovie().getTitle() + "\" written by " +
                         review.getWriter().getUser().getUsername() + " will be deleted");
    }

    @Override
    public boolean existsByUserIdAndMovieId(Long userId, Long movieId)
            throws NoEntityFoundException {
        return repository.existsByUserIdAndMovieId(userId, movieId);
    }

    @Override
    public void submitReviewForm(ReviewForm reviewForm)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit review form");
        CustomerAuthority customerAuthority = customerAuthorityRepository.findByUserWithId(reviewForm.getUserId())
                                                                         .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "user id", reviewForm.getUserId()));
        Movie movie = movieRepository.findById(reviewForm.getMovieId()).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", reviewForm.getMovieId()));
        if (repository.existsByWriterAndMovie(customerAuthority, movie)) {
            throw new ClashException(customerAuthority.getUser().getUsername() + " has already written " +
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
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
        review.setRating(reviewForm.getRating());
        review.setReview(reviewForm.getReview());
        review.setCreationDateTime(LocalDateTime.now());
        review.setIsCensored(false);
        save(review);
        logger.debug("Instantiated and saved review for " + movie.getTitle() +
                             " by " + customerAuthority.getUser().getUsername());
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
    public Page<ReviewDto> findPageByMovieId(Long movieId, Pageable pageable) {
        return repository.findAllByMovieWithId(movieId, pageable)
                         .map(this::convertToDto);
    }

    @Override
    public Integer findAverageRatingOfMovie(Movie movie) {
        Integer avgRating = repository.findAverageOfReviewRatingsForMovie(movie);
        return avgRating == null ? 0 : avgRating;
    }

    @Override
    public Integer findAverageRatingOfMovieWithId(Long movieId) {
        Integer avgRating = repository.findAverageOfReviewRatingsForMovieWithId(movieId);
        return avgRating == null ? 0 : avgRating;
    }

    @Override
    public ReviewDto convertIdToDto(Long id)
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
        logger.debug("convert review to DTO: " + reviewDTO);
        return reviewDTO;
    }

}
