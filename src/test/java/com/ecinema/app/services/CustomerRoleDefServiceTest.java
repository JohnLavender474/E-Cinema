package com.ecinema.app.services;

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

    private AddressService addressService;
    private CustomerRoleDefService customerRoleDefService;
    private PaymentCardService paymentCardService;
    private TicketService ticketService;
    private ReviewService reviewService;
    private CouponService couponService;
    @Mock
    private AddressRepository addressRepository;
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
        addressService = new AddressServiceImpl(addressRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        couponService = new CouponServiceImpl(couponRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository, reviewService,
                                                                ticketService, paymentCardService,
                                                                couponService);
        ticketService = new TicketServiceImpl(ticketRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        customerRoleDefService.deleteAll();
        paymentCardService.deleteAll();
        ticketService.deleteAll();
        reviewService.deleteAll();
    }

    /**
     * Test find by payment cards contains.
     */
    @Test
    void findByPaymentCardsContains() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getPaymentCards().add(paymentCard);
        paymentCardService.save(paymentCard);
        given(customerRoleDefRepository.findByPaymentCardsContains(paymentCard))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> customerRoleDefOptional =
                customerRoleDefService.findByPaymentCardsContains(paymentCard);
        // then
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDef, customerRoleDefOptional.get());
    }

    /**
     * Test find by tickets contains with id.
     */
    @Test
    void findByTicketsContainsWithId() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getTickets().add(ticket);
        ticketService.save(ticket);
        given(customerRoleDefRepository.findByTicketsContainsWithId(ticket.getId()))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> customerRoleDefOptional =
                customerRoleDefService.findByTicketsContainsWithId(1L);
        // then
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDef, customerRoleDefOptional.get());
    }

    /**
     * Test find by reviews contains.
     */
    @Test
    void findByReviewsContains() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Review review = new Review();
        review.setId(1L);
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        reviewService.save(review);
        given(customerRoleDefRepository.findByReviewsContainsWithId(review.getId()))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> customerRoleDefOptional =
                customerRoleDefService.findByReviewsContainsWithId(1L);
        // then
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDef, customerRoleDefOptional.get());
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