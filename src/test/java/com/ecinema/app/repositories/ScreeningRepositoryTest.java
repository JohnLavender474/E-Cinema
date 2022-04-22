package com.ecinema.app.repositories;

import com.ecinema.app.entities.*;
import com.ecinema.app.utils.UtilMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScreeningRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
        theaterRepository.deleteAll();
        screeningRepository.deleteAll();
        showroomRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    void findAllByShowDateTimeLessThanEqual() {
        // given
        List<Screening> screenings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Screening screening = new Screening();
            screening.setShowDateTime(UtilMethods.randomDateTime());
            screeningRepository.save(screening);
            screenings.add(screening);
        }
        LocalDateTime controlVar = UtilMethods.randomDateTime();
        List<Screening> control = screenings
                .stream().filter(screening -> screening.getShowDateTime().isEqual(controlVar) ||
                                screening.getShowDateTime().isBefore(controlVar))
                .sorted(Comparator.comparing(Screening::getShowDateTime))
                .collect(Collectors.toList());
        // when
        List<Screening> test = screeningRepository.findAllByShowDateTimeLessThanEqual(controlVar);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByShowDateTimeGreaterThanEqual() {
        // given
        List<Screening> screenings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Screening screening = new Screening();
            screening.setShowDateTime(UtilMethods.randomDateTime());
            screeningRepository.save(screening);
            screenings.add(screening);
        }
        LocalDateTime controlVar = UtilMethods.randomDateTime();
        List<Screening> control = screenings.stream()
                .filter(screening -> screening.getShowDateTime().isEqual(controlVar) ||
                        screening.getShowDateTime().isAfter(controlVar))
                .sorted(Comparator.comparing(Screening::getShowDateTime))
                .collect(Collectors.toList());
        // when
        List<Screening> test = screeningRepository.findAllByShowDateTimeGreaterThanEqual(controlVar);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByShowDateTimeBetween() {
        // given
        List<Screening> screenings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Screening screening = new Screening();
            screening.setShowDateTime(UtilMethods.randomDateTime());
            screeningRepository.save(screening);
            screenings.add(screening);
        }
        LocalDateTime controlVar1 = UtilMethods.randomDateTime();
        LocalDateTime controlVar2 = UtilMethods.randomDateTime();
        List<LocalDateTime> controlVars = new ArrayList<>() {{
            add(controlVar1);
            add(controlVar2);
        }};
        controlVars.sort(LocalDateTime::compareTo);
        List<Screening> control = screenings.stream()
                .filter(screening -> (screening.getShowDateTime().isAfter(controlVars.get(0)) ||
                        screening.getShowDateTime().equals(controlVars.get(0))) &&
                        (screening.getShowDateTime().isBefore(controlVars.get(1)) ||
                        screening.getShowDateTime().equals(controlVars.get(1))))
                .sorted(Comparator.comparing(Screening::getShowDateTime))
                .collect(Collectors.toList());
        // when
        List<Screening> test = screeningRepository.findAllByShowDateTimeBetween(controlVars.get(0), controlVars.get(1));
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByMovie() {
        // given
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        List<Screening> screenings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Screening screening = new Screening();
            Movie movie = i % 2 == 0 ? movie1 : movie2;
            screening.setMovie(movie);
            movie.getScreenings().add(screening);
            screeningRepository.save(screening);
            screenings.add(screening);
        }
        List<Screening> control = screenings.stream()
                .filter(screening -> screening.getMovie().equals(movie1))
                .collect(Collectors.toList());
        // when
        List<Screening> test1 = screeningRepository.findAllByMovie(movie1);
        List<Screening> test2 = screeningRepository.findAllByMovieWithId(movie1.getId());
        // then
        assertEquals(control, test1);
        assertEquals(control, test2);
    }

    @Test
    void findAllByTheater() {
        // given
        Theater theater = new Theater();
        theaterRepository.save(theater);
        Screening screening = new Screening();
        screening.setTheater(theater);
        theater.getScreenings().add(screening);
        screeningRepository.save(screening);
        // when
        List<Screening> test1 = screeningRepository.findAllByTheater(theater);
        List<Screening> test2 = screeningRepository
                .findAllByTheaterWithId(theater.getId());
        // then
        assertEquals(1, test1.size());
        assertEquals(screening, test1.get(0));
        assertEquals(theater, test1.get(0).getTheater());
        assertEquals(1, test2.size());
        assertEquals(screening, test2.get(0));
        assertEquals(theater, test2.get(0).getTheater());
    }

    @Test
    void findAllByShowroom() {
        // given
        Showroom showroom = new Showroom();
        showroomRepository.save(showroom);
        Screening screening = new Screening();
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screeningRepository.save(screening);
        // when
        List<Screening> test1 = screeningRepository
                .findAllByShowroom(showroom);
        List<Screening> test2 = screeningRepository
                .findAllByShowroomWithId(showroom.getId());
        // then
        assertEquals(1, test1.size());
        assertEquals(screening, test1.get(0));
        assertEquals(showroom, test1.get(0).getShowroom());
        assertEquals(1, test2.size());
        assertEquals(screening, test2.get(0));
        assertEquals(showroom, test2.get(0).getShowroom());
    }

    @Test
    void findByTicketsContains() {
        // given
        Screening screening = new Screening();
        screeningRepository.save(screening);
        Ticket ticket = new Ticket();
        ticket.setScreening(screening);
        screening.getTickets().add(ticket);
        ticketRepository.save(ticket);
        // when
        Optional<Screening> test1 = screeningRepository
                .findByTicketsContains(ticket);
        Optional<Screening> test2 = screeningRepository
                .findByTicketsContainsWithId(ticket.getId());
        // then
        assertTrue(test1.isPresent());
        assertTrue(test2.isPresent());
        assertEquals(screening, test1.get());
        assertEquals(test1.get(), test2.get());
    }

}