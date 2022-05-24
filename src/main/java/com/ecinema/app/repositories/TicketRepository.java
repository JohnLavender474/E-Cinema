package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The jpa repository for {@link Ticket}.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Find all by screening.
     *
     * @param screening the screening
     * @return the list of tickets
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.screening = ?1")
    List<Ticket> findAllByScreening(Screening screening);

    /**
     * Find all by screening with id.
     *
     * @param screeningId the screening id
     * @return the list of tickets
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.screening.id = ?1")
    List<Ticket> findAllByScreeningWithId(Long screeningId);

    /**
     * Find by screening seat optional.
     *
     * @param screeningSeat the screening seat
     * @return the optional ticket
     */
    Optional<Ticket> findByScreeningSeat(ScreeningSeat screeningSeat);

    /**
     * Find by screening seat with id optional.
     *
     * @param screeningSeatId the screening seat id
     * @return the optional ticket
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.id = ?1")
    Optional<Ticket> findByScreeningSeatWithId(Long screeningSeatId);

    /**
     * Find all by showroom.
     *
     * @param showroom the showroom
     * @return the list of tickets
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.screening.showroom = ?1")
    List<Ticket> findAllByShowroom(Showroom showroom);

    /**
     * Find all by showroom with id.
     *
     * @param showroomId the showroom id
     * @return the list of tickets
     */
    @Query("SELECT t FROM Ticket t WHERE t.screeningSeat.screening.showroom.id = ?1")
    List<Ticket> findAllByShowroomWithId(Long showroomId);

    /**
     * Find all by creation date time less than or equal to provided {@link LocalDateTime}.
     *
     * @param localDateTime the local date time
     * @return the list of tickets
     */
    List<Ticket> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by creation date time greater than or equals to provided {@link LocalDateTime}.
     *
     * @param localDateTime the local date time
     * @return the list of tickets
     */
    List<Ticket> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by ticket status.
     *
     * @param ticketStatus the ticket status
     * @return the list of tickets
     */
    List<Ticket> findAllByTicketStatus(TicketStatus ticketStatus);

    /**
     * Find all by ticket type.
     *
     * @param ticketType the ticket type
     * @return the list of tickets
     */
    List<Ticket> findAllByTicketType(TicketType ticketType);

    /**
     * Find all by customer ticket owner.
     *
     * @param customer the customer
     * @return the list of tickets
     */
    List<Ticket> findAllByTicketOwner(Customer customer);

    /**
     * Find all by customer ticket owner with id.
     *
     * @param customerRoleDefId the customer id
     * @return the list of tickets
     */
    @Query("SELECT t FROM Ticket t WHERE t.ticketOwner.id = ?1")
    List<Ticket> findAllByTicketOwnerWithId(Long customerRoleDefId);

    @Query("SELECT t.ticketOwner.user.username FROM Ticket t WHERE t.id = ?1")
    Optional<String> findUsernameOfTicketUserOwner(Long ticketId);

    @Query("SELECT t.screeningSeat.screening.movie.title FROM Ticket t WHERE t.id = ?1")
    Optional<String> findMovieTitleAssociatedWithTicket(Long ticketId);

    @Query("SELECT t.screeningSeat.screening.showroom.showroomLetter FROM Ticket t WHERE t.id = ?1")
    Optional<Letter> findShowroomLetterAssociatedWithTicket(Long ticketId);

    @Query("SELECT t.screeningSeat.screening.showDateTime FROM Ticket t WHERE t.id = ?1")
    Optional<LocalDateTime> findShowtimeOfScreeningAssociatedWithTicket(Long ticketId);

    @Query("SELECT t.screeningSeat.screening.endDateTime FROM Ticket t WHERE t.id = ?1")
    Optional<LocalDateTime> findEndtimeOfScreeningAssociatedWithTicket(Long ticketId);

    @Query("SELECT t.screeningSeat.showroomSeat FROM Ticket t WHERE t.id = ?1")
    Optional<ShowroomSeat> findShowroomSeatAssociatedWithTicket(Long ticketId);

}
