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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomerRoleDefServiceTest {

    CustomerRoleDefService customerRoleDefService;
    PaymentCardService paymentCardService;
    TicketService ticketService;
    ReviewService reviewService;
    @Mock
    CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    PaymentCardRepository paymentCardRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    void testFindByPaymentCardsContains() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getPaymentCards().add(paymentCard);
        paymentCardService.save(paymentCard);
        given(customerRoleDefRepository.findAllByPaymentCardsContains(paymentCard))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> customerRoleDefOptional =
                customerRoleDefService.findByPaymentCardsContains(paymentCard);
        // then
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDef, customerRoleDefOptional.get());
    }

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
        given(customerRoleDefRepository.findAllByTicketsContainsWithId(ticket.getId()))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> customerRoleDefOptional =
                customerRoleDefService.findByTicketsContainsWithId(1L);
        // then
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDef, customerRoleDefOptional.get());
    }

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
        given(customerRoleDefRepository.findAllByReviewsContainsWithId(review.getId()))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> customerRoleDefOptional =
                customerRoleDefService.findByReviewsContainsWithId(1L);
        // then
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDef, customerRoleDefOptional.get());
    }

}