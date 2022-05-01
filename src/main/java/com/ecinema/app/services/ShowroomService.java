package com.ecinema.app.services;

import com.ecinema.app.domain.EntityToDtoConverter;
import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.entities.Theater;
import com.ecinema.app.utils.Converter;
import com.ecinema.app.utils.Letter;

import java.util.List;
import java.util.Optional;

/**
 * The interface Showroom service.
 */
public interface ShowroomService extends AbstractService<Showroom>, EntityToDtoConverter<Showroom, ShowroomDto> {

    /**
     * Find by showroom letter optional.
     *
     * @param showroomLetter the showroom letter
     * @return the optional
     */
    Optional<Showroom> findByShowroomLetter(Letter showroomLetter);

    /**
     * Find by showroom seats contains optional.
     *
     * @param showroomSeat the showroom seat
     * @return the optional
     */
    Optional<Showroom> findByShowroomSeatsContains(ShowroomSeat showroomSeat);

    /**
     * Find by showroom seats contains with id optional.
     *
     * @param showroomSeatId the showroom seat id
     * @return the optional
     */
    Optional<Showroom> findByShowroomSeatsContainsWithId(Long showroomSeatId);

    /**
     * Find by screenings contains optional.
     *
     * @param screening the screening
     * @return the optional
     */
    Optional<Showroom> findByScreeningsContains(Screening screening);

    /**
     * Find by screenings contains with id optional.
     *
     * @param screeningId the screening id
     * @return the optional
     */
    Optional<Showroom> findByScreeningsContainsWithId(Long screeningId);

    /**
     * Find all by theater list.
     *
     * @param theater the theater
     * @return the list
     */
    List<Showroom> findAllByTheater(Theater theater);

    /**
     * Find all by theater with id list.
     *
     * @param theaterId the theater id
     * @return the list
     */
    List<Showroom> findAllByTheaterWithId(Long theaterId);

}
