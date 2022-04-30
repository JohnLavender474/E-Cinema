package com.ecinema.app.services;

import com.ecinema.app.entities.Ticket;
import com.ecinema.app.utils.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Ticket service.
 */
public interface TicketService extends AbstractService<Ticket> {

    /**
     * Find all by creation date time less than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<Ticket> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by creation date time greater than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<Ticket> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by ticket status list.
     *
     * @param ticketStatus the ticket status
     * @return the list
     */
    List<Ticket> findAllByTicketStatus(TicketStatus ticketStatus);

}
