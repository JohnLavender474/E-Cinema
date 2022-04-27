package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * The type Movie service.
 */
@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository> implements MovieService {

    private final ReviewService reviewService;
    private final ScreeningService screeningService;

    /**
     * Instantiates a new Movie service.
     *
     * @param repository       the repository
     * @param reviewService    the review service
     * @param screeningService the screening service
     */
    public MovieServiceImpl(MovieRepository repository, ReviewService reviewService, ScreeningService screeningService) {
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

}
