package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.*;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.*;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.*;
import com.ecinema.app.domain.validators.MovieValidator;
import com.ecinema.app.domain.validators.ReviewValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(MovieServiceTest.class);

    private MovieService movieService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private CouponService couponService;
    private PaymentCardService paymentCardService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningSeatService screeningSeatService;
    private ScreeningService screeningService;
    private CustomerAuthorityService customerAuthorityService;
    private UserService userService;
    private MovieValidator movieValidator;
    private ReviewValidator reviewValidator;
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
    private CustomerAuthorityRepository customerAuthorityRepository;
    @Mock
    private UserRepository userRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        reviewValidator = new ReviewValidator();
        movieValidator = new MovieValidator();
        reviewService = new ReviewServiceImpl(
                reviewRepository, movieRepository,
                customerAuthorityRepository, reviewValidator);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        screeningSeatService = new ScreeningSeatServiceImpl(
                screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(
                screeningRepository, movieRepository, showroomRepository,
                screeningSeatService, null);
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService,
                ticketService, paymentCardService, couponService);
        movieService = new MovieServiceImpl(
                movieRepository, reviewService,
                screeningService, movieValidator);
        showroomSeatService = new ShowroomSeatServiceImpl(
                showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(
                showroomRepository, showroomSeatService,
                screeningService, null);
        userService = new UserServiceImpl(
                userRepository, customerAuthorityService,
                null, null, null,
                null, null, null);
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
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthorityService.save(customerAuthority);
        Review review = new Review();
        review.setId(2L);
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
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
        assertTrue(customerAuthority.getReviews().contains(review));
        assertEquals(movie, review.getMovie());
        assertTrue(movie.getScreenings().contains(screening));
        assertEquals(movie, screening.getMovie());
        // when
        movieService.delete(movie);
        // then
        assertFalse(customerAuthority.getReviews().contains(review));
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
        User user = new User();
        user.setUsername("test username");
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityService.save(customerAuthority);
        Review review = new Review();
        review.setId(1L);
        review.setRating(7);
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
        review.setReview("meh, it's okay");
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
        screening.setShowDateTime(LocalDateTime.of(2022, Month.MARCH,
                                                   28, 12, 0));
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        given(screeningRepository.findById(1L))
                .willReturn(Optional.of(screening));
        screeningService.save(screening);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(1L);
        screeningSeat.setShowroomSeat(showroomSeat);
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
        MovieDto movieDto = movieService.convertIdToDto(1L);
        // then
        assertEquals(movie.getId(), movieDto.getId());
        assertEquals(movie.getTitle(), movieDto.getTitle());
        assertEquals(movie.getSynopsis(), movieDto.getSynopsis());
        assertEquals(movie.getDuration(), movieDto.getDuration());
        assertEquals(movie.getMsrbRating(), movieDto.getMsrbRating());
        assertEquals(movie.getCast(), movieDto.getCast());
        assertEquals(movie.getDirector(), movieDto.getDirector());
        assertEquals(movie.getMovieCategories(), movieDto.getMovieCategories());
        assertEquals(movie.getReleaseDate().getDayOfMonth(), movieDto.getReleaseDay());
        assertEquals(movie.getReleaseDate().getMonth(), movieDto.getReleaseMonth());
        assertEquals(movie.getReleaseDate().getYear(), movieDto.getReleaseYear());
    }

    @Test
    void submitReviewForm() {
        // given
        Movie movie = new Movie();
        movie.setId(1L);
        given(movieRepository.findById(1L))
                .willReturn(Optional.of(movie));
        movieService.save(movie);
        User user = new User();
        user.setId(2L);
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setId(3L);
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        given(customerAuthorityRepository.findByUserWithId(2L))
                .willReturn(Optional.of(customerAuthority));
        customerAuthorityService.save(customerAuthority);
        // when
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setMovieId(1L);
        reviewForm.setUserId(2L);
        reviewForm.setRating(4);
        reviewForm.setReview("wow, this movie sucks so much!");
        reviewService.submitReviewForm(reviewForm);
        Set<Review> reviews = movie.getReviews();
        // then
        assertEquals(1, reviews.size());
        Review review = reviews.stream().findFirst().orElse(null);
        assertNotNull(review);
        assertEquals(movie, review.getMovie());
        assertEquals(reviewForm.getReview(), review.getReview());
        assertEquals(reviewForm.getRating(), review.getRating());
        assertEquals(customerAuthority, review.getWriter());
        assertEquals(user, review.getWriter().getUser());
    }

    @Test
    void onDeleteInfo() {
        // given
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movieService.save(movie);
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroomService.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setRowLetter(Letter.A);
        showroomSeat.setSeatNumber(1);
        showroomSeatService.save(showroomSeat);
        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setShowDateTime(LocalDateTime.now());
        movie.getScreenings().add(screening);
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screeningService.save(screening);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeat.setScreening(screening);
        screening.getScreeningSeats().add(screeningSeat);
        screeningSeatService.save(screeningSeat);
        User user = new User();
        user.setUsername("TestUser123");
        user.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        userService.save(user);
        userService.addUserAuthorityToUser(user, UserAuthority.CUSTOMER);
        CustomerAuthority customerAuthority = (CustomerAuthority) user.getUserAuthorities().get(UserAuthority.CUSTOMER);
        Review review = new Review();
        review.setMovie(movie);
        movie.getReviews().add(review);
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
        review.setReview("This is a movie review");
        reviewService.save(review);
        Ticket ticket = new Ticket();
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        ticket.setTicketOwner(customerAuthority);
        customerAuthority.getTickets().add(ticket);
        ticketService.save(ticket);
        Coupon coupon = new Coupon();
        coupon.setCouponType(CouponType.FOOD_DRINK_COUPON);
        coupon.setCouponOwner(customerAuthority);
        customerAuthority.getCoupons().add(coupon);
        coupon.setTicket(ticket);
        ticket.getCoupons().add(coupon);
        couponService.save(coupon);
        // when
        List<String> info = new ArrayList<>();
        movieService.onDeleteInfo(movie, info);
        // then look at logger debug messages
        info.forEach(logger::debug);
    }

}