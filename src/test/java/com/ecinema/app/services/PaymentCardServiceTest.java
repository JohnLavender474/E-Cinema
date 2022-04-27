package com.ecinema.app.services;

import com.ecinema.app.entities.Address;
import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceTest {

    private AddressService addressService;
    private PaymentCardService paymentCardService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private CouponService couponService;
    private CustomerRoleDefService customerRoleDefService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;

    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(addressRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        reviewService = new ReviewServiceImpl(reviewRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        couponService = new CouponServiceImpl(couponRepository);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService,
                ticketService, paymentCardService, couponService);
    }

    @Test
    void findAllByCustomerAuthority() {
        // given
        CustomerRoleDef customerRoleDef1 = new CustomerRoleDef();
        customerRoleDef1.setId(1L);
        CustomerRoleDef customerRoleDef2 = new CustomerRoleDef();
        customerRoleDef2.setId(2L);
        customerRoleDefService.save(customerRoleDef1);
        customerRoleDefService.save(customerRoleDef2);
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
        customerRoleDefService.save(customerRoleDef1);
        customerRoleDefService.save(customerRoleDef2);
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

    @Test
    void deletePaymentCascade() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Address address = new Address();
        address.setId(1L);
        given(addressRepository.findById(1L))
                .willReturn(Optional.of(address));
        addressService.save(address);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(2L);
        paymentCard.setBillingAddress(address);
        paymentCard.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getPaymentCards().add(paymentCard);
        given(paymentCardRepository.findById(2L))
                .willReturn(Optional.of(paymentCard));
        paymentCardService.save(paymentCard);
        assertTrue(customerRoleDef.getPaymentCards().contains(paymentCard));
        assertEquals(customerRoleDef, paymentCard.getCustomerRoleDef());
        assertEquals(address, paymentCard.getBillingAddress());
        // when
        paymentCardService.delete(paymentCard);
        // then
        assertFalse(customerRoleDef.getPaymentCards().contains(paymentCard));
        assertNotEquals(customerRoleDef, paymentCard.getCustomerRoleDef());
        assertNotEquals(address, paymentCard.getBillingAddress());
    }

}