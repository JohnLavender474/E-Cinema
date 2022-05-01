package com.ecinema.app.services;

import com.ecinema.app.domain.EntityToDtoConverter;
import com.ecinema.app.domain.dtos.ShowroomSeatDto;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.utils.Converter;
import com.ecinema.app.utils.Letter;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.util.List;
import java.util.Optional;

/**
 * The interface Showroom seat service.
 */
public interface ShowroomSeatService extends AbstractService<ShowroomSeat>,
                                             EntityToDtoConverter<ShowroomSeat, ShowroomSeatDto> {

    /**
     * Find all by showroom list.
     *
     * @param showroom the showroom
     * @return the list
     */
    List<ShowroomSeat> findAllByShowroom(Showroom showroom);

    /**
     * Find all by showroom with id list.
     *
     * @param showroomId the showroom id
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<ShowroomSeat> findAllByShowroomWithId(Long showroomId)
            throws NoEntityFoundException;

    /**
     * Find all by showroom and row letter list.
     *
     * @param showroom  the showroom
     * @param rowLetter the row letter
     * @return the list
     */
    List<ShowroomSeat> findAllByShowroomAndRowLetter(Showroom showroom, Letter rowLetter);

    /**
     * Find all by showroom with id and row letter list.
     *
     * @param showroomId the showroom id
     * @param rowLetter  the row letter
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<ShowroomSeat> findAllByShowroomWithIdAndRowLetter(Long showroomId, Letter rowLetter)
            throws NoEntityFoundException;

    /**
     * Find by screening seats contains optional.
     *
     * @param screeningSeat the screening seat
     * @return the optional
     */
    Optional<ShowroomSeat> findByScreeningSeatsContains(ScreeningSeat screeningSeat);

    /**
     * Find by screening seats contains with id optional.
     *
     * @param screeningSeatId the screening seat id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    Optional<ShowroomSeat> findByScreeningSeatsContainsWithId(Long screeningSeatId)
            throws NoEntityFoundException;

    /**
     * Find by showroom and row letter and seat number optional.
     *
     * @param showroom   the showroom
     * @param rowLetter  the row letter
     * @param seatNumber the seat number
     * @return the optional
     */
    Optional<ShowroomSeat> findByShowroomAndRowLetterAndSeatNumber(Showroom showroom, Letter rowLetter,
                                                                   Integer seatNumber);

    /**
     * Find by showroom with id and row letter and seat number optional.
     *
     * @param showroomId the showroom id
     * @param rowLetter  the row letter
     * @param seatNumber the seat number
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    Optional<ShowroomSeat> findByShowroomWithIdAndRowLetterAndSeatNumber(Long showroomId, Letter rowLetter,
                                                                         Integer seatNumber)
            throws NoEntityFoundException;

}
