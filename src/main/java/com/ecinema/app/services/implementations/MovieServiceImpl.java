package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.services.CustomerRoleDefService;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.utils.Duration;
import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.validators.MovieValidator;
import com.ecinema.app.validators.ReviewValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The type Movie service.
 */
@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository>
        implements MovieService {

    private final CustomerRoleDefService customerRoleDefService;
    private final ScreeningService screeningService;
    private final ReviewService reviewService;
    private final MovieValidator movieValidator;
    private final ReviewValidator reviewValidator;

    /**
     * Instantiates a new Movie service.
     *
     * @param repository       the repository
     * @param reviewService    the review service
     * @param screeningService the screening service
     */
    public MovieServiceImpl(MovieRepository repository, ReviewService reviewService,
                            ScreeningService screeningService, CustomerRoleDefService customerRoleDefService,
                            MovieValidator movieValidator, ReviewValidator reviewValidator) {
        super(repository);
        this.reviewService = reviewService;
        this.screeningService = screeningService;
        this.customerRoleDefService = customerRoleDefService;
        this.movieValidator = movieValidator;
        this.reviewValidator = reviewValidator;
    }

    @Override
    protected void onDelete(Movie movie) {
        // cascade delete Reviews
        reviewService.deleteAll(movie.getReviews());
        // cascade delete Screenings
        screeningService.deleteAll(movie.getScreenings());
    }

    @Override
    public void submitReviewForm(ReviewForm reviewForm)
            throws NoEntityFoundException {
        List<String> errors = new ArrayList<>();
        reviewValidator.validate(reviewForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        CustomerRoleDef customerRoleDef = customerRoleDefService.findByUserWithId(reviewForm.getUserId())
                                                                .orElseThrow(() -> new NoEntityFoundException("user",
                                                                                                              "id",
                                                                                                              reviewForm.getUserId()));
        Movie movie = findById(reviewForm.getMovieId())
                .orElseThrow(() -> new NoEntityFoundException("movie", "id", reviewForm.getMovieId()));
        Review review = new Review();
        review.setReview(reviewForm.getReview());
        review.setRating(reviewForm.getRating());
        review.setIsCensored(false);
        review.setCreationDateTime(LocalDateTime.now());
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        review.setMovie(movie);
        movie.getReviews().add(review);
        reviewService.save(review);
    }

    @Override
    public String convertTitleToSearchTitle(String title) {
        return UtilMethods.removeWhitespace(title).toUpperCase();
    }

    @Override
    public void submitMovieForm(MovieForm movieForm)
            throws ClashException, InvalidArgsException {
        List<String> errors = new ArrayList<>();
        movieValidator.validate(movieForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        String searchTitle = convertTitleToSearchTitle(movieForm.getTitle());
        if (existsBySearchTitle(searchTitle)) {
            throw new ClashException("Movie \"" + movieForm.getTitle() + "\" already exists");
        }
        Movie movie = new Movie();
        movie.setSearchTitle(searchTitle);
        movie.setTitle(movieForm.getTitle());
        movie.setImage(movieForm.getImage());
        movie.setTrailer(movieForm.getTrailer());
        movie.setSynopsis(movieForm.getSynopsis());
        movie.setDuration(new Duration(movieForm.getHours(), movieForm.getMinutes()));
        movie.setReleaseDate(LocalDate.of(movieForm.getReleaseYear(),
                                          movieForm.getReleaseMonth(),
                                          movieForm.getReleaseDay()));
        movie.setMsrbRating(movieForm.getMsrbRating());
        movie.getCast().addAll(movieForm.getCast());
        movie.getWriters().addAll(movieForm.getWriters());
        movie.getMovieCategories().addAll(movieForm.getMovieCategories());
        save(movie);
    }

    @Override
    public Page<MovieDto> pageOfDtos(Pageable pageable) {
        return findAll(pageable).map(this::convertToDto);
    }

    @Override
    public Optional<Movie> findBySearchTitle(String title) {
        return repository.findByTitle(title.toUpperCase());
    }

    @Override
    public boolean existsBySearchTitle(String title) {
        return repository.existsBySearchTitle(title);
    }

    @Override
    public List<Movie> findAllByLikeTitle(String title) {
        return repository.findByTitleContaining(title);
    }

    @Override
    public Page<MovieDto> pageOfDtosLikeTitle(String title, Pageable pageable) {
        return findAllByLikeTitle(title, pageable).map(this::convertToDto);
    }

    @Override
    public Page<Movie> findAllByLikeTitle(String title, Pageable pageable) {
        return repository.findByTitleContaining(title, pageable);
    }

    @Override
    public List<Movie> findAllByMsrbRating(MsrbRating msrbRating) {
        return repository.findAllByMsrbRating(msrbRating);
    }

    @Override
    public List<Movie> findAllByMoviesCategoriesContains(MovieCategory movieCategory) {
        return repository.findAllByMoviesCategoriesContains(movieCategory);
    }

    @Override
    public List<Movie> findAllByMovieCategoriesContainsSet(Set<MovieCategory> movieCategories) {
        return repository.findAllByMovieCategoriesContainsSet(movieCategories);
    }

    @Override
    public List<Movie> findAllOrderByReleaseDateAscending() {
        return repository.findAllOrderByReleaseDateAscending();
    }

    @Override
    public List<Movie> findAllOrderByReleaseDateDescending() {
        return repository.findAllOrderByReleaseDateDescending();
    }

    @Override
    public List<Movie> findAllOrderByDurationAscending() {
        return repository.findAllOrderByDurationAscending();
    }

    @Override
    public List<Movie> findAllOrderByDurationDescending() {
        return repository.findAllOrderByDurationDescending();
    }

    @Override
    public Double findAverageRatingOfMovieWithId(Long movieId) {
        Movie movie = findById(movieId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieId));
        Double avgRating = reviewService.findAverageRatingOfMovie(movie);
        return avgRating == null ? 0D : avgRating;
    }

    @Override
    public MovieDto convertToDto(Long id)
            throws NoEntityFoundException {
        Movie movie = findById(id).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", id));
        MovieDto movieDTO = new MovieDto();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setImage(movie.getImage());
        movieDTO.setTrailer(movie.getTrailer());
        movieDTO.setSynopsis(movie.getSynopsis());
        movieDTO.setDuration(movie.getDuration());
        movieDTO.setReleaseYear(movie.getReleaseDate().getYear());
        movieDTO.setReleaseDay(movie.getReleaseDate().getDayOfMonth());
        movieDTO.setReleaseMonth(movie.getReleaseDate().getMonth());
        movieDTO.setMsrbRating(movie.getMsrbRating());
        movieDTO.getCast().addAll(movie.getCast());
        movieDTO.getWriters().addAll(movie.getWriters());
        movieDTO.getMovieCategories().addAll(movie.getMovieCategories());
        return movieDTO;
    }

}
