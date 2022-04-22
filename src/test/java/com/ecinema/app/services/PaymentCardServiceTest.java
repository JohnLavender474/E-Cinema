package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.services.implementations.CustomerRoleDefServiceImpl;
import com.ecinema.app.services.implementations.PaymentCardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceTest {

    @InjectMocks
    PaymentCardServiceImpl paymentCardService;

    @Mock
    PaymentCardRepository paymentCardRepository;

    @InjectMocks
    CustomerRoleDefServiceImpl customerAuthorityService;

    @Mock
    CustomerRoleDefRepository customerRoleDefRepository;

    @Test
    void findAllByCustomerAuthority() {
        // given
        CustomerRoleDef customerRoleDef1 = new CustomerRoleDef();
        customerRoleDef1.setId(1L);
        CustomerRoleDef customerRoleDef2 = new CustomerRoleDef();
        customerRoleDef2.setId(2L);
        customerAuthorityService.save(customerRoleDef1);
        customerAuthorityService.save(customerRoleDef2);
        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PaymentCard paymentCard = new PaymentCard();
            if (i % 2 == 0) {
                paymentCard.setCustomerRoleDef(customerRoleDef1);
            } else {
                paymentCard.setCustomerRoleDef(customerRoleDef2);
            }
            paymentCards.add(paymentCard);
            paymentCardService.save(paymentCard);
        }
        List<PaymentCard> paymentCardsControl1 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCustomerRoleDef().equals(customerRoleDef1))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCustomerRoleDef(customerRoleDef1))
                .willReturn(paymentCardsControl1);
        List<PaymentCard> paymentCardsControl2 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCustomerRoleDef().equals(customerRoleDef2))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCustomerRoleDef(customerRoleDef2))
                .willReturn(paymentCardsControl2);
        // when
        List<PaymentCard> paymentCardsTest1 = paymentCardService.findAllByCustomerRoleDef(customerRoleDef1);
        List<PaymentCard> paymentCardsTest2 = paymentCardService.findAllByCustomerRoleDef(customerRoleDef2);
        // then
        assertEquals(paymentCardsControl1, paymentCardsTest1);
        assertEquals(paymentCardsControl2, paymentCardsTest2);
        verify(paymentCardRepository, times(1)).findDistinctByCustomerRoleDef(customerRoleDef1);
        verify(paymentCardRepository, times(1)).findDistinctByCustomerRoleDef(customerRoleDef2);
        verify(customerRoleDefRepository, times(1)).save(customerRoleDef1);
        verify(customerRoleDefRepository, times(1)).save(customerRoleDef1);
    }

    @Test
    void findAllByCustomerAuthorityWithId() {
        // given
        CustomerRoleDef customerRoleDef1 = new CustomerRoleDef();
        customerRoleDef1.setId(1L);
        CustomerRoleDef customerRoleDef2 = new CustomerRoleDef();
        customerRoleDef2.setId(2L);
        customerAuthorityService.save(customerRoleDef1);
        customerAuthorityService.save(customerRoleDef2);
        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PaymentCard paymentCard = new PaymentCard();
            if (i % 2 == 0) {
                paymentCard.setCustomerRoleDef(customerRoleDef1);
            } else {
                paymentCard.setCustomerRoleDef(customerRoleDef2);
            }
            paymentCards.add(paymentCard);
            paymentCardService.save(paymentCard);
        }
        List<PaymentCard> paymentCardsControl1 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCustomerRoleDef().getId()
                                                           .equals(customerRoleDef1.getId()))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCustomerRoleDefWithId(customerRoleDef1.getId()))
                .willReturn(paymentCardsControl1);
        List<PaymentCard> paymentCardsControl2 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCustomerRoleDef().getId()
                                                           .equals(customerRoleDef2.getId()))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCustomerRoleDefWithId(customerRoleDef2.getId()))
                .willReturn(paymentCardsControl2);
        // when
        List<PaymentCard> paymentCardsTest1 = paymentCardService.findAllByCustomerRoleDefId(customerRoleDef1.getId());
        List<PaymentCard> paymentCardsTest2 = paymentCardService.findAllByCustomerRoleDefId(customerRoleDef2.getId());
        // then
        assertEquals(paymentCardsControl1, paymentCardsTest1);
        assertEquals(paymentCardsControl2,  paymentCardsTest2);
        verify(paymentCardRepository, times(1)).findDistinctByCustomerRoleDefWithId(customerRoleDef1.getId());
        verify(paymentCardRepository, times(1)).findDistinctByCustomerRoleDefWithId(customerRoleDef2.getId());
        verify(customerRoleDefRepository, times(1)).save(customerRoleDef1);
        verify(customerRoleDefRepository, times(1)).save(customerRoleDef2);
    }

}