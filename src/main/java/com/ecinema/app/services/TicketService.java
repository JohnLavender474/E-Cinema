package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.TicketDto;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.forms.TicketForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Ticket service.
 */
public interface TicketService extends AbstractService<Ticket>, EntityDtoConverter<Ticket, TicketDto> {

    /**
     * Submit ticket form.
     *
     * @param ticketForm the ticket form
     * @throws NoEntityFoundException the no entity found exception
     * @throws ClashException         the clash exception
     */
    void submitTicketForm(TicketForm ticketForm)
        throws NoEntityFoundException, ClashException, InvalidAssociationException;

    /**
     * Find all by creation date time less than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<TicketDto> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by creation date time greater than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<TicketDto> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by ticket status list.
     *
     * @param ticketStatus the ticket status
     * @return the list
     */
    List<TicketDto> findAllByTicketStatus(TicketStatus ticketStatus);

}
