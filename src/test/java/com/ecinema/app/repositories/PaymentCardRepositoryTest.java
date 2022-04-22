package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentCardRepositoryTest {

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @Autowired
    private CustomerRoleDefRepository customerRoleDefRepository;

    @AfterEach
    void tearDown() {
        for (PaymentCard paymentCard : paymentCardRepository.findAll()) {
            paymentCard.getCustomerRoleDef().getPaymentCards().remove(paymentCard);
            paymentCard.setCustomerRoleDef(null);
        }
        paymentCardRepository.deleteAll();
    }

    @Test
    void findAllByCustomerRoleDef() {
        // given
        PaymentCard paymentCard = new PaymentCard();
        paymentCardRepository.save(paymentCard);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.getPaymentCards().add(paymentCard);
        paymentCard.setCustomerRoleDef(customerRoleDef);
        customerRoleDefRepository.save(customerRoleDef);
        // when
        List<PaymentCard> paymentCards = paymentCardRepository
                .findDistinctByCustomerRoleDef(customerRoleDef);
        // then
        assertEquals(1, paymentCards.size());
        assertEquals(paymentCard, paymentCards.get(0));
    }

    @Test
    void findAllByCustomerRoleDefWithId() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefRepository.save(customerRoleDef);
        PaymentCard paymentCard = new PaymentCard();
        customerRoleDef.getPaymentCards().add(paymentCard);
        paymentCard.setCustomerRoleDef(customerRoleDef);
        paymentCardRepository.save(paymentCard);
        // when
        List<PaymentCard> paymentCards = paymentCardRepository
                .findDistinctByCustomerRoleDefWithId(customerRoleDef.getId());
        // then
        assertEquals(1, paymentCards.size());
        assertEquals(paymentCard, paymentCards.get(0));
    }

}