package com.ecinema.app.services.implementations;

import com.ecinema.app.dtos.MovieDto;
import com.ecinema.app.dtos.ReviewDto;
import com.ecinema.app.dtos.ScreeningDto;
import com.ecinema.app.entities.Movie;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Screening;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The type Movie service.
 */
@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository>
        implements MovieService {

    private final ScreeningService screeningService;
    private final ReviewService reviewService;

    /**
     * Instantiates a new Movie service.
     *
     * @param repository       the repository
     * @param reviewService    the review service
     * @param screeningService the screening service
     */
    public MovieServiceImpl(MovieRepository repository, ReviewService reviewService,
                            ScreeningService screeningService) {
        super(repository);
        this.reviewService = reviewService;
        this.screeningService = screeningService;
    }

    @Override
    protected void onDelete(Movie movie) {
        // cascade delete Reviews
        reviewService.deleteAll(movie.getReviews());
        // cascade delete Screenings
        screeningService.deleteAll(movie.getScreenings());
    }

    @Override
    public List<Movie> findAllByLikeTitle(String title) {
        return repository.findByTitleContaining(title);
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
    public MovieDto convert(Long entityId)
            throws NoEntityFoundException {
        Movie movie = findById(entityId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", entityId));
        MovieDto movieDTO = new MovieDto();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setImage(movie.getImage());
        movieDTO.setTrailer(movie.getTrailer());
        movieDTO.setSynopsis(movie.getSynopsis());
        movieDTO.setDuration(movie.getDuration());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setMsrbRating(movie.getMsrbRating());
        movieDTO.getCast().addAll(movie.getCast());
        movieDTO.getWriters().addAll(movie.getWriters());
        movieDTO.getMovieCategories().addAll(movie.getMovieCategories());
        List<ScreeningDto> screeningDtos = new ArrayList<>();
        for (Screening screening : movie.getScreenings()) {
            ScreeningDto screeningDTO = screeningService.convert(screening.getId());
            screeningDtos.add(screeningDTO);
        }
        movieDTO.getScreeningDtos().addAll(screeningDtos);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : movie.getReviews()) {
            ReviewDto reviewDTO = reviewService.convert(review.getId());
            reviewDtos.add(reviewDTO);
        }
        movieDTO.getReviewDtos().addAll(reviewDtos);
        return movieDTO;
    }

}
