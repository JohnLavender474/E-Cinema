package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Screening service.
 */
public interface ScreeningService extends AbstractService<Screening>, EntityDtoConverter<Screening, ScreeningDto> {

    /**
     * Find all dtos page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<ScreeningDto> findPageByMovieId(Long movieId, Pageable pageable);

    /**
     * Find all by show date time less than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<ScreeningDto> findAllByShowDateTimeLessThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by show date time greater than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<ScreeningDto> findAllByShowDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by show date time between list.
     *
     * @param l1 the l 1
     * @param l2 the l 2
     * @return the list
     */
    List<ScreeningDto> findAllByShowDateTimeBetween(LocalDateTime l1, LocalDateTime l2);

    /**
     * Find all by movie list.
     *
     * @param movie the movie
     * @return the list
     */
    List<ScreeningDto> findAllByMovie(Movie movie);

    /**
     * Find all by movie with id list.
     *
     * @param movieId the movie id
     * @return the list
     */
    List<ScreeningDto> findAllByMovieWithId(Long movieId);

    /**
     * Find all by showroom list.
     *
     * @param showroom the showroom
     * @return the list
     */
    List<ScreeningDto> findAllByShowroom(Showroom showroom);

    /**
     * Find all by showroom with id list.
     *
     * @param showroomId the showroom id
     * @return the list
     */
    List<ScreeningDto> findAllByShowroomWithId(Long showroomId);

}
