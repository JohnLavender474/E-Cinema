package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.utils.Duration;
import com.ecinema.app.domain.enums.MovieCategory;
import com.ecinema.app.domain.enums.MsrbRating;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.domain.validators.MovieValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Movie service.
 */
@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository>
        implements MovieService {

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
    public MovieServiceImpl(MovieRepository repository, ReviewService reviewService,
                            ScreeningService screeningService, MovieValidator movieValidator) {
        super(repository);
        this.reviewService = reviewService;
        this.screeningService = screeningService;
        this.movieValidator = movieValidator;
    }

    @Override
    protected void onDelete(Movie movie) {
        // cascade delete Reviews
        reviewService.deleteAll(movie.getReviews());
        // cascade delete Screenings
        screeningService.deleteAll(movie.getScreenings());
    }

    @Override
    public Optional<MovieDto> findDtoById(Long movieId) {
        return findById(movieId).map(this::convertToDto);
    }

    @Override
    public MovieForm fetchAsForm(Long movieId)
            throws NoEntityFoundException {
        Movie movie = findById(movieId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieId));
        MovieForm movieForm = new MovieForm();
        movieForm.setId(movie.getId());
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
        movieForm.getMovieCategories().addAll(
                movie.getMovieCategories()
                     .stream().map(MovieCategory::name)
                     .collect(Collectors.toList()));
        return movieForm;
    }

    @Override
    public String convertTitleToSearchTitle(String title) {
        return UtilMethods.removeWhitespace(title).toUpperCase();
    }

    @Override
    public void submitMovieForm(MovieForm movieForm)
            throws InvalidArgsException {
        List<String> errors = new ArrayList<>();
        movieValidator.validate(movieForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        logger.debug("Movie form passed validation checks");
        Movie movie = movieForm.getId() != null ? findById(movieForm.getId()).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieForm.getId())) : new Movie();
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
        movie.getMovieCategories().addAll(
                movieForm.getMovieCategories()
                        .stream().map(MovieCategory::valueOf)
                        .collect(Collectors.toList()));
        logger.debug("Instantiated and saved Movie entity");
        save(movie);
    }

    @Override
    public List<MovieDto> findAllDtos() {
        return convertToDto(findAll());
    }

    @Override
    public Page<MovieDto> pageOfDtos(Pageable pageable) {
        return findAll(pageable).map(this::convertToDto);
    }

    @Override
    public MovieDto findByTitle(String title) {
        String searchTitle = convertTitleToSearchTitle(title);
        Movie movie = repository.findBySearchTitle(searchTitle)
                .orElseThrow(() -> new NoEntityFoundException("movie", "title", title));
        return convertToDto(movie);
    }

    @Override
    public boolean existsByTitle(String title) {
        String searchTitle = convertTitleToSearchTitle(title);
        return repository.existsBySearchTitle(searchTitle);
    }

    @Override
    public List<MovieDto> findAllByLikeTitle(String title) {
        String searchTitle = convertTitleToSearchTitle(title);
        return convertToDto(repository.findBySearchTitleContaining(searchTitle));
    }

    @Override
    public Page<MovieDto> findAllByLikeTitle(String title, Pageable pageable) {
        String searchTitle = convertTitleToSearchTitle(title);
        return repository.findBySearchTitleContaining(searchTitle, pageable)
                .map(this::convertToDto);
    }

    @Override
    public List<MovieDto> findAllByMsrbRating(MsrbRating msrbRating) {
        return convertToDto(repository.findAllByMsrbRating(msrbRating));
    }

    @Override
    public List<MovieDto> findAllByMoviesCategoriesContains(MovieCategory movieCategory) {
        return convertToDto(repository.findAllByMoviesCategoriesContains(movieCategory));
    }

    @Override
    public List<MovieDto> findAllByMovieCategoriesContainsSet(Set<MovieCategory> movieCategories) {
        return convertToDto(repository.findAllByMovieCategoriesContainsSet(movieCategories));
    }

    @Override
    public List<MovieDto> findAllOrderByReleaseDateAscending() {
        return convertToDto(repository.findAllOrderByReleaseDateAscending());
    }

    @Override
    public List<MovieDto> findAllOrderByReleaseDateDescending() {
        return convertToDto(repository.findAllOrderByReleaseDateDescending());
    }

    @Override
    public List<MovieDto> findAllOrderByDurationAscending() {
        return convertToDto(repository.findAllOrderByDurationAscending());
    }

    @Override
    public List<MovieDto> findAllOrderByDurationDescending() {
        return convertToDto(repository.findAllOrderByDurationDescending());
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
