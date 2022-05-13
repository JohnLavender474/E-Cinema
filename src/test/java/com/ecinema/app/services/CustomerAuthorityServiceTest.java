package com.ecinema.app.services;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
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
class CustomerAuthorityServiceTest {

    private CustomerAuthorityService customerAuthorityService;
    private PaymentCardService paymentCardService;
    private TicketService ticketService;
    private ReviewService reviewService;
    private CouponService couponService;
    @Mock
    private CustomerAuthorityRepository customerAuthorityRepository;
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
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        reviewService = new ReviewServiceImpl(
                reviewRepository, null,
                customerAuthorityRepository, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService,
                ticketService, paymentCardService, couponService);
    }

    @Test
    void deleteCustomerRoleDefCascade() {
        // given
       CustomerAuthority customerAuthority = new CustomerAuthority();
       customerAuthority.setId(1L);
       given(customerAuthorityRepository.findById(1L))
               .willReturn(Optional.of(customerAuthority));
       customerAuthorityService.save(customerAuthority);
       Review review = new Review();
       review.setId(2L);
       review.setWriter(customerAuthority);
       customerAuthority.getReviews().add(review);
       given(reviewRepository.findById(2L))
               .willReturn(Optional.of(review));
       reviewService.save(review);
       Ticket ticket = new Ticket();
       ticket.setId(3L);
       ticket.setTicketOwner(customerAuthority);
       customerAuthority.getTickets().add(ticket);
       given(ticketRepository.findById(3L))
               .willReturn(Optional.of(ticket));
       ticketService.save(ticket);
       PaymentCard paymentCard = new PaymentCard();
       paymentCard.setId(4L);
       paymentCard.setCardOwner(customerAuthority);
       customerAuthority.getPaymentCards().add(paymentCard);
       given(paymentCardRepository.findById(4L))
               .willReturn(Optional.of(paymentCard));
       paymentCardService.save(paymentCard);
       Coupon coupon = new Coupon();
       coupon.setId(5L);
       coupon.setCouponOwner(customerAuthority);
       customerAuthority.getCoupons().add(coupon);
       given(couponRepository.findById(5L))
               .willReturn(Optional.of(coupon));
       couponService.save(coupon);
       assertEquals(customerAuthority, review.getWriter());
       assertTrue(customerAuthority.getReviews().contains(review));
       assertEquals(customerAuthority, ticket.getTicketOwner());
       assertTrue(customerAuthority.getTickets().contains(ticket));
       assertEquals(customerAuthority, paymentCard.getCardOwner());
       assertTrue(customerAuthority.getPaymentCards().contains(paymentCard));
       assertEquals(customerAuthority, coupon.getCouponOwner());
       assertTrue(customerAuthority.getCoupons().contains(coupon));
       // when
        customerAuthorityService.delete(customerAuthority);
        // then
        assertNotEquals(customerAuthority, review.getWriter());
        assertFalse(customerAuthority.getReviews().contains(review));
        assertNotEquals(customerAuthority, ticket.getTicketOwner());
        assertFalse(customerAuthority.getTickets().contains(ticket));
        assertNotEquals(customerAuthority, paymentCard.getCardOwner());
        assertFalse(customerAuthority.getPaymentCards().contains(paymentCard));
        assertNotEquals(customerAuthority, coupon.getCouponOwner());
        assertFalse(customerAuthority.getCoupons().contains(coupon));
    }

}