package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.repositories.ReviewRepository;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.services.implementations.CustomerRoleDefServiceImpl;
import com.ecinema.app.services.implementations.PaymentCardServiceImpl;
import com.ecinema.app.services.implementations.ReviewServiceImpl;
import com.ecinema.app.services.implementations.TicketServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 * The type Customer role def service test.
 */
@ExtendWith(MockitoExtension.class)
class CustomerRoleDefServiceTest {

    /**
     * The Customer role def service.
     */
    CustomerRoleDefService customerRoleDefService;
    /**
     * The Payment card service.
     */
    PaymentCardService paymentCardService;
    /**
     * The Ticket service.
     */
    TicketService ticketService;
    /**
     * The Review service.
     */
    ReviewService reviewService;
    /**
     * The Customer role def repository.
     */
    @Mock
    CustomerRoleDefRepository customerRoleDefRepository;
    /**
     * The Payment card repository.
     */
    @Mock
    PaymentCardRepository paymentCardRepository;
    /**
     * The Ticket repository.
     */
    @Mock
    TicketRepository ticketRepository;
    /**
     * The Review repository.
     */
    @Mock
    ReviewRepository reviewRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository);
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
    void testFindByPaymentCardsContains() {
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
    void testFindByTicketsContainsWithId() {
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
    void testFindByReviewsContains() {
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

}