package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.domain.enums.UserRole;
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
    private CustomerRoleDefService customerRoleDefService;
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
    private CustomerRoleDefRepository customerRoleDefRepository;
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
                customerRoleDefRepository, reviewValidator);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        screeningSeatService = new ScreeningSeatServiceImpl(
                screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(
                screeningRepository, movieRepository, showroomRepository,
                screeningSeatService, null);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService,
                ticketService, paymentCardService, couponService);
        movieService = new MovieServiceImpl(
                movieRepository, reviewService,
                screeningService, movieValidator);
        userService = new UserServiceImpl(
                userRepository, customerRoleDefService,
                null, null, null);
    }

    @Test
    void deleteReviewCascade() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Movie movie = new Movie();
        movieService.save(movie);
        Review review = new Review();
        review.setId(1L);
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        review.setMovie(movie);
        movie.getReviews().add(review);
        given(reviewRepository.findById(1L))
                .willReturn(Optional.of(review));
        reviewService.save(review);
        assertTrue(customerRoleDef.getReviews().contains(review));
        assertEquals(customerRoleDef, review.getWriter());
        assertTrue(movie.getReviews().contains(review));
        assertEquals(movie, review.getMovie());
        // when
        reviewService.delete(review);
        // then
        assertFalse(customerRoleDef.getReviews().contains(review));
        assertNotEquals(customerRoleDef, review.getWriter());
        assertFalse(movie.getReviews().contains(review));
        assertNotEquals(movie, review.getMovie());
    }

    @Test
    void reviewDto() {
        // given
        User user = new User();
        user.setUsername("test username");
        userService.save(user);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
        customerRoleDefService.save(customerRoleDef);
        Review review = new Review();
        review.setId(1L);
        review.setReview("test review");
        review.setWriter(customerRoleDef);
        customerRoleDef.getReviews().add(review);
        review.setIsCensored(false);
        review.setCreationDateTime(LocalDateTime.of(
                2022, Month.APRIL, 28, 22, 55));
        given(reviewRepository.findById(1L))
                .willReturn(Optional.of(review));
        reviewService.save(review);
        // when
        ReviewDto reviewDto = reviewService.convertToDto(1L);
        // then
        assertEquals(review.getId(), reviewDto.getId());
        assertEquals(review.getReview(), reviewDto.getReview());
        assertEquals(review.getIsCensored(), reviewDto.getIsCensored());
        assertEquals(LocalDateTime.of(2022, Month.APRIL, 28, 22, 55),
                     reviewDto.getCreationDateTime());
    }

}