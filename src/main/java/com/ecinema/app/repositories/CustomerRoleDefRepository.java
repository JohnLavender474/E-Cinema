package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRoleDefRepository extends UserRoleDefRepository<CustomerRoleDef> {

    @Query("SELECT c FROM CustomerRoleDef c JOIN c.paymentCards p WHERE p = ?1")
    Optional<CustomerRoleDef> findByPaymentCardsContains(PaymentCard paymentCard);

    @Query("SELECT c FROM CustomerRoleDef c JOIN c.tickets t WHERE t = ?1")
    Optional<CustomerRoleDef> findByTicketsContains(Ticket ticket);

    @Query("SELECT c FROM CustomerRoleDef c JOIN c.reviews r WHERE r = ?1")
    Optional<CustomerRoleDef> findByReviewsContains(Review review);

}
