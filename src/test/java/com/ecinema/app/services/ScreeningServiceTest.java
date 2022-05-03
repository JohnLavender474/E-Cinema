package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.forms.ScreeningForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.Duration;
import com.ecinema.app.utils.Letter;
import com.ecinema.app.validators.MovieValidator;
import com.ecinema.app.validators.ReviewValidator;
import com.ecinema.app.validators.ScreeningFormValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private TicketService ticketService;
    private MovieService movieService;
    private ShowroomSeatService showroomSeatService;
    private ShowroomService showroomService;
    private ReviewService reviewService;
    @MockBean
    private ScreeningFormValidator screeningFormValidator;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ShowroomRepository showroomRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(
                screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(
                screeningRepository, movieRepository, showroomRepository,
                screeningSeatService, screeningFormValidator);
        showroomSeatService = new ShowroomSeatServiceImpl(
                showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(
                showroomRepository, showroomSeatService,
                screeningService, null);
        reviewService = new ReviewServiceImpl(reviewRepository);
        movieService = new MovieServiceImpl(movieRepository, reviewService,
                                            screeningService, null,
                                            null, null);
    }

    @Test
    void deleteScreeningCascade() {
        // given
        Screening screening = new Screening();
        screening.setId(1L);
        given(screeningRepository.findById(1L))
                .willReturn(Optional.of(screening));
        Movie movie = new Movie();
        movie.getScreenings().add(screening);
        screening.setMovie(movie);
        movieService.save(movie);
        Showroom showroom = new Showroom();
        showroom.getScreenings().add(screening);
        screening.setShowroom(showroom);
        showroomService.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setShowroom(showroom);
        showroom.getShowroomSeats().add(showroomSeat);
        showroomSeatService.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(2L);
        screeningSeat.setScreening(screening);
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screening.getScreeningSeats().add(screeningSeat);
        given(screeningSeatRepository.findById(2L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeatService.save(screeningSeat);
        Ticket ticket = new Ticket();
        ticket.setId(3L);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        given(ticketRepository.findById(3L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        assertEquals(movie, screening.getMovie());
        assertTrue(movie.getScreenings().contains(screening));
        assertEquals(showroom, screening.getShowroom());
        assertTrue(showroom.getScreenings().contains(screening));
        assertEquals(showroomSeat, screeningSeat.getShowroomSeat());
        assertTrue(showroomSeat.getScreeningSeats().contains(screeningSeat));
        assertEquals(showroom, showroomSeat.getShowroom());
        assertTrue(showroom.getShowroomSeats().contains(showroomSeat));
        assertEquals(screeningSeat, ticket.getScreeningSeat());
        // when
        screeningService.delete(screening);
        // then
        assertNotEquals(movie, screening.getMovie());
        assertNull(screening.getMovie());
        assertFalse(movie.getScreenings().contains(screening));
        assertNotEquals(showroom, screening.getShowroom());
        assertNull(screening.getShowroom());
        assertFalse(showroom.getScreenings().contains(screening));
        assertNotEquals(showroomSeat, screeningSeat.getShowroomSeat());
        assertNull(screeningSeat.getShowroomSeat());
        assertFalse(showroomSeat.getScreeningSeats().contains(screeningSeat));
        assertNull(screeningSeat.getShowroomSeat());
        assertNotEquals(screeningSeat, ticket.getScreeningSeat());
        assertNull(ticket.getScreeningSeat());
        assertNull(screeningSeat.getTicket());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void screeningDto() {
        // given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("test");
        movieService.save(movie);
        Showroom showroom = new Showroom();
        showroom.setId(2L);
        showroom.setShowroomLetter(Letter.A);
        given(showroomRepository.save(showroom))
                .willReturn(showroom);
        showroomService.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setId(3L);
        showroomSeat.setShowroom(showroom);
        showroom.getShowroomSeats().add(showroomSeat);
        showroomSeat.setRowLetter(Letter.C);
        showroomSeat.setSeatNumber(14);
        given(showroomSeatRepository.findById(3L))
                .willReturn(Optional.of(showroomSeat));
        Screening screening = new Screening();
        screening.setId(4L);
        screening.setShowDateTime(LocalDateTime.of(2022, Month.MAY, 1, 12, 0));
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        given(screeningRepository.findById(4L))
                .willReturn(Optional.of(screening));
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(5L);
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeat.setScreening(screening);
        screening.getScreeningSeats().add(screeningSeat);
        given(screeningSeatRepository.findById(5L))
                .willReturn(Optional.of(screeningSeat));
        given(screeningSeatRepository.save(screeningSeat))
                .willReturn(screeningSeat);
        screeningSeatService.save(screeningSeat);
        // when
        ScreeningDto screeningDto = screeningService.convertToDto(screening.getId());
        // then
        assertEquals(screening.getId(), screeningDto.getId());
        assertEquals("test", screeningDto.getMovieTitle());
        assertEquals(Letter.A, screeningDto.getShowroomLetter());
        assertEquals(LocalDateTime.of(2022, Month.MAY, 1, 12, 0),
                     screeningDto.getShowDateTime());
        assertNotNull(screeningDto);
    }

    @Test
    void submitSubmitScreeningForm() {
        // given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDuration(new Duration(1, 30));
        given(movieRepository.save(movie))
                .willReturn(movie);
        movieService.save(movie);
        Showroom showroom = new Showroom();
        showroom.setId(2L);
        given(showroomRepository.save(showroom))
                .willReturn(showroom);
        showroomService.save(showroom);
        for (int i = 0; i < 5; i++) {
            ShowroomSeat showroomSeat = new ShowroomSeat();
            showroomSeat.setShowroom(showroom);
            showroom.getShowroomSeats().add(showroomSeat);
            given(showroomSeatRepository.save(showroomSeat))
                    .willReturn(showroomSeat);
            showroomSeatService.save(showroomSeat);
        }
        // when
        ScreeningForm screeningForm = new ScreeningForm();
        screeningForm.setMovieId(1L);
        screeningForm.setShowroomId(2L);
        screeningForm.setShowtimeDay(1);
        screeningForm.setShowtimeMonth(Month.JANUARY);
        screeningForm.setShowtimeYear(2023);
        screeningForm.setShowtimeHour(1);
        screeningForm.setShowtimeMinute(0);
        LocalDateTime showDateTime = LocalDateTime.of(screeningForm.getShowtimeYear(),
                                                      screeningForm.getShowtimeMonth(),
                                                      screeningForm.getShowtimeDay(),
                                                      screeningForm.getShowtimeHour(),
                                                      screeningForm.getShowtimeMinute());
        LocalDateTime endDateTime = showDateTime
                .plusHours(movie.getDuration().getHours())
                .plusMinutes(movie.getDuration().getMinutes());
        Screening screening = new Screening();
        screening.setId(3L);
        screening.setShowDateTime(showDateTime);
        screening.setEndDateTime(endDateTime);
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        given(screeningRepository.findById(3L))
                .willReturn(Optional.of(screening));
        ScreeningDto screeningDto = screeningService.convertToDto(3L);
        // then
        assertEquals(movie.getId(), screeningDto.getMovieId());
        assertEquals(showroom.getId(), screeningDto.getShowroomId());
        assertEquals(movie.getTitle(), screeningDto.getMovieTitle());
        assertEquals(showroom.getShowroomLetter(), screeningDto.getShowroomLetter());
        assertEquals(LocalDateTime.of(2023, Month.JANUARY, 1, 1, 0),
                     screeningDto.getShowDateTime());
        assertEquals(LocalDateTime.of(2023, Month.JANUARY, 1, 2, 30),
                     screeningDto.getEndDateTime());
        assertEquals(showroom.getShowroomSeats().size(), screeningDto.getSeatsAvailable());
        assertEquals(screeningDto.getSeatsAvailable(), screeningDto.getTotalSeatsInRoom());
        assertEquals(0, screeningDto.getSeatsBooked());
    }

}