package com.ecinema.app.services;

import com.ecinema.app.domain.EntityToDtoConverter;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * The interface Movie service.
 */
public interface MovieService extends AbstractService<Movie>, EntityToDtoConverter<Movie, MovieDto> {

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Movie> findAll(Pageable pageable);

    /**
     * Find all dtos page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<MovieDto> pageOfDtos(Pageable pageable);

    /**
     * Find all by like title list.
     *
     * @param title the title
     * @return the list
     */
    List<Movie> findAllByLikeTitle(String title);

    /**
     * Page of dtos like title page.
     *
     * @param title the title
     * @return the page
     */
    Page<MovieDto> pageOfDtosLikeTitle(String title, Pageable pageable);

    /**
     * Find all by like title page.
     *
     * @param title    the title
     * @param pageable the pageable
     * @return the page
     */
    Page<Movie> findAllByLikeTitle(String title, Pageable pageable);

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
