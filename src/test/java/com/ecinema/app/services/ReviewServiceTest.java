package com.ecinema.app.services;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.domain.validators.MovieValidator;
import com.ecinema.app.domain.validators.ReviewValidator;
import com.ecinema.app.repositories.*;
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
    private MovieService movieService;
    private ScreeningSeatService screeningSeatService;
    private ScreeningService screeningService;
    private CustomerService customerService;
    private UserService userService;
    private ReviewValidator reviewValidator;
    private MovieValidator movieValidator;
    private SecurityContext securityContext;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        securityContext = new SecurityContext();
        movieValidator = new MovieValidator();
        reviewValidator = new ReviewValidator();
        paymentCardService = new PaymentCardService(
                paymentCardRepository, null, null);
        reviewService = new ReviewService(
                reviewRepository, movieRepository,
                customerRepository, reviewValidator);
        ticketService = new TicketService(ticketRepository);
        screeningSeatService = new ScreeningSeatService(
                screeningSeatRepository, ticketService);
        screeningService = new ScreeningService(
                screeningRepository, movieRepository, null,
                showroomRepository, screeningSeatService, null);
        customerService = new CustomerService(
                customerRepository, screeningSeatRepository,
                null, reviewService, ticketService,
                paymentCardService, securityContext);
        movieService = new MovieService(
                movieRepository, reviewService,
                screeningService, movieValidator);
        userService = new UserService(
                userRepository, customerService,
                null, null, null,
                null, null);
    }

    @Test
    void deleteReviewCascade() {
        // given
        Customer customer = new Customer();
        Movie movie = new Movie();
        Review review = new Review();
        review.setId(1L);
        review.setWriter(customer);
        customer.getReviews().add(review);
        review.setMovie(movie);
        movie.getReviews().add(review);
        assertTrue(customer.getReviews().contains(review));
        assertEquals(customer, review.getWriter());
        assertTrue(movie.getReviews().contains(review));
        assertEquals(movie, review.getMovie());
        // when
        reviewService.delete(review);
        // then
        assertFalse(customer.getReviews().contains(review));
        assertNotEquals(customer, review.getWriter());
        assertFalse(movie.getReviews().contains(review));
        assertNotEquals(movie, review.getMovie());
    }

    @Test
    void reviewDto() {
        // given
        User user = new User();
        user.setUsername("test username");
        userService.save(user);
        Customer customer = new Customer();
        customer.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customer);
        customerService.save(customer);
        Review review = new Review();
        review.setId(1L);
        review.setReview("test review");
        review.setWriter(customer);
        customer.getReviews().add(review);
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

    @Test
    void submitReviewForm() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        Customer customer = new Customer();
        customer.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customer);
        given(customerRepository.findByUserWithId(1L))
                .willReturn(Optional.of(customer));
        customerService.save(customer);
        Movie movie = new Movie();
        movie.setId(2L);
        given(movieRepository.findById(2L)).willReturn(Optional.of(movie));
        movieService.save(movie);
        given(reviewRepository.existsByWriterAndMovie(customer, movie))
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
        assertEquals(1, customer.getReviews().size());
        Review review = customer.getReviews().stream().findFirst()
                                .orElseThrow(IllegalStateException::new);
        assertEquals(movie, review.getMovie());
        assertEquals(customer, review.getWriter());
        assertEquals(10, review.getRating());
        assertEquals(reviewStr, review.getReview());
        assertFalse(review.getIsCensored());
    }

}