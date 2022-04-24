package com.ecinema.app.repositories;

import com.ecinema.app.entities.*;
import com.ecinema.app.utils.constants.TicketStatus;
import com.ecinema.app.utils.constants.TicketType;
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
public interface TicketRepository extends JpaRepository<Ticket, Long> {

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
     * @param customerRoleDef the customer role def
     * @return the list
     */
    List<Ticket> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef);

    /**
     * Find all by customer role def with id list.
     *
     * @param customerRoleDefId the customer role def id
     * @return the list
     */
    @Query("SELECT t FROM Ticket t WHERE t.customerRoleDef.id = ?1")
    List<Ticket> findAllByCustomerRoleDefWithId(Long customerRoleDefId);

    /**
     * Find all by payment card list.
     *
     * @param paymentCard the payment card
     * @return the list
     */
    List<Ticket> findAllByPaymentCard(PaymentCard paymentCard);

    /**
     * Find all by payment card with id list.
     *
     * @param paymentCardId the payment card id
     * @return the list
     */
    @Query("SELECT t FROM Ticket t WHERE t.paymentCard.id = ?1")
    List<Ticket> findAllByPaymentCardWithId(Long paymentCardId);

    /**
     * Find all by screening list.
     *
     * @param screening the screening
     * @return the list
     */
    List<Ticket> findAllByScreening(Screening screening);

    /**
     * Find all by screening with id list.
     *
     * @param screeningId the screening id
     * @return the list
     */
    @Query("SELECT t FROM Ticket t WHERE t.screening.id = ?1")
    List<Ticket> findAllByScreeningWithId(Long screeningId);

}
