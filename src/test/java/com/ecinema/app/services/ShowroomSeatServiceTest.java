package com.ecinema.app.services;

import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Ticket;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.repositories.ShowroomSeatRepository;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.services.implementations.ScreeningSeatServiceImpl;
import com.ecinema.app.services.implementations.ShowroomSeatServiceImpl;
import com.ecinema.app.services.implementations.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShowroomSeatServiceTest {

    private TicketService ticketService;
    private ScreeningSeatService screeningSeatService;
    private ShowroomSeatService showroomSeatService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
        showroomSeatService = new ShowroomSeatServiceImpl(showroomSeatRepository, screeningSeatService);
    }

    @Test
    void deleteShowroomSeatCascade() {
        // given
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setId(1L);
        given(showroomSeatRepository.findById(1L))
                .willReturn(Optional.of(showroomSeat));
        showroomSeatService.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(2L);
        given(screeningSeatRepository.findById(2L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeatService.save(screeningSeat);
        Ticket ticket = new Ticket();
        ticket.setId(3L);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        given(ticketRepository.findById(3L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        assertEquals(ticket, screeningSeat.getTicket());
        assertEquals(showroomSeat, screeningSeat.getShowroomSeat());
        // when
        showroomSeatService.delete(showroomSeat);
        // then
        assertNotEquals(ticket, screeningSeat.getTicket());
        assertNotEquals(showroomSeat, screeningSeat.getShowroomSeat());
        verify(showroomSeatRepository, times(1)).delete(showroomSeat);
        verify(screeningSeatRepository, times(1)).delete(screeningSeat);
        verify(ticketRepository, times(1)).delete(ticket);
    }

}