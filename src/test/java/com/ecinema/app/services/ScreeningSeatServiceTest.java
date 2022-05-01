package com.ecinema.app.services;

import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.services.implementations.ScreeningSeatServiceImpl;
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
    private ScreeningSeatService screeningSeatService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
    }

    @Test
    void deleteScreeningSeatCascade() {
        // given
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(1L);
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