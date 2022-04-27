package com.ecinema.app.services;

import com.ecinema.app.entities.Screening;
import com.ecinema.app.entities.Showroom;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.constants.Letter;
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

    private TheaterService theaterService;
    private AddressService addressService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private TicketService ticketService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private TheaterRepository theaterRepository;
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

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        showroomRepository.deleteAll();
        addressRepository.deleteAll();
        showroomRepository.deleteAll();
        showroomSeatRepository.deleteAll();
        screeningRepository.deleteAll();
        screeningSeatRepository.deleteAll();
        ticketRepository.deleteAll();
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
        List<ShowroomSeat> showroomSeats = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Letter rowLetter = Letter.values()[i];
            for (int j = 0; j < 20; j++) {
                ShowroomSeat showroomSeat = new ShowroomSeat();
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
        Optional<Showroom> test = showroomService.findByShowroomSeatsContains(showroomSeats.get(0));
        // then
        assertTrue(test.isPresent());
        assertEquals(showroom, test.get());
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
        Theater theater = new Theater();
        theater.setId(2L);
        theater.getShowrooms().put(Letter.A, showroom);
        showroom.setTheater(theater);
        theaterService.save(theater);
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
        assertEquals(theater, showroom.getTheater());
        assertFalse(theater.getShowrooms().isEmpty());
        for (ShowroomSeat showroomSeat : showroomSeats) {
            assertEquals(showroom, showroomSeat.getShowroom());
        }
        for (Screening screening : screenings) {
            assertEquals(showroom, screening.getShowroom());
        }
        // when
        showroomService.delete(showroom);
        // then
        assertNull(showroom.getTheater());
        assertTrue(theater.getShowrooms().isEmpty());
        for (ShowroomSeat showroomSeat : showroomSeats) {
            assertNull(showroomSeat.getShowroom());
        }
        for (Screening screening : screenings) {
            assertNull(screening.getShowroom());
        }
    }

}