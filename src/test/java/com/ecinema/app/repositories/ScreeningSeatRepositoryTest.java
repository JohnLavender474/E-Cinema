package com.ecinema.app.repositories;

import com.ecinema.app.entities.Screening;
import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScreeningSeatRepositoryTest {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    private ShowroomSeatRepository showroomSeatRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    void tearDown() {
        screeningRepository.deleteAll();
        screeningSeatRepository.deleteAll();
        showroomSeatRepository.deleteAll();
    }

    @Test
    void findAllByScreening() {
        // given
        Screening screening = new Screening();
        screeningRepository.save(screening);
        List<ScreeningSeat> screeningSeats = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setScreening(screening);
            screening.getScreeningSeats().add(screeningSeat);
            screeningSeatRepository.save(screeningSeat);
            screeningSeats.add(screeningSeat);
        }
        // when
        List<ScreeningSeat> test1 = screeningSeatRepository
                .findAllByScreening(screening);
        List<ScreeningSeat> test2 = screeningSeatRepository
                .findAllByScreeningWithId(screening.getId());
        // then
        assertEquals(screeningSeats, test1);
        assertEquals(screeningSeats, test2);
    }

    @Test
    void findAllByShowroomSeat() {
        // given
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeatRepository.save(showroomSeat);
        List<ScreeningSeat> screeningSeats = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setShowroomSeat(showroomSeat);
            showroomSeat.getScreeningSeats().add(screeningSeat);
            screeningSeatRepository.save(screeningSeat);
            screeningSeats.add(screeningSeat);
        }
        // when
        List<ScreeningSeat> test1 = screeningSeatRepository
                .findAllByShowroomSeat(showroomSeat);
        List<ScreeningSeat> test2 = screeningSeatRepository
                .findAllByShowroomSeatWithId(showroomSeat.getId());
        // then
        assertEquals(screeningSeats, test1);
        assertEquals(screeningSeats, test2);
    }

    @Test
    void findByTicket() {
        // given
        Ticket ticket = new Ticket();
        ticketRepository.save(ticket);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setTicket(ticket);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeatRepository.save(screeningSeat);
        // when
        Optional<ScreeningSeat> screeningSeatOptional1 = screeningSeatRepository
                .findByTicket(ticket);
        Optional<ScreeningSeat> screeningSeatOptional2 = screeningSeatRepository
                .findByTicketWithId(ticket.getId());
        // then
        assertTrue(screeningSeatOptional1.isPresent());
        assertTrue(screeningSeatOptional2.isPresent());
        assertEquals(screeningSeat, screeningSeatOptional1.get());
        assertEquals(screeningSeat, screeningSeatOptional2.get());
    }

}