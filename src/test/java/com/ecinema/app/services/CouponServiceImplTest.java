package com.ecinema.app.services;

import com.ecinema.app.domain.entities.Coupon;
import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    private CustomerAuthorityService customerAuthorityService;
    private TicketService ticketService;
    private ReviewService reviewService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    @Mock
    private CustomerAuthorityRepository customerAuthorityRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        reviewService = new ReviewServiceImpl(
                reviewRepository, null,
                customerAuthorityRepository, null);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, null,
                                                        null);
        couponService = new CouponServiceImpl(couponRepository, null, null);
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService, ticketService,
                paymentCardService, couponService);
    }

    @Test
    void deleteCouponCascade() {
        // given
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthorityService.save(customerAuthority);
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCouponOwner(customerAuthority);
        customerAuthority.getCoupons().add(coupon);
        given(couponRepository.findById(1L))
                .willReturn(Optional.of(coupon));
        couponService.save(coupon);
        assertEquals(customerAuthority, coupon.getCouponOwner());
        assertTrue(customerAuthority.getCoupons().contains(coupon));
        // when
        couponService.delete(coupon);
        // then
        assertNotEquals(customerAuthority, coupon.getCouponOwner());
        assertFalse(customerAuthority.getCoupons().contains(coupon));
    }

}