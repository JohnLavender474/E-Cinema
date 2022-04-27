package com.ecinema.app.services;

import com.ecinema.app.entities.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Screening service.
 */
public interface ScreeningService extends AbstractService<Screening> {

    /**
     * Find all by show date time less than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<Screening> findAllByShowDateTimeLessThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by show date time greater than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<Screening> findAllByShowDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by show date time between list.
     *
     * @param l1 the l 1
     * @param l2 the l 2
     * @return the list
     */
    List<Screening> findAllByShowDateTimeBetween(LocalDateTime l1, LocalDateTime l2);

    /**
     * Find all by movie list.
     *
     * @param movie the movie
     * @return the list
     */
    List<Screening> findAllByMovie(Movie movie);

    /**
     * Find all by movie with id list.
     *
     * @param movieId the movie id
     * @return the list
     */
    List<Screening> findAllByMovieWithId(Long movieId);

    /**
     * Find all by showroom list.
     *
     * @param showroom the showroom
     * @return the list
     */
    List<Screening> findAllByShowroom(Showroom showroom);

    /**
     * Find all by showroom with id list.
     *
     * @param showroomId the showroom id
     * @return the list
     */
    List<Screening> findAllByShowroomWithId(Long showroomId);

}
