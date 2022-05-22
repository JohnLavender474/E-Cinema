package com.ecinema.app.services;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.*;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private TicketService ticketService;
    private ScreeningService screeningService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningSeatService screeningSeatService;
    private CustomerService customerService;
    private ReviewService reviewService;
    private PaymentCardService paymentCardService;
    private SecurityContext securityContext;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        securityContext = new SecurityContext();
        ticketService = new TicketService(ticketRepository);
        screeningSeatService = new ScreeningSeatService(
                screeningSeatRepository, ticketService);
        showroomSeatService = new ShowroomSeatService(
                showroomSeatRepository, screeningSeatService);
        screeningService = new ScreeningService(
                screeningRepository, movieRepository,
                showroomRepository,  screeningSeatService, null);
        reviewService = new ReviewService(
                reviewRepository, movieRepository,
                null, null);
        paymentCardService = new PaymentCardService(
                paymentCardRepository, null, null);
        customerService = new CustomerService(
                customerRepository, screeningSeatRepository,
                null, reviewService, ticketService,
                paymentCardService, securityContext);
        showroomService = new ShowroomService(
                showroomRepository, showroomSeatService,
                screeningService, null);
    }

    @Test
    void deleteTicketCascade() {
        // given
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroomService.save(showroom);
        for (int i = 0; i < 30; i++) {
            ShowroomSeat showroomSeat = new ShowroomSeat();
            showroomSeat.setId((long) i);
            showroomSeat.setRowLetter(Letter.A);
            showroomSeat.setSeatNumber(i);
            given(showroomSeatRepository.findById((long) i))
                    .willReturn(Optional.of(showroomSeat));
            showroomSeatService.save(showroomSeat);
        }
        Screening screening = new Screening();
        screening.setId(0L);
        screeningService.save(screening);
        List<ScreeningSeat> screeningSeats = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            final Integer j = i;
            ShowroomSeat showroomSeat = showroomSeatRepository.findById((long) i).orElseThrow(
                    () -> new NoEntityFoundException("showroom seat", "id", j));
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setId((long) i);
            screeningSeat.setShowroomSeat(showroomSeat);
            showroomSeat.getScreeningSeats().add(screeningSeat);
            screeningSeat.setScreening(screening);
            screening.getScreeningSeats().add(screeningSeat);
            screeningSeatService.save(screeningSeat);
            screeningSeats.add(screeningSeat);
        }
        Customer customer = new Customer();
        customer.setId(31L);
        customerService.save(customer);
        Ticket ticket = new Ticket();
        ticket.setId(32L);
        ticket.setTicketOwner(customer);
        customer.getTickets().add(ticket);
        ticket.setScreeningSeat(screeningSeats.get(0));
        screeningSeats.get(0).setTicket(ticket);
        ticketService.save(ticket);
        assertFalse(customer.getTickets().isEmpty());
        assertNotNull(ticket.getTicketOwner());
        assertEquals(screeningSeats.get(0), ticket.getScreeningSeat());
        assertEquals(ticket, screeningSeats.get(0).getTicket());
        for (ScreeningSeat screeningSeat : screeningSeats) {
            assertEquals(screening, screeningSeat.getScreening());
        }
        // when
        ticketService.delete(ticket);
        // then
        assertTrue(customer.getTickets().isEmpty());
        assertNull(ticket.getTicketOwner());
        assertNotEquals(screeningSeats.get(0), ticket.getScreeningSeat());
        for (ScreeningSeat screeningSeat : screeningSeats) {
            assertEquals(screening, screeningSeat.getScreening());
        }
    }

}