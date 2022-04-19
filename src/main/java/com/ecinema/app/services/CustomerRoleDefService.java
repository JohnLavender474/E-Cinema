package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;

import java.util.Optional;

public interface CustomerRoleDefService extends UserRoleDefService<CustomerRoleDef> {
    Optional<CustomerRoleDef> findByPaymentCardsContains(PaymentCard paymentCard);
    Optional<CustomerRoleDef> findByTicketsContains(Ticket ticket);
    Optional<CustomerRoleDef> findByReviewsContains(Review review);
}
