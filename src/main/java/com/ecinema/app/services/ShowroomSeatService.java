package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ShowroomSeatDto;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.exceptions.NoAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.domain.enums.Letter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The interface Showroom seat service.
 */
public interface ShowroomSeatService extends AbstractService<ShowroomSeat>,
                                             EntityDtoConverter<ShowroomSeat, ShowroomSeatDto> {

    /**
     * Find showroom seat map by showroom with id map.
     *
     * @param showroomId the showroom id
     * @return the map
     */
    Map<Letter, Set<ShowroomSeatDto>> findShowroomSeatMapByShowroomWithId(Long showroomId)
            throws NoAssociationException;

    /**
     * Find all by showroom list.
     *
     * @param showroom the showroom
     * @return the list
     */
    List<ShowroomSeatDto> findAllByShowroom(Showroom showroom);

    /**
     * Find all by showroom with id list.
     *
     * @param showroomId the showroom id
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<ShowroomSeatDto> findAllByShowroomWithId(Long showroomId)
            throws NoEntityFoundException;

    /**
     * Find all by showroom and row letter list.
     *
     * @param showroom  the showroom
     * @param rowLetter the row letter
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<ShowroomSeatDto> findAllByShowroomAndRowLetter(Showroom showroom, Letter rowLetter)
            throws NoEntityFoundException;

    /**
     * Find all by showroom with id and row letter list.
     *
     * @param showroomId the showroom id
     * @param rowLetter  the row letter
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<ShowroomSeatDto> findAllByShowroomWithIdAndRowLetter(Long showroomId, Letter rowLetter)
            throws NoEntityFoundException;

    /**
     * Find by screening seats contains optional.
     *
     * @param screeningSeat the screening seat
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    ShowroomSeatDto findByScreeningSeatsContains(ScreeningSeat screeningSeat)
            throws NoEntityFoundException;

    /**
     * Find by screening seats contains with id optional.
     *
     * @param screeningSeatId the screening seat id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    ShowroomSeatDto findByScreeningSeatsContainsWithId(Long screeningSeatId)
            throws NoEntityFoundException;

    /**
     * Find by showroom and row letter and seat number optional.
     *
     * @param showroom   the showroom
     * @param rowLetter  the row letter
     * @param seatNumber the seat number
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    ShowroomSeatDto findByShowroomAndRowLetterAndSeatNumber(
            Showroom showroom, Letter rowLetter, Integer seatNumber)
            throws NoEntityFoundException;

    /**
     * Find by showroom with id and row letter and seat number optional.
     *
     * @param showroomId the showroom id
     * @param rowLetter  the row letter
     * @param seatNumber the seat number
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    ShowroomSeatDto findByShowroomWithIdAndRowLetterAndSeatNumber(
            Long showroomId, Letter rowLetter, Integer seatNumber)
            throws NoEntityFoundException;

}
