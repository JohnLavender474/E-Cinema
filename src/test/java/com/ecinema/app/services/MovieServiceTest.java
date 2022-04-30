package com.ecinema.app.services;

import com.ecinema.app.dtos.*;
import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.Duration;
import com.ecinema.app.utils.Letter;
import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * The type Movie service test.
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private AddressService addressService;
    private MovieService movieService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private CouponService couponService;
    private PaymentCardService paymentCardService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningSeatService screeningSeatService;
    private ScreeningService screeningService;
    private CustomerRoleDefService customerRoleDefService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(addressRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        couponService = new CouponServiceImpl(couponRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(screeningRepository, screeningSeatService);
        movieService = new MovieServiceImpl(movieRepository, reviewService, screeningService);
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository, reviewService,
                                                                ticketService, paymentCardService, couponService);
        showroomSeatService = new ShowroomSeatServiceImpl(showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(showroomRepository, showroomSeatService, screeningService);
    }

    /**
     * Delete movie cascade.
     */
    @Test
    void deleteMovieCascade() {
        // given
        Movie movie = new Movie();
        movie.setId(1L);
        given(movieRepository.findById(1L))
                .willReturn(Optional.of(movie));
        movieService.save(movie);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Review review = new Review();
        review.setId(2L);
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        review.setMovie(movie);
        movie.getReviews().add(review);
        given(reviewRepository.findById(2L))
                .willReturn(Optional.of(review));
        reviewService.save(review);
        Screening screening = new Screening();
        screening.setId(3L);
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        given(screeningRepository.findById(3L))
                .willReturn(Optional.of(screening));
        screeningService.save(screening);
        assertTrue(customerRoleDef.getReviews().contains(review));
        assertEquals(movie, review.getMovie());
        assertTrue(movie.getScreenings().contains(screening));
        assertEquals(movie, screening.getMovie());
        // when
        movieService.delete(movie);
        // then
        assertFalse(customerRoleDef.getReviews().contains(review));
        assertNull(review.getMovie());
        assertFalse(movie.getScreenings().contains(screening));
        assertNull(screening.getMovie());
    }

    /**
     * Test movie dto. Mockito complains about unnecessary stubbings, hence LENIENT setting.
     * Mockito is unable to recognize chain service calls (another service being called within a service).
     */
    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void movieDto() {
        // given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("title");
        movie.setSynopsis("synopsis");
        movie.setDuration(new Duration(1, 30));
        movie.setReleaseDate(LocalDate.of(2022, Month.APRIL, 28));
        movie.setMsrbRating(MsrbRating.PG);
        movie.setCast(new HashSet<>() {{
            add("test1");
            add("test2");
        }});
        movie.setDirector("test3");
        movie.setMovieCategories(EnumSet.of(MovieCategory.ACTION, MovieCategory.DRAMA));
        given(movieRepository.findById(1L))
                .willReturn(Optional.of(movie));
        movieService.save(movie);
        Review review = new Review();
        review.setId(1L);
        review.setReview("meh, it's okay");
        review.setLikes(5);
        review.setDislikes(1);
        review.setMovie(movie);
        movie.getReviews().add(review);
        given(reviewRepository.findById(1L))
                .willReturn(Optional.of(review));
        reviewService.save(review);
        Showroom showroom = new Showroom();
        showroom.setId(1L);
        showroom.setShowroomLetter(Letter.A);
        showroomService.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setId(1L);
        showroomSeat.setShowroom(showroom);
        showroomSeat.setRowLetter(Letter.A);
        showroomSeat.setSeatNumber(1);
        showroom.getShowroomSeats().add(showroomSeat);
        showroomSeat.setShowroom(showroom);
        given(showroomSeatRepository.findById(1L))
                .willReturn(Optional.of(showroomSeat));
        showroomSeatService.save(showroomSeat);
        Screening screening = new Screening();
        screening.setId(1L);
        screening.setMovie(movie);
        screening.setShowroom(showroom);
        screening.setShowDateTime(LocalDateTime.of(2022, Month.MARCH, 28, 12, 0));
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        given(screeningRepository.findById(1L))
                .willReturn(Optional.of(screening));
        screeningService.save(screening);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(1L);
        screening.getScreeningSeats().add(screeningSeat);
        screeningSeat.setScreening(screening);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeat.setShowroomSeat(showroomSeat);
        given(screeningSeatRepository.findById(1L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeatService.save(screeningSeat);
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        given(ticketRepository.findById(1L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        // when
        MovieDto movieDto = movieService.convert(1L);
        // then
        assertEquals(movie.getId(), movieDto.getId());
        assertEquals(movie.getTitle(), movieDto.getTitle());
        assertEquals(movie.getSynopsis(), movieDto.getSynopsis());
        assertEquals(movie.getDuration(), movieDto.getDuration());
        assertEquals(movie.getReleaseDate(), movieDto.getReleaseDate());
        assertEquals(movie.getMsrbRating(), movieDto.getMsrbRating());
        assertEquals(movie.getCast(), movieDto.getCast());
        assertEquals(movie.getDirector(), movieDto.getDirector());
        assertEquals(movie.getMovieCategories(), movieDto.getMovieCategories());
        assertEquals(1, movieDto.getReviewDtos().size());
        ReviewDto reviewDto = movieDto.getReviewDtos().get(0);
        assertEquals(review.getId(), reviewDto.getId());
        assertEquals(review.getReview(), reviewDto.getReview());
        assertEquals(review.getLikes(), reviewDto.getLikes());
        assertEquals(review.getDislikes(), reviewDto.getDislikes());
        assertEquals(review.getIsCensored(), reviewDto.getIsCensored());
        assertEquals(review.getCreationDateTime(), reviewDto.getCreationDateTime());
        assertEquals(1, movieDto.getScreeningDtos().size());
        ScreeningDto screeningDto = movieDto.getScreeningDtos().get(0);
        assertEquals(screening.getId(), screeningDto.getId());
        assertEquals(movie.getTitle(), screeningDto.getMovieTitle());
        assertEquals(showroom.getShowroomLetter(), screeningDto.getShowroomLetter());
        assertEquals(screening.getShowDateTime(), screeningDto.getShowDateTime());
        assertEquals(1, screeningDto.getScreeningSeats().size());
        ScreeningSeatDto screeningSeatDto = ((TreeSet<ScreeningSeatDto>) screeningDto.getScreeningSeats()).last();
        assertEquals(screeningSeat.getId(), screeningSeatDto.getId());
        assertTrue(screeningSeatDto.getIsBooked());
    }

}