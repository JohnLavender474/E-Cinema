package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.domain.enums.MovieCategory;
import com.ecinema.app.domain.enums.MsrbRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The interface Movie service.
 */
public interface MovieService extends AbstractService<Movie>, EntityDtoConverter<Movie, MovieDto> {

    /**
     * Find dto by id optional.
     *
     * @param movieId the movie id
     * @return the optional
     */
    Optional<MovieDto> findDtoById(Long movieId);

    /**
     * Fetch as form movie form.
     *
     * @param movieId the movie id
     * @return the movie form
     * @throws NoEntityFoundException the no entity found exception
     */
    MovieForm fetchAsForm(Long movieId)
        throws NoEntityFoundException;

    /**
     * Convert title to search title string.
     *
     * @param title the title
     * @return the string
     */
    String convertTitleToSearchTitle(String title);

    /**
     * Submit movie form movie dto.
     *
     * @param movieForm the movie form
     * @throws InvalidArgsException the invalid args exception
     */
    void submitMovieForm(MovieForm movieForm)
            throws InvalidArgsException;

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Movie> findAll(Pageable pageable);

    /**
     * Find all dtos list.
     *
     * @return the list
     */
    List<MovieDto> findAllDtos();

    /**
     * Find all dtos page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<MovieDto> pageOfDtos(Pageable pageable);

    /**
     * Find by title optional.
     *
     * @param searchTitle the searchTitle
     * @return the optional
     */
    MovieDto findByTitle(String searchTitle);

    /**
     * Exists by title boolean.
     *
     * @param searchTitle the searchTitle
     * @return the boolean
     */
    boolean existsByTitle(String searchTitle);

    /**
     * Find all by like title list.
     *
     * @param title the title
     * @return the list
     */
    List<MovieDto> findAllByLikeTitle(String title);

    /**
     * Find all by like title page.
     *
     * @param title    the title
     * @param pageable the pageable
     * @return the page
     */
    Page<MovieDto> findAllByLikeTitle(String title, Pageable pageable);

    /**
     * Find all by msrb rating list.
     *
     * @param msrbRating the msrb rating
     * @return the list
     */
    List<MovieDto> findAllByMsrbRating(MsrbRating msrbRating);

    /**
     * Find all by movies categories contains list.
     *
     * @param movieCategory the movie category
     * @return the list
     */
    List<MovieDto> findAllByMoviesCategoriesContains(MovieCategory movieCategory);

    /**
     * Find all by movie categories contains list.
     *
     * @param movieCategories the movie categories
     * @return the list
     */
    List<MovieDto> findAllByMovieCategoriesContainsSet(Set<MovieCategory> movieCategories);

    /**
     * Find all order by release date ascending list.
     *
     * @return the list
     */
    List<MovieDto> findAllOrderByReleaseDateAscending();

    /**
     * Find all order by release date descending list.
     *
     * @return the list
     */
    List<MovieDto> findAllOrderByReleaseDateDescending();

    /**
     * Find all order by duration ascending list.
     *
     * @return the list
     */
    List<MovieDto> findAllOrderByDurationAscending();

    /**
     * Find all order by duration descending list.
     *
     * @return the list
     */
    List<MovieDto> findAllOrderByDurationDescending();

    /**
     * Find average rating of movie with id double.
     *
     * @param movieId the movie id
     * @return the double
     * @throws NoEntityFoundException the no entity found exception
     */
    Double findAverageRatingOfMovieWithId(Long movieId)
            throws NoEntityFoundException;

}
