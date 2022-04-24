package com.ecinema.app.services;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;

import java.util.List;
import java.util.Set;

/**
 * The interface Movie service.
 */
public interface MovieService extends AbstractService<Movie> {

    /**
     * Find all by msrb rating list.
     *
     * @param msrbRating the msrb rating
     * @return the list
     */
    List<Movie> findAllByMsrbRating(MsrbRating msrbRating);

    /**
     * Find all by movies categories contains list.
     *
     * @param movieCategory the movie category
     * @return the list
     */
    List<Movie> findAllByMoviesCategoriesContains(MovieCategory movieCategory);

    /**
     * Find all by movie categories contains list.
     *
     * @param movieCategories the movie categories
     * @return the list
     */
    List<Movie> findAllByMovieCategoriesContainsSet(Set<MovieCategory> movieCategories);

    /**
     * Find all order by release date ascending list.
     *
     * @return the list
     */
    List<Movie> findAllOrderByReleaseDateAscending();

    /**
     * Find all order by release date descending list.
     *
     * @return the list
     */
    List<Movie> findAllOrderByReleaseDateDescending();

    /**
     * Find all order by duration ascending list.
     *
     * @return the list
     */
    List<Movie> findAllOrderByDurationAscending();

    /**
     * Find all order by duration descending list.
     *
     * @return the list
     */
    List<Movie> findAllOrderByDurationDescending();

}
