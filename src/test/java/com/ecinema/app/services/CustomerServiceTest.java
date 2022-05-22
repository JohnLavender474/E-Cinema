package com.ecinema.app.services;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.SeatBookingsForm;
import com.ecinema.app.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * The type Customer role def service test.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private TicketService ticketService;
    private ReviewService reviewService;
    private CustomerService customerService;
    private PaymentCardService paymentCardService;
    @Mock
    private EmailService emailService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;

    /*
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository);
        reviewService = new ReviewService(
                reviewRepository, null,
                customerRepository, null);
        paymentCardService = new PaymentCardService(
                paymentCardRepository, customerRepository, null);
        customerService = new CustomerService(
                customerRepository, screeningSeatRepository,
                emailService, reviewService, ticketService,
                paymentCardService, securityContext);
    }

    @Test
    void deleteCustomerRoleDefCascade() {
        // given
       Customer customer = new Customer();
       customer.setId(1L);
       customerService.save(customer);
       Review review = new Review();
       review.setId(2L);
       review.setWriter(customer);
       customer.getReviews().add(review);
       reviewService.save(review);
       Ticket ticket = new Ticket();
       ticket.setId(3L);
       ticket.setTicketOwner(customer);
       customer.getTickets().add(ticket);
       ticketService.save(ticket);
       PaymentCard paymentCard = new PaymentCard();
       paymentCard.setId(4L);
       paymentCard.setCardOwner(customer);
       customer.getPaymentCards().add(paymentCard);
       paymentCardService.save(paymentCard);
       assertEquals(customer, review.getWriter());
       assertTrue(customer.getReviews().contains(review));
       assertEquals(customer, ticket.getTicketOwner());
       assertTrue(customer.getTickets().contains(ticket));
       assertEquals(customer, paymentCard.getCardOwner());
       assertTrue(customer.getPaymentCards().contains(paymentCard));
       // when
        customerService.delete(customer);
        // then
        assertNotEquals(customer, review.getWriter());
        assertFalse(customer.getReviews().contains(review));
        assertNotEquals(customer, ticket.getTicketOwner());
        assertFalse(customer.getTickets().contains(ticket));
        assertNotEquals(customer, paymentCard.getCardOwner());
        assertFalse(customer.getPaymentCards().contains(paymentCard));
    }

    @Test
    void bookTickets() {
        // given
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        given(customerRepository.existsByUserWithId(1L)).willReturn(true);
        User user = new User();
        user.setEmail("test@gmail.com");
        Customer customer = new Customer();
        customer.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customer);
        given(customerRepository.findByUserWithId(1L)).willReturn(Optional.of(customer));
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setRowLetter(Letter.A);
        showroomSeat.setSeatNumber(1);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setShowroomSeat(showroomSeat);
        given(screeningSeatRepository.existsById(2L)).willReturn(true);
        given(screeningSeatRepository.screeningSeatIsBooked(2L)).willReturn(false);
        given(screeningSeatRepository.findById(2L)).willReturn(
                Optional.of(screeningSeat));
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setExpirationDate(LocalDate.now().plusYears(1));
        paymentCard.setId(3L);
        given(paymentCardRepository.findById(3L)).willReturn(
                Optional.of(paymentCard));
        given(paymentCardRepository.isPaymentCardOwnedByUser(
                3L, 1L)).willReturn(true);
        doNothing().when(emailService).sendFromBusinessEmail(
                anyString(), anyString(), anyString());
        given(ticketRepository.findUsernameOfTicketUserOwner(
                any())).willReturn(Optional.of("Test"));
        given(ticketRepository.findShowroomLetterAssociatedWithTicket(
                any())).willReturn(Optional.of(Letter.A));
        given(ticketRepository.findMovieTitleAssociatedWithTicket(
                any())).willReturn(Optional.of("Test"));
        given(ticketRepository.findShowtimeOfScreeningAssociatedWithTicket(
                any())).willReturn(Optional.of(
                        LocalDateTime.now().plusMonths(1)));
        given(ticketRepository.findEndtimeOfScreeningAssociatedWithTicket(
                any())).willReturn(Optional.of(
                        LocalDateTime.now().plusMonths(1).plusHours(2)));
        given(ticketRepository.findShowroomSeatAssociatedWithTicket(
                any())).willReturn(Optional.of(showroomSeat));
        // when
        SeatBookingsForm seatBookingsForm = new SeatBookingsForm();
        seatBookingsForm.setSeatIds(List.of(2L));
        seatBookingsForm.setPaymentCardId(3L);
        customerService.bookTickets(seatBookingsForm);
        // then
        verify(ticketRepository, times(1)).save(
                any(Ticket.class));
    }

}