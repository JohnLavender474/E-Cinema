package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.domain.validators.MovieValidator;
import com.ecinema.app.domain.validators.ReviewValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    private PaymentCardService paymentCardService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private CouponService couponService;
    private MovieService movieService;
    private ScreeningSeatService screeningSeatService;
    private ScreeningService screeningService;
    private CustomerAuthorityService customerAuthorityService;
    private UserService userService;
    private ReviewValidator reviewValidator;
    private MovieValidator movieValidator;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private CustomerAuthorityRepository customerAuthorityRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        movieValidator = new MovieValidator();
        reviewValidator = new ReviewValidator();
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        reviewService = new ReviewServiceImpl(
                reviewRepository, movieRepository,
                customerAuthorityRepository, reviewValidator);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
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
        userService = new UserServiceImpl(
                userRepository, customerAuthorityService,
                null, null, null,
                null, null, null);
    }

    @Test
    void deleteReviewCascade() {
        // given
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthorityService.save(customerAuthority);
        Movie movie = new Movie();
        movieService.save(movie);
        Review review = new Review();
        review.setId(1L);
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
        review.setMovie(movie);
        movie.getReviews().add(review);
        given(reviewRepository.findById(1L))
                .willReturn(Optional.of(review));
        reviewService.save(review);
        assertTrue(customerAuthority.getReviews().contains(review));
        assertEquals(customerAuthority, review.getWriter());
        assertTrue(movie.getReviews().contains(review));
        assertEquals(movie, review.getMovie());
        // when
        reviewService.delete(review);
        // then
        assertFalse(customerAuthority.getReviews().contains(review));
        assertNotEquals(customerAuthority, review.getWriter());
        assertFalse(movie.getReviews().contains(review));
        assertNotEquals(movie, review.getMovie());
    }

    @Test
    void reviewDto() {
        // given
        User user = new User();
        user.setUsername("test username");
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityService.save(customerAuthority);
        Review review = new Review();
        review.setId(1L);
        review.setReview("test review");
        review.setWriter(customerAuthority);
        customerAuthority.getReviews().add(review);
        review.setIsCensored(false);
        review.setCreationDateTime(LocalDateTime.of(
                2022, Month.APRIL, 28, 22, 55));
        given(reviewRepository.findById(1L))
                .willReturn(Optional.of(review));
        reviewService.save(review);
        // when
        ReviewDto reviewDto = reviewService.convertIdToDto(1L);
        // then
        assertEquals(review.getId(), reviewDto.getId());
        assertEquals(review.getReview(), reviewDto.getReview());
        assertEquals(review.getIsCensored(), reviewDto.getIsCensored());
        assertEquals(LocalDateTime.of(2022, Month.APRIL, 28, 22, 55),
                     reviewDto.getCreationDateTime());
    }

    @Test
    void submitReviewForm() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        given(customerAuthorityRepository.findByUserWithId(1L))
                .willReturn(Optional.of(customerAuthority));
        customerAuthorityService.save(customerAuthority);
        Movie movie = new Movie();
        movie.setId(2L);
        given(movieRepository.findById(2L)).willReturn(Optional.of(movie));
        movieService.save(movie);
        given(reviewRepository.existsByWriterAndMovie(customerAuthority, movie))
                .willReturn(false);
        // when
        String reviewStr = "This movie is absolute garbage, I don't even know why I paid to see it.";
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setUserId(1L);
        reviewForm.setMovieId(2L);
        reviewForm.setReview(reviewStr);
        reviewForm.setRating(10);
        reviewService.submitReviewForm(reviewForm);
        // then
        assertEquals(1, customerAuthority.getReviews().size());
        Review review = customerAuthority.getReviews().stream().findFirst()
                                         .orElseThrow(IllegalStateException::new);
        assertEquals(movie, review.getMovie());
        assertEquals(customerAuthority, review.getWriter());
        assertEquals(10, review.getRating());
        assertEquals(reviewStr, review.getReview());
        assertFalse(review.getIsCensored());
    }

}