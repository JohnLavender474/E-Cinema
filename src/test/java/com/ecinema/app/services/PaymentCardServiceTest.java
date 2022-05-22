package com.ecinema.app.services;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.dtos.PaymentCardDto;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.enums.UsState;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.domain.validators.AddressValidator;
import com.ecinema.app.domain.validators.PaymentCardValidator;
import com.ecinema.app.repositories.*;
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

    private ReviewService reviewService;
    private TicketService ticketService;
    private CustomerService customerService;
    private AddressValidator addressValidator;
    private PaymentCardService paymentCardService;
    private PaymentCardValidator paymentCardValidator;
    private SecurityContext securityContext;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        securityContext = new SecurityContext();
        addressValidator = new AddressValidator();
        paymentCardValidator = new PaymentCardValidator(addressValidator);
        paymentCardService = new PaymentCardService(
                paymentCardRepository, customerRepository,
                paymentCardValidator);
        reviewService = new ReviewService(
                reviewRepository, null,
                customerRepository, null);
        ticketService = new TicketService(ticketRepository);
        customerService = new CustomerService(
                customerRepository, screeningSeatRepository,
                null, reviewService, ticketService,
                paymentCardService, securityContext);
    }

    @Test
    void findAllByCustomerAuthority() {
        // given
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customerService.save(customer1);
        customerService.save(customer2);
        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PaymentCard paymentCard = new PaymentCard();
            if (i % 2 == 0) {
                paymentCard.setCardOwner(customer1);
            } else {
                paymentCard.setCardOwner(customer2);
            }
            paymentCards.add(paymentCard);
            paymentCardService.save(paymentCard);
        }
        List<PaymentCard> paymentCardsControl1 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().equals(customer1))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCardUserWithId(customer1.getId()))
                .willReturn(paymentCardsControl1);
        List<PaymentCard> paymentCardsControl2 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().equals(customer2))
                .collect(Collectors.toList());
        given(paymentCardRepository.findDistinctByCardUserWithId(customer2.getId()))
                .willReturn(paymentCardsControl2);
        // when
        List<PaymentCardDto> paymentCardsTest1 = paymentCardService
                .findAllByCardUserWithId(customer1.getId());
        List<PaymentCardDto> paymentCardsTest2 = paymentCardService
                .findAllByCardUserWithId(customer2.getId());
        // then
        assertEquals(paymentCardService.convertToDto(paymentCardsControl1), paymentCardsTest1);
        assertEquals(paymentCardService.convertToDto(paymentCardsControl2), paymentCardsTest2);
        verify(paymentCardRepository, times(1))
                .findDistinctByCardUserWithId(customer1.getId());
        verify(paymentCardRepository, times(1))
                .findDistinctByCardUserWithId(customer2.getId());
        verify(customerRepository, times(1)).save(customer1);
        verify(customerRepository, times(1)).save(customer1);
    }

    @Test
    void findAllByCustomerAuthorityWithId() {
        // given
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customerService.save(customer1);
        customerService.save(customer2);
        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PaymentCard paymentCard = new PaymentCard();
            if (i % 2 == 0) {
                paymentCard.setCardOwner(customer1);
            } else {
                paymentCard.setCardOwner(customer2);
            }
            paymentCards.add(paymentCard);
            paymentCardService.save(paymentCard);
        }
        List<PaymentCard> paymentCardsControl1 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().getId()
                                                           .equals(customer1.getId()))
                .collect(Collectors.toList());
        given(paymentCardRepository
                      .findDistinctByCardUserWithId(customer1.getId()))
                .willReturn(paymentCardsControl1);
        List<PaymentCard> paymentCardsControl2 = paymentCards
                .stream().filter(paymentCard -> paymentCard.getCardOwner().getId()
                                                           .equals(customer2.getId()))
                .collect(Collectors.toList());
        given(paymentCardRepository
                      .findDistinctByCardUserWithId(customer2.getId()))
                .willReturn(paymentCardsControl2);
        // when
        List<PaymentCardDto> paymentCardsTest1 = paymentCardService
                .findAllByCardUserWithId(customer1.getId());
        List<PaymentCardDto> paymentCardsTest2 = paymentCardService
                .findAllByCardUserWithId(customer2.getId());
        // then
        assertEquals(paymentCardService.convertToDto(paymentCardsControl1), paymentCardsTest1);
        assertEquals(paymentCardService.convertToDto(paymentCardsControl2),  paymentCardsTest2);
        verify(paymentCardRepository, times(1))
                .findDistinctByCardUserWithId(customer1.getId());
        verify(paymentCardRepository, times(1))
                .findDistinctByCardUserWithId(customer2.getId());
        verify(customerRepository, times(1)).save(customer1);
        verify(customerRepository, times(1)).save(customer2);
    }

    @Test
    void deletePaymentCascade() {
        // given
        Customer customer = new Customer();
        customerService.save(customer);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(2L);
        paymentCard.setCardOwner(customer);
        customer.getPaymentCards().add(paymentCard);
        paymentCardService.save(paymentCard);
        assertTrue(customer.getPaymentCards().contains(paymentCard));
        assertEquals(customer, paymentCard.getCardOwner());
        // when
        paymentCardService.delete(paymentCard);
        // then
        assertFalse(customer.getPaymentCards().contains(paymentCard));
        assertNotEquals(customer, paymentCard.getCardOwner());
    }

    @Test
    void submitPaymentCardForm() {
        // given
        Customer customer = new Customer();
        customer.setId(1L);
        given(customerRepository.findById(1L))
                .willReturn(Optional.of(customer));
        customerService.save(customer);
        // when
        LocalDate expirationDate = LocalDate.now().plusYears(1);
        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.setCustomerRoleDefId(1L);
        paymentCardForm.setExpirationDate(expirationDate);
        paymentCardService.submitPaymentCardForm(paymentCardForm);
        // then
        assertEquals(1, customer.getPaymentCards().size());
        PaymentCard paymentCard = customer.getPaymentCards().stream().findFirst().orElse(null);
        assertNotNull(paymentCard);
        assertEquals(customer, paymentCard.getCardOwner());
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