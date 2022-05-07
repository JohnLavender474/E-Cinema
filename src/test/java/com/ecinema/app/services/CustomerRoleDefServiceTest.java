package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.CustomerRoleDefDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * The type Customer role def service test.
 */
@ExtendWith(MockitoExtension.class)
class CustomerRoleDefServiceTest {

    private CustomerRoleDefService customerRoleDefService;
    private PaymentCardService paymentCardService;
    private TicketService ticketService;
    private ReviewService reviewService;
    private CouponService couponService;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CouponRepository couponRepository;

    /*
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository);
        couponService = new CouponServiceImpl(couponRepository);
        reviewService = new ReviewServiceImpl(
                reviewRepository, null,
                customerRoleDefRepository, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService,
                ticketService, paymentCardService, couponService);
        ticketService = new TicketServiceImpl(ticketRepository);
    }

    @Test
    void deleteCustomerRoleDefCascade() {
        // given
       CustomerRoleDef customerRoleDef = new CustomerRoleDef();
       customerRoleDef.setId(1L);
       given(customerRoleDefRepository.findById(1L))
               .willReturn(Optional.of(customerRoleDef));
       customerRoleDefService.save(customerRoleDef);
       Review review = new Review();
       review.setId(2L);
       review.setWriter(customerRoleDef);
       customerRoleDef.getReviews().add(review);
       given(reviewRepository.findById(2L))
               .willReturn(Optional.of(review));
       reviewService.save(review);
       Ticket ticket = new Ticket();
       ticket.setId(3L);
       ticket.setCustomerRoleDef(customerRoleDef);
       customerRoleDef.getTickets().add(ticket);
       given(ticketRepository.findById(3L))
               .willReturn(Optional.of(ticket));
       ticketService.save(ticket);
       PaymentCard paymentCard = new PaymentCard();
       paymentCard.setId(4L);
       paymentCard.setCustomerRoleDef(customerRoleDef);
       customerRoleDef.getPaymentCards().add(paymentCard);
       given(paymentCardRepository.findById(4L))
               .willReturn(Optional.of(paymentCard));
       paymentCardService.save(paymentCard);
       Coupon coupon = new Coupon();
       coupon.setId(5L);
       coupon.setCustomerRoleDef(customerRoleDef);
       customerRoleDef.getCoupons().add(coupon);
       given(couponRepository.findById(5L))
               .willReturn(Optional.of(coupon));
       couponService.save(coupon);
       assertEquals(customerRoleDef, review.getWriter());
       assertTrue(customerRoleDef.getReviews().contains(review));
       assertEquals(customerRoleDef, ticket.getCustomerRoleDef());
       assertTrue(customerRoleDef.getTickets().contains(ticket));
       assertEquals(customerRoleDef, paymentCard.getCustomerRoleDef());
       assertTrue(customerRoleDef.getPaymentCards().contains(paymentCard));
       assertEquals(customerRoleDef, coupon.getCustomerRoleDef());
       assertTrue(customerRoleDef.getCoupons().contains(coupon));
       // when
        customerRoleDefService.delete(customerRoleDef);
        // then
        assertNotEquals(customerRoleDef, review.getWriter());
        assertFalse(customerRoleDef.getReviews().contains(review));
        assertNotEquals(customerRoleDef, ticket.getCustomerRoleDef());
        assertFalse(customerRoleDef.getTickets().contains(ticket));
        assertNotEquals(customerRoleDef, paymentCard.getCustomerRoleDef());
        assertFalse(customerRoleDef.getPaymentCards().contains(paymentCard));
        assertNotEquals(customerRoleDef, coupon.getCustomerRoleDef());
        assertFalse(customerRoleDef.getCoupons().contains(coupon));
    }

}