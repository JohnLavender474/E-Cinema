package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.forms.ScreeningForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.domain.enums.Letter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The interface Screening service.
 */
public interface ScreeningService extends AbstractService<Screening>, EntityDtoConverter<Screening, ScreeningDto> {

    /**
     * Find screening seat map by screening with id map.
     *
     * @param screeningId the screening id
     * @return the map
     */
    Map<Letter, Set<ScreeningSeatDto>> findScreeningSeatMapByScreeningWithId(Long screeningId);

    /**
     * Submit screening form.
     *
     * @param screeningForm the screening form
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid args exception
     * @throws ClashException         the clash exception
     */
    void submitScreeningForm(ScreeningForm screeningForm)
            throws NoEntityFoundException, InvalidArgsException, ClashException;

    /**
     * Exists screening by showroom and in between start time and end time boolean.
     *
     * @param showroom  the showroom
     * @param startTime the start time
     * @param endTime   the end time
     * @return the boolean
     */
    Optional<ScreeningDto> findScreeningByShowroomAndInBetweenStartTimeAndEndTime(
            Showroom showroom, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Exists screening by showroom id and in between start time and end time boolean.
     *
     * @param showroomId the showroom id
     * @param startTime  the start time
     * @param endTime    the end time
     * @return the boolean
     */
    Optional<ScreeningDto> findScreeningByShowroomIdAndInBetweenStartTimeAndEndTime(
            Long showroomId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find all dtos page.
     *
     * @param movieId  the movie id
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
