package com.ecinema.app.services;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.Letter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TheaterServiceTest {

    private AddressService addressService;
    private TicketService ticketService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private TheaterService theaterService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TheaterRepository theaterRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(addressRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(screeningRepository, screeningSeatService);
        showroomSeatService = new ShowroomSeatServiceImpl(showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(showroomRepository, showroomSeatService, screeningService);
        theaterService = new TheaterServiceImpl(theaterRepository, addressService,
                                                showroomService, screeningService);
    }

    @Test
    void deleteTheaterCascade() {
        // given
        Address address = new Address();
        address.setId(0L);
        given(addressRepository.findById(0L))
                .willReturn(Optional.of(address));
        addressService.save(address);
        Theater theater = new Theater();
        theater.setId(1L);
        theater.setAddress(address);
        given(theaterRepository.findById(1L))
                .willReturn(Optional.of(theater));
        theaterService.save(theater);
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroom.setId(2L);
        showroom.setTheater(theater);
        theater.getShowrooms().put(showroom.getShowroomLetter(), showroom);
        given(showroomRepository.findById(2L))
                .willReturn(Optional.of(showroom));
        showroomService.save(showroom);
        Screening screening = new Screening();
        screening.setId(3L);
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        given(screeningRepository.findById(3L))
                .willReturn(Optional.of(screening));
        screeningService.save(screening);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setId(4L);
        showroomSeat.setShowroom(showroom);
        showroom.getShowroomSeats().add(showroomSeat);
        given(showroomSeatRepository.findById(4L))
                .willReturn(Optional.of(showroomSeat));
        showroomSeatService.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(5L);
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeat.setScreening(screening);
        screening.getScreeningSeats().add(screeningSeat);
        given(screeningSeatRepository.findById(5L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeatService.save(screeningSeat);
        Ticket ticket = new Ticket();
        ticket.setId(6L);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        given(ticketRepository.findById(6L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        assertEquals(address, theater.getAddress());
        assertEquals(ticket, screeningSeat.getTicket());
        assertEquals(screeningSeat, ticket.getScreeningSeat());
        assertEquals(screening, screeningSeat.getScreening());
        assertTrue(screening.getScreeningSeats().contains(screeningSeat));
        assertEquals(showroomSeat, screeningSeat.getShowroomSeat());
        assertTrue(showroomSeat.getScreeningSeats().contains(screeningSeat));
        assertEquals(showroom, showroomSeat.getShowroom());
        assertTrue(showroom.getShowroomSeats().contains(showroomSeat));
        assertEquals(theater, showroom.getTheater());
        assertEquals(showroom, theater.getShowrooms().get(Letter.A));
        // when
        theaterService.delete(theater);
        // then
        assertNotEquals(address, theater.getAddress());
        assertNotEquals(ticket, screeningSeat.getTicket());
        assertNotEquals(screeningSeat, ticket.getScreeningSeat());
        assertNotEquals(screening, screeningSeat.getScreening());
        assertFalse(screening.getScreeningSeats().contains(screeningSeat));
        assertNotEquals(showroomSeat, screeningSeat.getShowroomSeat());
        assertFalse(showroomSeat.getScreeningSeats().contains(screeningSeat));
        assertNotEquals(showroom, showroomSeat.getShowroom());
        assertFalse(showroom.getShowroomSeats().contains(showroomSeat));
        assertNotEquals(theater, showroom.getTheater());
        assertNotEquals(showroom, theater.getShowrooms().get(Letter.A));
    }

}