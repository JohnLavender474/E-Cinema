package com.ecinema.app.services;

import com.ecinema.app.entities.Coupon;
import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.*;
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

    private CustomerRoleDefService customerRoleDefService;
    private AddressService addressService;
    private TicketService ticketService;
    private ReviewService reviewService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
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
        ticketService = new TicketServiceImpl(ticketRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
        addressService = new AddressServiceImpl(addressRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        couponService = new CouponServiceImpl(couponRepository);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService, ticketService,
                paymentCardService, couponService);
    }

    @Test
    void deleteCouponCascade() {
        // given
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getCoupons().add(coupon);
        given(couponRepository.findById(1L))
                .willReturn(Optional.of(coupon));
        couponService.save(coupon);
        assertEquals(customerRoleDef, coupon.getCustomerRoleDef());
        assertTrue(customerRoleDef.getCoupons().contains(coupon));
        // when
        couponService.delete(coupon);
        // then
        assertNotEquals(customerRoleDef, coupon.getCustomerRoleDef());
        assertFalse(customerRoleDef.getCoupons().contains(coupon));
    }

}