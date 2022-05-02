package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.exceptions.NoAssociationException;
import com.ecinema.app.utils.Letter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The interface Screening seat service.
 */
public interface ScreeningSeatService extends AbstractService<ScreeningSeat>,
                                              EntityDtoConverter<ScreeningSeat, ScreeningSeatDto> {

    /**
     * Gets map of screening seats for screening with id.
     *
     * @param screeningId the screening id
     * @return the map of screening seats for screening with id
     */
    Map<Letter, Set<ScreeningSeatDto>> getMapOfScreeningSeatsForScreeningWithId(Long screeningId)
            throws NoAssociationException;

    /**
     * Find all by screening list.
     *
     * @param screening the screening
     * @return the list
     */
    List<ScreeningSeatDto> findAllByScreening(Screening screening);

    /**
     * Find all by screening with id list.
     *
     * @param screeningId the screening id
     * @return the list
     */
    List<ScreeningSeatDto> findAllByScreeningWithId(Long screeningId);

    /**
     * Find all by showroom seat list.
     *
     * @param showroomSeat the showroom seat
     * @return the list
     */
    List<ScreeningSeatDto> findAllByShowroomSeat(ShowroomSeat showroomSeat);

    /**
     * Find all by showroom seat with id list.
     *
     * @param showroomSeatId the showroom seat id
     * @return the list
     */
    List<ScreeningSeatDto> findAllByShowroomSeatWithId(Long showroomSeatId);

    /**
     * Find by ticket optional.
     *
     * @param ticket the ticket
     * @return the optional
     */
    ScreeningSeatDto findByTicket(Ticket ticket);

    /**
     * Find by ticket with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     */
    ScreeningSeatDto findByTicketWithId(Long ticketId);

}
