package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Ticket repository.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, AbstractRepository {

    /**
     * Find all by screening list.
     *
     * @param screening the screening
     * @return the list
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.screening = ?1")
    List<Ticket> findAllByScreening(Screening screening);

    /**
     * Find all by screening with id list.
     *
     * @param screeningId the screening id
     * @return the list
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.screening.id = ?1")
    List<Ticket> findAllByScreeningWithId(Long screeningId);

    /**
     * Find by screening seat optional.
     *
     * @param screeningSeat the screening seat
     * @return the optional
     */
    Optional<Ticket> findByScreeningSeat(ScreeningSeat screeningSeat);

    /**
     * Find by screening seat with id optional.
     *
     * @param screeningSeatId the screening seat id
     * @return the optional
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.id = ?1")
    Optional<Ticket> findByScreeningSeatWithId(Long screeningSeatId);

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

    /**
     * Find all by ticket type list.
     *
     * @param ticketType the ticket type
     * @return the list
     */
    List<Ticket> findAllByTicketType(TicketType ticketType);

    /**
     * Find all by customer role def list.
     *
     * @param customerAuthority the customer role def
     * @return the list
     */
    List<Ticket> findAllByTicketOwner(CustomerAuthority customerAuthority);

    /**
     * Find all by customer role def with id list.
     *
     * @param customerRoleDefId the customer role def id
     * @return the list
     */
    @Query("SELECT t FROM Ticket t WHERE t.ticketOwner.id = ?1")
    List<Ticket> findAllByTicketOwnerWithId(Long customerRoleDefId);

}
