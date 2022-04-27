package com.ecinema.app.services;

import com.ecinema.app.entities.Coupon;
import com.ecinema.app.utils.constants.CouponType;
import com.ecinema.app.utils.constants.DiscountType;

import java.util.List;

/**
 * The interface Coupon service.
 */
public interface CouponService extends AbstractService<Coupon> {

    /**
     * Find all by coupon type list.
     *
     * @param couponType the coupon type
     * @return the list
     */
    List<Coupon> findAllByCouponType(CouponType couponType);

    /**
     * Find all by discount type list.
     *
     * @param discountType the discount type
     * @return the list
     */
    List<Coupon> findAllByDiscountType(DiscountType discountType);

}