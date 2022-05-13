package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.UserAuthority;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
class CustomerAuthorityRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerAuthorityRepository customerAuthorityRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @AfterEach
    void tearDown() {
        customerAuthorityRepository.deleteAll();
    }

    @Test
    void findByReviewsContains() {
        // given
        User user = new User();
        userRepository.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityRepository.save(customerAuthority);
        Review review = new Review();
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
        reviewRepository.save(review);
        // when
        Optional<CustomerAuthority> customerAuthorityOptional = customerAuthorityRepository
                .findByReviewsContains(review);
        // then
        assertTrue(customerAuthorityOptional.isPresent() &&
                customerAuthorityOptional.get().equals(customerAuthority) &&
                customerAuthorityOptional.get().getReviews().contains(review));
    }

    @Test
    void findByTicketsContains() {
        // given
        User user = new User();
        userRepository.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityRepository.save(customerAuthority);
        Ticket ticket = new Ticket();
        ticket.setTicketOwner(customerAuthority);
        customerAuthority.getTickets().add(ticket);
        ticketRepository.save(ticket);
        // when
        Optional<CustomerAuthority> customerAuthorityOptional = customerAuthorityRepository
                .findByTicketsContains(ticket);
        // then
        assertTrue(customerAuthorityOptional.isPresent() &&
                           customerAuthorityOptional.get().equals(customerAuthority) &&
                           customerAuthorityOptional.get().getTickets().contains(ticket));
    }

    @Test
    void findByPaymentCardsContains() {
        // given
        User user = new User();
        userRepository.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityRepository.save(customerAuthority);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardOwner(customerAuthority);
        customerAuthority.getPaymentCards().add(paymentCard);
        paymentCardRepository.save(paymentCard);
        // when
        Optional<CustomerAuthority> customerAuthorityOptional = customerAuthorityRepository
                .findByPaymentCardsContains(paymentCard);
        // then
        assertTrue(customerAuthorityOptional.isPresent() &&
                customerAuthorityOptional.get().equals(customerAuthority) &&
                customerAuthorityOptional.get().getPaymentCards().contains(paymentCard));
    }

    @Test
    void findByCouponsContains() {
        // given
        User user = new User();
        userRepository.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityRepository.save(customerAuthority);
        Coupon coupon = new Coupon();
        coupon.setCouponOwner(customerAuthority);
        customerAuthority.getCoupons().add(coupon);
        couponRepository.save(coupon);
        // when
        Optional<CustomerAuthority> customerRoleDefOptional1 = customerAuthorityRepository
                .findByCouponsContains(coupon);
        Optional<CustomerAuthority> customerRoleDefOptional2 = customerAuthorityRepository
                .findByCouponsContainsWithId(coupon.getId());
        // then
        assertTrue(customerRoleDefOptional1.isPresent());
        assertTrue(customerRoleDefOptional2.isPresent());
        assertEquals(customerAuthority, customerRoleDefOptional1.get());
        assertEquals(customerAuthority, customerRoleDefOptional2.get());
    }

    @Test
    void findIdByUserWithId() {
        // given
        User user = new User();
        userRepository.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityRepository.save(customerAuthority);
        // when
        Optional<Long> customerRoleDefIdOptional = customerAuthorityRepository.findIdByUserWithId(user.getId());
        Optional<CustomerAuthority> customerRoleDefOptional = customerAuthorityRepository.findByUser(user);
        // then
        assertTrue(customerRoleDefIdOptional.isPresent());
        assertTrue(customerRoleDefOptional.isPresent());
        assertEquals(customerRoleDefOptional.get().getId(), customerRoleDefIdOptional.get());
    }

}