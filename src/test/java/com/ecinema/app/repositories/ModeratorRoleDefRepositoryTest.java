package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.User;
import com.ecinema.app.utils.constants.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ModeratorRoleDefRepositoryTest {

    @Autowired
    private ModeratorRoleDefRepository moderatorRoleDefRepository;

    @Autowired
    private CustomerRoleDefRepository customerRoleDefRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        moderatorRoleDefRepository.deleteAll();
    }

    @Test
    void testFindAllByContainsCensoredReviews() {
        // given
        for (int i = 0; i < 10; i++) {
            User user1 = new User();
            userRepository.save(user1);
            ModeratorRoleDef moderatorRoleDef = new ModeratorRoleDef();
            moderatorRoleDef.setUser(user1);
            moderatorRoleDefRepository.save(moderatorRoleDef);
            if (i % 2 == 0) {
                User user2 = new User();
                userRepository.save(user2);
                CustomerRoleDef customerRoleDef = new CustomerRoleDef();
                customerRoleDef.setUser(user2);
                user2.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
                customerRoleDefRepository.save(customerRoleDef);
                Review review = new Review();
                review.setWriter(customerRoleDef);
                customerRoleDef.getReviews().add(review);
                review.setCensor(moderatorRoleDef);
                moderatorRoleDef.getCensoredReviews().add(review);
                reviewRepository.save(review);
            }
        }
        // when
        List<ModeratorRoleDef> moderatorRoleDefs = moderatorRoleDefRepository
                .findAllByContainsCensoredReviews();
        // then
        assertEquals(5, moderatorRoleDefs.size());
    }

    @Test
    void testFindByCensoredReviewsContainsAndCensoredReviewsContainsWithId() {
        // given
        User customer = new User();
        userRepository.save(customer);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setUser(customer);
        customer.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
        customerRoleDefRepository.save(customerRoleDef);
        Review review = new Review();
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        reviewRepository.save(review);
        User moderator = new User();
        userRepository.save(moderator);
        ModeratorRoleDef moderatorRoleDef = new ModeratorRoleDef();
        moderatorRoleDef.setUser(moderator);
        moderator.getUserRoleDefs().put(UserRole.MODERATOR, moderatorRoleDef);
        moderatorRoleDefRepository.save(moderatorRoleDef);
        moderatorRoleDef.getCensoredReviews().add(review);
        review.setCensor(moderatorRoleDef);
        reviewRepository.save(review);
        // when
        Optional<ModeratorRoleDef> censor1 = moderatorRoleDefRepository.
                findByCensoredReviewsContains(review);
        Optional<ModeratorRoleDef> censor2 = moderatorRoleDefRepository
                .findByCensoredReviewsContainsWithId(review.getId());
        // then
        assertTrue(censor1.isPresent() && censor2.isPresent() &&
                           censor1.get().equals(moderatorRoleDef) &&
                           censor2.get().equals(moderatorRoleDef));
    }

    @Test
    void testFindByCensoredUsersContainsAndContainsWithId() {
        // given
        User customer = new User();
        userRepository.save(customer);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setUser(customer);
        customer.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
        customerRoleDefRepository.save(customerRoleDef);
        User moderator = new User();
        userRepository.save(moderator);
        ModeratorRoleDef moderatorRoleDef = new ModeratorRoleDef();
        moderatorRoleDef.setUser(moderator);
        moderator.getUserRoleDefs().put(UserRole.MODERATOR, moderatorRoleDef);
        moderatorRoleDef.getCensoredCustomers().add(customerRoleDef);
        customerRoleDef.setCensoredBy(moderatorRoleDef);
        moderatorRoleDefRepository.save(moderatorRoleDef);
        // when
        Optional<ModeratorRoleDef> censor1 = moderatorRoleDefRepository
                .findByCensoredUsersContains(customerRoleDef);
        Optional<ModeratorRoleDef> censor2 = moderatorRoleDefRepository
                .findByCensoredUsersContainsWithId(customerRoleDef.getId());
        // then
        assertTrue(censor1.isPresent() && censor2.isPresent() &&
                           censor1.get().equals(moderatorRoleDef) &&
                           censor2.get().equals(moderatorRoleDef));
    }

}