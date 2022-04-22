package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Customer role def repository.
 */
@Repository
public interface CustomerRoleDefRepository extends UserRoleDefRepository<CustomerRoleDef> {

    /**
     * Find all by payment cards contains optional.
     *
     * @param paymentCard the payment card
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.paymentCards p WHERE p = ?1")
    Optional<CustomerRoleDef> findAllByPaymentCardsContains(PaymentCard paymentCard);

    /**
     * Find all by payment cards contains with id optional.
     *
     * @param paymentCardId the payment card id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.paymentCards p WHERE p.id = ?1")
    Optional<CustomerRoleDef> findAllByPaymentCardsContainsWithId(Long paymentCardId);

    /**
     * Find all by tickets contains optional.
     *
     * @param ticket the ticket
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.tickets t WHERE t = ?1")
    Optional<CustomerRoleDef> findAllByTicketsContains(Ticket ticket);

    /**
     * Find all by tickets contains with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.tickets t WHERE t.id = ?1")
    Optional<CustomerRoleDef> findAllByTicketsContainsWithId(Long ticketId);

    /**
     * Find all by reviews contains optional.
     *
     * @param review the review
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.reviews r WHERE r = ?1")
    Optional<CustomerRoleDef> findAllByReviewsContains(Review review);

    /**
     * Find all by reviews contains with id optional.
     *
     * @param reviewId the review id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.reviews r WHERE r.id = ?1")
    Optional<CustomerRoleDef> findAllByReviewsContainsWithId(Long reviewId);

}
