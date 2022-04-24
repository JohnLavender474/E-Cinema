package com.ecinema.app.repositories;

import com.ecinema.app.entities.*;
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
    Optional<CustomerRoleDef> findByPaymentCardsContains(PaymentCard paymentCard);

    /**
     * Find all by payment cards contains with id optional.
     *
     * @param paymentCardId the payment card id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.paymentCards p WHERE p.id = ?1")
    Optional<CustomerRoleDef> findByPaymentCardsContainsWithId(Long paymentCardId);

    /**
     * Find all by tickets contains optional.
     *
     * @param ticket the ticket
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.tickets t WHERE t = ?1")
    Optional<CustomerRoleDef> findByTicketsContains(Ticket ticket);

    /**
     * Find all by tickets contains with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.tickets t WHERE t.id = ?1")
    Optional<CustomerRoleDef> findByTicketsContainsWithId(Long ticketId);

    /**
     * Find all by reviews contains optional.
     *
     * @param review the review
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.reviews r WHERE r = ?1")
    Optional<CustomerRoleDef> findByReviewsContains(Review review);

    /**
     * Find all by reviews contains with id optional.
     *
     * @param reviewId the review id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.reviews r WHERE r.id = ?1")
    Optional<CustomerRoleDef> findByReviewsContainsWithId(Long reviewId);

    /**
     * Find by coupons contains optional.
     *
     * @param coupon the coupon
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.coupons co WHERE co = ?1")
    Optional<CustomerRoleDef> findByCouponsContains(Coupon coupon);

    /**
     * Find by coupons contains with id optional.
     *
     * @param couponId the coupon id
     * @return the optional
     */
    @Query("SELECT c FROM CustomerRoleDef c JOIN c.coupons co WHERE co.id = ?1")
    Optional<CustomerRoleDef> findByCouponsContainsWithId(Long couponId);

}
