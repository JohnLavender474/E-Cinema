package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;

import java.util.Optional;

public interface CustomerRoleDefService extends UserRoleDefService<CustomerRoleDef> {

    /**
     * Find by payment cards contains optional.
     *
     * @param paymentCard the payment card
     * @return the optional
     */
    Optional<CustomerRoleDef> findByPaymentCardsContains(PaymentCard paymentCard);

    /**
     * Find by payment cards contains with id optional.
     *
     * @param paymentCardId the payment card id
     * @return the optional
     */
    Optional<CustomerRoleDef> findByPaymentCardsContainsWithId(Long paymentCardId);

    /**
     * Find by tickets contains optional.
     *
     * @param ticket the ticket
     * @return the optional
     */
    Optional<CustomerRoleDef> findByTicketsContains(Ticket ticket);

    /**
     * Find by tickets contains with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     */
    Optional<CustomerRoleDef> findByTicketsContainsWithId(Long ticketId);

    /**
     * Find by reviews contains optional.
     *
     * @param review the review
     * @return the optional
     */
    Optional<CustomerRoleDef> findByReviewsContains(Review review);

    /**
     * Find by reviews contains with id optional.
     *
     * @param reviewId the review id
     * @return the optional
     */
    Optional<CustomerRoleDef> findByReviewsContainsWithId(Long reviewId);

}
