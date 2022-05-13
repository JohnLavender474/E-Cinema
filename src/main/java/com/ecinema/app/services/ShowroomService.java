package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.dtos.ShowroomSeatDto;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.forms.ShowroomForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.domain.enums.Letter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The interface Showroom service.
 */
public interface ShowroomService extends AbstractService<Showroom>, EntityDtoConverter<Showroom, ShowroomDto> {

    /**
     * Gets showroom seat map.
     *
     * @param showroomId the showroom id
     * @return the showroom seat map
     */
    Map<Letter, Set<ShowroomSeatDto>> findShowroomSeatMapByShowroomWithId(Long showroomId);

    /**
     * Submit showroom form.
     *
     * @param showroomForm the showroom form
     * @throws InvalidArgsException the invalid args exception
     */
    void submitShowroomForm(ShowroomForm showroomForm)
            throws InvalidArgsException;

    /**
     * Find by showroom letter optional.
     *
     * @param showroomLetter the showroom letter
     * @return the optional
     */
    ShowroomDto findByShowroomLetter(Letter showroomLetter);

    /**
     * Find by showroom seats contains optional.
     *
     * @param showroomSeat the showroom seat
     * @return the optional
     */
    ShowroomDto findByShowroomSeatsContains(ShowroomSeat showroomSeat);

    /**
     * Find by showroom seats contains with id optional.
     *
     * @param showroomSeatId the showroom seat id
     * @return the optional
     */
    ShowroomDto findByShowroomSeatsContainsWithId(Long showroomSeatId);

    /**
     * Find by screenings contains optional.
     *
     * @param screening the screening
     * @return the optional
     */
    ShowroomDto findByScreeningsContains(Screening screening);

    /**
     * Find by screenings contains with id optional.
     *
     * @param screeningId the screening id
     * @return the optional
     */
    ShowroomDto findByScreeningsContainsWithId(Long screeningId);

    /**
     * Find all dtos list.
     *
     * @return the list
     */
    List<ShowroomDto> findAllDtos();

}
