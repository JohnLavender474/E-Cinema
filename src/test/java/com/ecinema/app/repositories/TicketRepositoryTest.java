package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.objects.Duration;
import com.ecinema.app.util.UtilMethods;
import com.ecinema.app.domain.enums.TicketStatus;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ShowroomSeatRepository showroomSeatRepository;

    @Autowired
    private ScreeningSeatRepository screeningSeatRepository;

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

    @Test
    void queriesForBuildingDto() {
        // given
        User user = new User();
        user.setUsername("TestUser123");
        userRepository.save(user);
        Customer customer = new Customer();
        customer.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customer);
        customerRepository.save(customer);
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.C);
        showroomRepository.save(showroom);
        Movie movie = new Movie();
        movie.setTitle("Test Title");
        movie.setDuration(Duration.of(1, 30));
        movieRepository.save(movie);
        Screening screening = new Screening();
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        LocalDateTime localDateTime = LocalDateTime.now();
        screening.setShowDateTime(localDateTime);
        screening.setEndDateTime(
                localDateTime.plusHours(1).plusMinutes(30));
        screeningRepository.save(screening);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setRowLetter(Letter.D);
        showroomSeat.setSeatNumber(9);
        showroomSeat.setShowroom(showroom);
        showroom.getShowroomSeats().add(showroomSeat);
        showroomSeatRepository.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeat.setScreening(screening);
        screening.getScreeningSeats().add(screeningSeat);
        screeningSeatRepository.save(screeningSeat);
        Ticket ticket = new Ticket();
        ticket.setTicketOwner(customer);
        customer.getTickets().add(ticket);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        ticketRepository.save(ticket);
        // when
        String username = ticketRepository
                .findUsernameOfTicketUserOwner(ticket.getId())
                .orElseThrow(IllegalStateException::new);
        String movieTitle = ticketRepository
                .findMovieTitleAssociatedWithTicket(ticket.getId())
                .orElseThrow(IllegalStateException::new);
        Letter showroomLetter = ticketRepository
                .findShowroomLetterAssociatedWithTicket(ticket.getId())
                .orElseThrow(IllegalStateException::new);
        LocalDateTime showtime = ticketRepository
                .findShowtimeOfScreeningAssociatedWithTicket(ticket.getId())
                .orElseThrow(IllegalStateException::new);
        LocalDateTime endtime = ticketRepository
                .findEndtimeOfScreeningAssociatedWithTicket(ticket.getId())
                .orElseThrow(IllegalStateException::new);
        ShowroomSeat testShowroomSeat = ticketRepository
                .findShowroomSeatAssociatedWithTicket(ticket.getId())
                .orElseThrow(IllegalStateException::new);
        // then
        assertEquals("TestUser123", username);
        assertEquals("Test Title", movieTitle);
        assertEquals(Letter.C, showroomLetter);
        assertEquals(localDateTime, showtime);
        assertEquals(
                localDateTime.plusHours(1).plusMinutes(30), endtime);
        assertEquals(showroomSeat, testShowroomSeat);
    }

}