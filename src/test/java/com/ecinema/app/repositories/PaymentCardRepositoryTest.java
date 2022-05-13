package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.PaymentCard;
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
    private CustomerAuthorityRepository customerAuthorityRepository;

    @AfterEach
    void tearDown() {
        for (PaymentCard paymentCard : paymentCardRepository.findAll()) {
            paymentCard.getCardOwner().getPaymentCards().remove(paymentCard);
            paymentCard.setCardOwner(null);
        }
        paymentCardRepository.deleteAll();
    }

    @Test
    void findAllByCustomerRoleDef() {
        // given
        PaymentCard paymentCard = new PaymentCard();
        paymentCardRepository.save(paymentCard);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.getPaymentCards().add(paymentCard);
        paymentCard.setCardOwner(customerAuthority);
        customerAuthorityRepository.save(customerAuthority);
        // when
        List<PaymentCard> paymentCards = paymentCardRepository
                .findDistinctByCardOwner(customerAuthority);
        // then
        assertEquals(1, paymentCards.size());
        assertEquals(paymentCard, paymentCards.get(0));
    }

    @Test
    void findAllByCustomerRoleDefWithId() {
        // given
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthorityRepository.save(customerAuthority);
        PaymentCard paymentCard = new PaymentCard();
        customerAuthority.getPaymentCards().add(paymentCard);
        paymentCard.setCardOwner(customerAuthority);
        paymentCardRepository.save(paymentCard);
        // when
        List<PaymentCard> paymentCards = paymentCardRepository
                .findDistinctByCardOwnerWithId(customerAuthority.getId());
        // then
        assertEquals(1, paymentCards.size());
        assertEquals(paymentCard, paymentCards.get(0));
    }

}