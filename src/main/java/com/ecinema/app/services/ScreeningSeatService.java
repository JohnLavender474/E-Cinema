package com.ecinema.app.services;

import com.ecinema.app.entities.Screening;
import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * The interface Screening seat service.
 */
public interface ScreeningSeatService extends AbstractService<ScreeningSeat> {

    /**
     * Find all by screening list.
     *
     * @param screening the screening
     * @return the list
     */
    List<ScreeningSeat> findAllByScreening(Screening screening);

    /**
     * Find all by screening with id list.
     *
     * @param screeningId the screening id
     * @return the list
     */
    List<ScreeningSeat> findAllByScreeningWithId(Long screeningId);

    /**
     * Find all by showroom seat list.
     *
     * @param showroomSeat the showroom seat
     * @return the list
     */
    List<ScreeningSeat> findAllByShowroomSeat(ShowroomSeat showroomSeat);

    /**
     * Find all by showroom seat with id list.
     *
     * @param showroomSeatId the showroom seat id
     * @return the list
     */
    List<ScreeningSeat> findAllByShowroomSeatWithId(Long showroomSeatId);

    /**
     * Find by ticket optional.
     *
     * @param ticket the ticket
     * @return the optional
     */
    Optional<ScreeningSeat> findByTicket(Ticket ticket);

    /**
     * Find by ticket with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     */
    Optional<ScreeningSeat> findByTicketWithId(Long ticketId);

}
