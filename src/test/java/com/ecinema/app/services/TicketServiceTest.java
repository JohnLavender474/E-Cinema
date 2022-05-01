package com.ecinema.app.services;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private TicketService ticketService;
    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private CustomerRoleDefService customerRoleDefService;
    private ReviewService reviewService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    private AddressService addressService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(screeningRepository, screeningSeatService);
        reviewService = new ReviewServiceImpl(reviewRepository);
        addressService = new AddressServiceImpl(addressRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        couponService = new CouponServiceImpl(couponRepository);
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository, reviewService,
                                                                ticketService, paymentCardService, couponService);
    }

    @Test
    void deleteTicketCascade() {
        // given
        Screening screening = new Screening();
        screening.setId(0L);
        screeningService.save(screening);
        List<ScreeningSeat> screeningSeats = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setId((long) i + 1);
            screeningSeat.setScreening(screening);
            screening.getScreeningSeats().add(screeningSeat);
            screeningSeatService.save(screeningSeat);
            screeningSeats.add(screeningSeat);
        }
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setId(31L);
        customerRoleDefService.save(customerRoleDef);
        Ticket ticket = new Ticket();
        ticket.setId(32L);
        ticket.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getTickets().add(ticket);
        ticket.setScreeningSeat(screeningSeats.get(0));
        screeningSeats.get(0).setTicket(ticket);
        given(ticketRepository.findById(32L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        assertFalse(customerRoleDef.getTickets().isEmpty());
        assertNotNull(ticket.getCustomerRoleDef());
        assertEquals(screeningSeats.get(0), ticket.getScreeningSeat());
        assertEquals(ticket, screeningSeats.get(0).getTicket());
        for (ScreeningSeat screeningSeat : screeningSeats) {
            assertEquals(screening, screeningSeat.getScreening());
        }
        // when
        ticketService.delete(ticket);
        // then
        assertTrue(customerRoleDef.getTickets().isEmpty());
        assertNull(ticket.getCustomerRoleDef());
        assertNotEquals(screeningSeats.get(0), ticket.getScreeningSeat());
        for (ScreeningSeat screeningSeat : screeningSeats) {
            assertEquals(screening, screeningSeat.getScreening());
        }
    }

}