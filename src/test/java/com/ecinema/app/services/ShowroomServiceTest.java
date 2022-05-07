package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.validators.ScreeningValidator;
import com.ecinema.app.domain.validators.ShowroomValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * The type Showroom service test.
 */
@ExtendWith(MockitoExtension.class)
class ShowroomServiceTest {

    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private TicketService ticketService;
    private ShowroomValidator showroomValidator;
    private ScreeningValidator screeningValidator;
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
    private MovieRepository movieRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        showroomValidator = new ShowroomValidator();
        screeningValidator = new ScreeningValidator();
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(
                screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(
                screeningRepository, movieRepository,
                showroomRepository,  screeningSeatService,
                screeningValidator);
        showroomSeatService = new ShowroomSeatServiceImpl(
                showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(
                showroomRepository, showroomSeatService,
                screeningService, showroomValidator);
    }

    /**
     * Find by showroom letter.
     */
    @Test
    void findByShowroomLetter() {
        // given
        Map<Letter, Showroom> showrooms = new EnumMap<>(Letter.class);
        for (int i = 0; i < Letter.values().length; i++) {
            Showroom showroom = new Showroom();
            Letter showroomLetter = Letter.values()[i];
            showroom.setShowroomLetter(showroomLetter);
            showroomService.save(showroom);
            showrooms.put(showroomLetter, showroom);
        }
        Showroom control = showrooms.get(Letter.A);
        given(showroomRepository.findByShowroomLetter(Letter.A))
                .willReturn(Optional.of(showrooms.get(Letter.A)));
        // when
        Optional<Showroom> test = showroomRepository.findByShowroomLetter(Letter.A);
        // then
        assertTrue(test.isPresent());
        assertEquals(control, test.get());
    }

    /**
     * Find by showroom seats contains.
     */
    @Test
    void findByShowroomSeatsContains() {
        // given
        Showroom showroom = new Showroom();
        showroom.setId(1000L);
        given(showroomRepository.findById(1000L))
                .willReturn(Optional.of(showroom));
        List<ShowroomSeat> showroomSeats = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Letter rowLetter = Letter.values()[i];
            for (int j = 0; j < 20; j++) {
                ShowroomSeat showroomSeat = new ShowroomSeat();
                showroomSeat.setId((long) (20 * i) + j);
                showroomSeat.setRowLetter(rowLetter);
                showroomSeat.setSeatNumber(j);
                showroomSeat.setShowroom(showroom);
                showroom.getShowroomSeats().add(showroomSeat);
                showroomSeats.add(showroomSeat);
            }
        }
        given(showroomRepository.findByShowroomSeatsContains(any()))
                .willReturn(Optional.of(showroom));
        // when
        ShowroomDto test = showroomService.findByShowroomSeatsContains(showroomSeats.get(0));
        // then
        assertEquals(showroom.getId(), test.getId());
    }

    @Test
    void deleteShowroomCascade() {
        // given
        Showroom showroom = new Showroom();
        showroom.setId(1L);
        showroom.setShowroomLetter(Letter.A);
        given(showroomRepository.findById(1L))
                .willReturn(Optional.of(showroom));
        showroomService.save(showroom);
        List<ShowroomSeat> showroomSeats = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ShowroomSeat showroomSeat = new ShowroomSeat();
            showroomSeat.setId(3L + i);
            showroomSeat.setShowroom(showroom);
            showroom.getShowroomSeats().add(showroomSeat);
            given(showroomSeatRepository.findById(3L + i))
                    .willReturn(Optional.of(showroomSeat));
            showroomSeatService.save(showroomSeat);
            showroomSeats.add(showroomSeat);
        }
        List<Screening> screenings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Screening screening = new Screening();
            screening.setId(6L + i);
            screening.setShowroom(showroom);
            showroom.getScreenings().add(screening);
            given(screeningRepository.findById(6L + i))
                    .willReturn(Optional.of(screening));
            screeningService.save(screening);
            screenings.add(screening);
        }
        for (ShowroomSeat showroomSeat : showroomSeats) {
            assertEquals(showroom, showroomSeat.getShowroom());
        }
        for (Screening screening : screenings) {
            assertEquals(showroom, screening.getShowroom());
        }
        // when
        showroomService.delete(showroom);
        // then
        for (ShowroomSeat showroomSeat : showroomSeats) {
            assertNull(showroomSeat.getShowroom());
        }
        for (Screening screening : screenings) {
            assertNull(screening.getShowroom());
        }
    }

}