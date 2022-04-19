package com.ecinema.app.services;

import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.services.implementations.CustomerRoleDefServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerRoleDefServiceTest {

    @InjectMocks
    CustomerRoleDefServiceImpl customerAuthorityService;

    @Mock
    CustomerRoleDefRepository customerRoleDefRepository;

    @Test
    void testFindByPaymentCardsContains() {
        // given

    }

    @Test
    void testFindByTicketsContains() {
    }

    @Test
    void testFindByReviewsContains() {
    }

}