package com.ecinema.app.repositories;

import com.ecinema.app.entities.Ticket;
import com.ecinema.app.utils.constants.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Ticket repository.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

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
