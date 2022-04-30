package com.ecinema.app.services;

import com.ecinema.app.dtos.TheaterDto;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.utils.Converter;

import java.util.Optional;

/**
 * The interface Theater service.
 */
public interface TheaterService extends AbstractService<Theater>, Converter<TheaterDto, Long> {

    /**
     * Find by theater name optional.
     *
     * @param theaterName the theater name
     * @return the optional
     */
    Optional<Theater> findByTheaterName(String theaterName);

    /**
     * Find by theater number optional.
     *
     * @param theaterNumber the theater number
     * @return the optional
     */
    Optional<Theater> findByTheaterNumber(Integer theaterNumber);

    /**
     * Add showroom to theater.
     *
     * @param theaterId   the theater id
     * @param screeningId the screening id
     * @throws NoEntityFoundException the no entity found exception
     */
    void addShowroomToTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException;

    /**
     * Remove showroom from theater.
     *
     * @param theaterId   the theater id
     * @param screeningId the screening id
     * @throws NoEntityFoundException the no entity found exception
     */
    void removeShowroomFromTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException;

    /**
     * Add screening to theater.
     *
     * @param theaterId   the theater id
     * @param screeningId the screening id
     * @throws NoEntityFoundException the no entity found exception
     */
    void addScreeningToTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException;

    /**
     * Remove screening from theater.
     *
     * @param theaterId   the theater id
     * @param screeningId the screening id
     * @throws NoEntityFoundException the no entity found exception
     */
    void removeScreeningFromTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException;

}
