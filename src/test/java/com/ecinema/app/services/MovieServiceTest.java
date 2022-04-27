package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.Movie;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Screening;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.annotation.XmlEnum;

import java.util.Optional;

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
        assertNotEquals(movie, review.getMovie());
        assertFalse(movie.getScreenings().contains(screening));
        assertNotEquals(movie, screening.getMovie());
    }

}