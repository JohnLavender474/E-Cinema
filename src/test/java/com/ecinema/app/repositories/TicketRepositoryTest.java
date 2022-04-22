package com.ecinema.app.repositories;

import com.ecinema.app.entities.Ticket;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.TicketStatus;
import jdk.jshell.execution.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
    }

    @Test
    void findAllByCreationDateTimeLessThanEqual() {
        // given
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Ticket ticket = new Ticket();
            ticket.setCreationDateTime(UtilMethods.randomDateTime());
            ticketRepository.save(ticket);
            tickets.add(ticket);
        }
        LocalDateTime controlVar = UtilMethods.randomDateTime();
        List<Ticket> control = tickets.stream()
                .filter(ticket -> ticket.getCreationDateTime().isEqual(controlVar) ||
                        ticket.getCreationDateTime().isBefore(controlVar))
                .collect(Collectors.toList());
        // when
        List<Ticket> test = ticketRepository.findAllByCreationDateTimeLessThanEqual(controlVar);
        // then
        assertEquals(control, test, "both have size: " + control.size());
    }

    @Test
    void findAllByCreationDateTimeGreaterThanEqual() {
        // given
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Ticket ticket = new Ticket();
            ticket.setCreationDateTime(UtilMethods.randomDateTime());
            ticketRepository.save(ticket);
            tickets.add(ticket);
        }
        LocalDateTime controlVar = UtilMethods.randomDateTime();
        List<Ticket> control = tickets.stream()
                .filter(ticket -> ticket.getCreationDateTime().isEqual(controlVar) ||
                        ticket.getCreationDateTime().isAfter(controlVar))
                .collect(Collectors.toList());
        // when
        List<Ticket> test = ticketRepository.findAllByCreationDateTimeGreaterThanEqual(controlVar);
        // then
        assertEquals(control, test, "both have size: " + control.size());
    }

    @Test
    void findAllByTicketStatus() {
        // given
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Ticket ticket = new Ticket();
            ticket.setTicketStatus(TicketStatus.values()[i % 4]);
            ticketRepository.save(ticket);
            tickets.add(ticket);
        }
        List<Ticket> control = tickets.stream()
                .filter(ticket ->  ticket.getTicketStatus().equals(TicketStatus.VALID))
                .collect(Collectors.toList());
        // when
        List<Ticket> test = ticketRepository.findAllByTicketStatus(TicketStatus.VALID);
        // then
        assertEquals(control, test, "both have size: " + control.size());
    }

}