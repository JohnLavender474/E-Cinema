package com.ecinema.app.services;

import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.repositories.ShowroomSeatRepository;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.services.implementations.ScreeningSeatServiceImpl;
import com.ecinema.app.services.implementations.ShowroomSeatServiceImpl;
import com.ecinema.app.services.implementations.ShowroomServiceImpl;
import com.ecinema.app.services.implementations.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScreeningSeatServiceTest {

    private TicketService ticketService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningSeatService screeningSeatService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ShowroomRepository showroomRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        screeningSeatService = new ScreeningSeatServiceImpl(
                screeningSeatRepository, ticketService);
        showroomSeatService = new ShowroomSeatServiceImpl(
                showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(
                showroomRepository, showroomSeatService,
                null, null);
    }

    @Test
    void deleteScreeningSeatCascade() {
        // given
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroomService.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setRowLetter(Letter.A);
        showroomSeat.setSeatNumber(1);
        showroomSeatService.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(1L);
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        given(screeningSeatRepository.findById(1L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeatService.save(screeningSeat);
        Ticket ticket = new Ticket();
        ticket.setId(2L);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        given(ticketRepository.findById(2L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        assertEquals(screeningSeat, ticket.getScreeningSeat());
        assertEquals(ticket, screeningSeat.getTicket());
        // when
        screeningSeatService.delete(screeningSeat);
        // then
        assertNotEquals(screeningSeat, ticket.getScreeningSeat());
        assertNull(ticket.getScreeningSeat());
        assertNotEquals(ticket, screeningSeat.getTicket());
        assertNull(screeningSeat.getTicket());
    }

}