package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository> implements MovieService {

    public MovieServiceImpl(MovieRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Movie movie) {

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
