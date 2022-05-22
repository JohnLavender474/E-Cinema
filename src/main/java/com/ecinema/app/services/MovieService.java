package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.validators.MovieValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.domain.enums.MovieCategory;
import com.ecinema.app.domain.enums.MsrbRating;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.domain.objects.Duration;
import com.ecinema.app.util.UtilMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MovieService extends AbstractEntityService<Movie, MovieRepository, MovieDto> {

    private final ScreeningService screeningService;
    private final ReviewService reviewService;
    private final MovieValidator movieValidator;

    /**
     * Instantiates a new Movie service.
     *
     * @param repository             the repository
     * @param reviewService          the review service
     * @param screeningService       the screening service
     * @param movieValidator         the movie validator
     */
    public MovieService(MovieRepository repository, ReviewService reviewService,
                        ScreeningService screeningService, MovieValidator movieValidator) {
        super(repository);
        this.reviewService = reviewService;
        this.screeningService = screeningService;
        this.movieValidator = movieValidator;
    }

    public static String convertTitleToSearchTitle(String title) {
        return UtilMethods.removeWhitespace(title).toUpperCase();
    }

    @Override
    public void onDelete(Movie movie) {
        logger.debug("Movie on delete");
        // cascade delete Reviews
        logger.debug("Deleting all associated reviews");
        reviewService.deleteAll(movie.getReviews());
        // cascade delete Screenings
        logger.debug("Deleting all associated screenings");
        screeningService.deleteAll(movie.getScreenings());
    }

    @Override
    public MovieDto convertToDto(Movie movie) {
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
        logger.debug("Converted movie to DTO: " + movieDTO);
        logger.debug("Movie: " + movie);
        return movieDTO;
    }

    public MovieForm fetchAsForm(Long movieId)
            throws NoEntityFoundException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Fetching movie as form");
        Movie movie = repository.findById(movieId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieId));
        logger.debug("Found movie with id: " + movieId);
        MovieForm movieForm = new MovieForm();
        movieForm.setId(movieId);
        movieForm.setTitle(movie.getTitle());
        movieForm.setDirector(movie.getDirector());
        movieForm.setImage(movie.getImage());
        movieForm.setTrailer(movie.getTrailer());
        movieForm.setSynopsis(movie.getSynopsis());
        movieForm.setHours(movie.getDuration().getHours());
        movieForm.setMinutes(movie.getDuration().getMinutes());
        movieForm.setReleaseYear(movie.getReleaseDate().getYear());
        movieForm.setReleaseMonth(movie.getReleaseDate().getMonth());
        movieForm.setReleaseDay(movie.getReleaseDate().getDayOfMonth());
        movieForm.setMsrbRating(movie.getMsrbRating());
        movieForm.getCast().addAll(movie.getCast());
        movieForm.getWriters().addAll(movie.getWriters());
        movieForm.getMovieCategories().addAll(movie.getMovieCategories());
        logger.debug("Instantiated new movie form: " + movieForm);
        logger.debug("Movie: " + movie);
        return movieForm;
    }

    public void submitMovieForm(MovieForm movieForm)
            throws InvalidArgsException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit movie form: " + movieForm);
        List<String> errors = new ArrayList<>();
        movieValidator.validate(movieForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        logger.debug("Movie form passed validation checks");
        Movie movie = movieForm.getId() != null ? repository.findById(movieForm.getId()).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieForm.getId())) : new Movie();
        logger.debug("Movie before set to form: " + movie);
        String searchTitle = convertTitleToSearchTitle(movieForm.getTitle());
        movie.setSearchTitle(searchTitle);
        movie.setTitle(movieForm.getTitle());
        movie.setImage(movieForm.getImage());
        movie.setTrailer(movieForm.getTrailer());
        movie.setDirector(movieForm.getDirector());
        movie.setSynopsis(movieForm.getSynopsis());
        movie.setDuration(new Duration(movieForm.getHours(), movieForm.getMinutes()));
        movie.setReleaseDate(LocalDate.of(movieForm.getReleaseYear(),
                                          movieForm.getReleaseMonth(),
                                          movieForm.getReleaseDay()));
        movie.setMsrbRating(movieForm.getMsrbRating());
        movie.getCast().clear();
        movie.getCast().addAll(movieForm.getCast());
        movie.getWriters().clear();
        movie.getWriters().addAll(movieForm.getWriters());
        movie.getMovieCategories().clear();
        movie.getMovieCategories().addAll(movieForm.getMovieCategories());
        logger.debug("Saved movie: " + movie);
        repository.save(movie);
    }

    public MovieDto findByTitle(String title) {
        String searchTitle = convertTitleToSearchTitle(title);
        Movie movie = repository.findBySearchTitle(searchTitle).orElseThrow(
                () -> new NoEntityFoundException("movie", "title", title));
        return convertToDto(movie);
    }

    public boolean existsByTitle(String title) {
        String searchTitle = convertTitleToSearchTitle(title);
        return repository.existsBySearchTitle(searchTitle);
    }

    public List<MovieDto> findAllByLikeTitle(String title) {
        String searchTitle = convertTitleToSearchTitle(title);
        return convertToDto(repository.findBySearchTitleContaining(searchTitle));
    }

    public Page<MovieDto> findAllByLikeTitle(String title, Pageable pageable) {
        String searchTitle = convertTitleToSearchTitle(title);
        return repository.findBySearchTitleContaining(searchTitle, pageable)
                         .map(this::convertToDto);
    }

    public List<MovieDto> findAllByMsrbRating(MsrbRating msrbRating) {
        return convertToDto(repository.findAllByMsrbRating(msrbRating));
    }

    public List<MovieDto> findAllByMoviesCategoriesContains(MovieCategory movieCategory) {
        return convertToDto(repository.findAllByMoviesCategoriesContains(movieCategory));
    }

    public List<MovieDto> findAllByMovieCategoriesContainsSet(Set<MovieCategory> movieCategories) {
        return convertToDto(repository.findAllByMovieCategoriesContainsSet(movieCategories));
    }

    public List<MovieDto> findAllOrderByReleaseDateAscending() {
        return convertToDto(repository.findAllOrderByReleaseDateAscending());
    }

    public List<MovieDto> findAllOrderByReleaseDateDescending() {
        return convertToDto(repository.findAllOrderByReleaseDateDescending());
    }

    public List<MovieDto> findAllOrderByDurationAscending() {
        return convertToDto(repository.findAllOrderByDurationAscending());
    }

    public List<MovieDto> findAllOrderByDurationDescending() {
        return convertToDto(repository.findAllOrderByDurationDescending());
    }

    public Integer findAverageRatingOfMovieWithId(Long movieId) {
        Movie movie = repository.findById(movieId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieId));
        Integer avgRating = reviewService.findAverageRatingOfMovie(movie);
        return avgRating == null ? 0 : avgRating;
    }

}
