package com.ecinema.app.services;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.enums.UsState;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.domain.validators.AddressValidator;
import com.ecinema.app.domain.validators.PaymentCardValidator;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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

    private PaymentCardService paymentCardService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private CouponService couponService;
    private CustomerAuthorityService customerAuthorityService;
    private AddressValidator addressValidator;
    private PaymentCardValidator paymentCardValidator;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CustomerAuthorityRepository customerAuthorityRepository;

    @BeforeEach
    void setUp() {
        addressValidator = new AddressValidator();
        paymentCardValidator = new PaymentCardValidator(addressValidator);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, customerAuthorityRepository,
                paymentCardValidator);
        reviewService = new ReviewServiceImpl(
                reviewRepository, null,
                customerAuthorityRepository, null);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService,
                ticketService, paymentCardService, couponService);
    }

    @Test
    void findAllByCustomerAuthority() {
        // given
        CustomerAuthority customerAuthority1 = new CustomerAuthority();
        customerAuthority1.setId(1L);
        CustomerAuthority customerAuthority2 = new CustomerAuthority();
        customerAuthority2.setId(2L);
        customerAuthorityService.save(customerAuthority1);
        customerAuthorityService.save(customerAuthority2);
        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PaymentCard paymentCard = new PaymentCard();
            if (i % 2 == 0) {
                paymentCard.setCardOwner(customerAuthority1);
            } else {
                paymentCard.setCardOwner(customerAuthority2);
            }
            paymentCards.add(paymentCard);
            paymentCardService.save(paymentCard);
        }
        List<PaymentCard> paymentCardsControl1 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().equals(customerAuthority1))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCardOwner(customerAuthority1))
                .willReturn(paymentCardsControl1);
        List<PaymentCard> paymentCardsControl2 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().equals(customerAuthority2))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCardOwner(customerAuthority2))
                .willReturn(paymentCardsControl2);
        // when
        List<PaymentCard> paymentCardsTest1 = paymentCardService
                .findAllByCardOwner(customerAuthority1);
        List<PaymentCard> paymentCardsTest2 = paymentCardService
                .findAllByCardOwner(customerAuthority2);
        // then
        assertEquals(paymentCardsControl1, paymentCardsTest1);
        assertEquals(paymentCardsControl2, paymentCardsTest2);
        verify(paymentCardRepository, times(1))
                .findDistinctByCardOwner(customerAuthority1);
        verify(paymentCardRepository, times(1))
                .findDistinctByCardOwner(customerAuthority2);
        verify(customerAuthorityRepository, times(1)).save(customerAuthority1);
        verify(customerAuthorityRepository, times(1)).save(customerAuthority1);
    }

    @Test
    void findAllByCustomerAuthorityWithId() {
        // given
        CustomerAuthority customerAuthority1 = new CustomerAuthority();
        customerAuthority1.setId(1L);
        CustomerAuthority customerAuthority2 = new CustomerAuthority();
        customerAuthority2.setId(2L);
        customerAuthorityService.save(customerAuthority1);
        customerAuthorityService.save(customerAuthority2);
        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PaymentCard paymentCard = new PaymentCard();
            if (i % 2 == 0) {
                paymentCard.setCardOwner(customerAuthority1);
            } else {
                paymentCard.setCardOwner(customerAuthority2);
            }
            paymentCards.add(paymentCard);
            paymentCardService.save(paymentCard);
        }
        List<PaymentCard> paymentCardsControl1 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().getId()
                                                           .equals(customerAuthority1.getId()))
                .collect(Collectors.toList());
        given(paymentCardRepository
                      .findDistinctByCardOwnerWithId(customerAuthority1.getId()))
                .willReturn(paymentCardsControl1);
        List<PaymentCard> paymentCardsControl2 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().getId()
                                                           .equals(customerAuthority2.getId()))
                .collect(Collectors.toList());
        given(paymentCardRepository
                      .findDistinctByCardOwnerWithId(customerAuthority2.getId()))
                .willReturn(paymentCardsControl2);
        // when
        List<PaymentCard> paymentCardsTest1 = paymentCardService
                .findAllByCardOwnerWithId(customerAuthority1.getId());
        List<PaymentCard> paymentCardsTest2 = paymentCardService
                .findAllByCardOwnerWithId(customerAuthority2.getId());
        // then
        assertEquals(paymentCardsControl1, paymentCardsTest1);
        assertEquals(paymentCardsControl2,  paymentCardsTest2);
        verify(paymentCardRepository, times(1))
                .findDistinctByCardOwnerWithId(customerAuthority1.getId());
        verify(paymentCardRepository, times(1))
                .findDistinctByCardOwnerWithId(customerAuthority2.getId());
        verify(customerAuthorityRepository, times(1)).save(customerAuthority1);
        verify(customerAuthorityRepository, times(1)).save(customerAuthority2);
    }

    @Test
    void deletePaymentCascade() {
        // given
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthorityService.save(customerAuthority);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(2L);
        paymentCard.setCardOwner(customerAuthority);
        customerAuthority.getPaymentCards().add(paymentCard);
        given(paymentCardRepository.findById(2L))
                .willReturn(Optional.of(paymentCard));
        paymentCardService.save(paymentCard);
        assertTrue(customerAuthority.getPaymentCards().contains(paymentCard));
        assertEquals(customerAuthority, paymentCard.getCardOwner());
        // when
        paymentCardService.delete(paymentCard);
        // then
        assertFalse(customerAuthority.getPaymentCards().contains(paymentCard));
        assertNotEquals(customerAuthority, paymentCard.getCardOwner());
    }

    @Test
    void submitPaymentCardForm() {
        // given
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setId(1L);
        given(customerAuthorityRepository.findById(1L))
                .willReturn(Optional.of(customerAuthority));
        customerAuthorityService.save(customerAuthority);
        // when
        LocalDate expirationDate = LocalDate.now().plusYears(1);
        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.setCustomerRoleDefId(1L);
        paymentCardForm.setExpirationDate(expirationDate);
        paymentCardService.submitPaymentCardForm(paymentCardForm);
        // then
        assertEquals(1, customerAuthority.getPaymentCards().size());
        PaymentCard paymentCard = customerAuthority.getPaymentCards().stream().findFirst().orElse(null);
        assertNotNull(paymentCard);
        assertEquals(customerAuthority, paymentCard.getCardOwner());
        assertEquals("John", paymentCard.getFirstName());
        assertEquals("Doe", paymentCard.getLastName());
        assertEquals("1234123412341234", paymentCard.getCardNumber());
        assertNotNull(paymentCard.getExpirationDate());
        assertEquals(expirationDate.getMonth(), paymentCard.getExpirationDate().getMonth());
        assertEquals(1, paymentCard.getExpirationDate().getDayOfMonth());
        assertEquals(expirationDate.getYear(), paymentCard.getExpirationDate().getYear());
        assertNotNull(paymentCard.getBillingAddress());
        assertEquals("Street", paymentCard.getBillingAddress().getStreet());
        assertEquals("City", paymentCard.getBillingAddress().getCity());
        assertEquals(UsState.GEORGIA, paymentCard.getBillingAddress().getUsState());
        assertEquals("12345", paymentCard.getBillingAddress().getZipcode());
    }

}