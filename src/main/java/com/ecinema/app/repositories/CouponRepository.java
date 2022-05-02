package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Coupon;
import com.ecinema.app.utils.CouponType;
import com.ecinema.app.utils.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Coupon repository.
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, AbstractRepository {

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
