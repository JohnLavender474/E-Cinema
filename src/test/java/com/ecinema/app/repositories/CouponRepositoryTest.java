package com.ecinema.app.repositories;

import com.ecinema.app.entities.Coupon;
import com.ecinema.app.utils.constants.CouponType;
import com.ecinema.app.utils.constants.DiscountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Test
    void findAllByCouponType() {
        // given
        List<Coupon> coupons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Coupon coupon = new Coupon();
            int j = i % CouponType.values().length;
            coupon.setCouponType(CouponType.values()[j]);
            couponRepository.save(coupon);
            coupons.add(coupon);
        }
        List<Coupon> control = coupons.stream()
                .filter(coupon -> coupon.getCouponType().equals(CouponType.FOOD_DRINK_COUPON))
                .collect(Collectors.toList());
        // when
        List<Coupon> test = couponRepository.findAllByCouponType(CouponType.FOOD_DRINK_COUPON);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByDiscountType() {
        // given
        List<Coupon> coupons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Coupon coupon = new Coupon();
            int j = i % DiscountType.values().length;
            coupon.setDiscountType(DiscountType.values()[j]);
            couponRepository.save(coupon);
            coupons.add(coupon);
        }
        List<Coupon> control = coupons.stream()
                .filter(coupon -> coupon.getDiscountType().equals(DiscountType.FIXED_DISCOUNT))
                .collect(Collectors.toList());
        // when
        List<Coupon> test = couponRepository.findAllByDiscountType(DiscountType.FIXED_DISCOUNT);
        // then
        assertEquals(control, test);
    }

}