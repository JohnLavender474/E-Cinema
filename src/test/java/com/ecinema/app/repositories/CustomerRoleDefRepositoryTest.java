package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
class CustomerRoleDefRepositoryTest {

    @Autowired
    private CustomerRoleDefRepository customerRoleDefRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @AfterEach
    void tearDown() {
        customerRoleDefRepository.deleteAll();
    }

    @Test
    void testFindByReviewsContains() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefRepository.save(customerRoleDef);
        Review review = new Review();
        review.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        reviewRepository.save(review);
        // when
        Optional<CustomerRoleDef> customerAuthorityOptional = customerRoleDefRepository
                .findByReviewsContains(review);
        // then
        assertTrue(customerAuthorityOptional.isPresent() &&
                customerAuthorityOptional.get().equals(customerRoleDef) &&
                customerAuthorityOptional.get().getReviews().contains(review));
    }

    @Test
    void testFindByTicketsContains() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefRepository.save(customerRoleDef);
        Ticket ticket = new Ticket();
        ticket.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getTickets().add(ticket);
        ticketRepository.save(ticket);
        // when
        Optional<CustomerRoleDef> customerAuthorityOptional = customerRoleDefRepository
                .findByTicketsContains(ticket);
        // then
        assertTrue(customerAuthorityOptional.isPresent() &&
                           customerAuthorityOptional.get().equals(customerRoleDef) &&
                           customerAuthorityOptional.get().getTickets().contains(ticket));
    }

    @Test
    void testFindByPaymentCardsContains() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefRepository.save(customerRoleDef);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getPaymentCards().add(paymentCard);
        paymentCardRepository.save(paymentCard);
        // when
        Optional<CustomerRoleDef> customerAuthorityOptional = customerRoleDefRepository
                .findByPaymentCardsContains(paymentCard);
        // then
        assertTrue(customerAuthorityOptional.isPresent() &&
                customerAuthorityOptional.get().equals(customerRoleDef) &&
                customerAuthorityOptional.get().getPaymentCards().contains(paymentCard));
    }

}