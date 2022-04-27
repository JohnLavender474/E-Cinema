package com.ecinema.app.repositories;

import com.ecinema.app.entities.Screening;
import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Screening seat repository.
 */
@Repository
public interface ScreeningSeatRepository extends JpaRepository<ScreeningSeat, Long> {

    /**
     * Find all by screening list.
     *
     * @param screening the screening
     * @return the list
     */
    @Query("SELECT s FROM ScreeningSeat s WHERE s.screening = ?1")
    List<ScreeningSeat> findAllByScreening(Screening screening);

    /**
     * Find all by screening with id list.
     *
     * @param screeningId the screening id
     * @return the list
     */
    @Query("SELECT s FROM ScreeningSeat s WHERE s.screening.id = ?1")
    List<ScreeningSeat> findAllByScreeningWithId(Long screeningId);

    /**
     * Find all by showroom seat list.
     *
     * @param showroomSeat the showroom seat
     * @return the list
     */
    @Query("SELECT s FROM ScreeningSeat s WHERE s.showroomSeat = ?1")
    List<ScreeningSeat> findAllByShowroomSeat(ShowroomSeat showroomSeat);

    /**
     * Find all by showroom seat with id list.
     *
     * @param showroomSeatId the showroom seat id
     * @return the list
     */
    @Query("SELECT s FROM ScreeningSeat s WHERE s.showroomSeat.id = ?1")
    List<ScreeningSeat> findAllByShowroomSeatWithId(Long showroomSeatId);

    /**
     * Find by ticket optional.
     *
     * @param ticket the ticket
     * @return the optional
     */
    @Query("SELECT s FROM ScreeningSeat s WHERE s.ticket = ?1")
    Optional<ScreeningSeat> findByTicket(Ticket ticket);

    /**
     * Find by ticket with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     */
    @Query("SELECT s FROM ScreeningSeat s WHERE s.ticket.id = ?1")
    Optional<ScreeningSeat> findByTicketWithId(Long ticketId);

}