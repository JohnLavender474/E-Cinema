package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.CouponDto;
import com.ecinema.app.domain.entities.Coupon;
import com.ecinema.app.domain.enums.CouponType;
import com.ecinema.app.domain.enums.DiscountType;
import com.ecinema.app.exceptions.FatalErrorException;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.util.List;

/**
 * The interface Coupon service.
 */
public interface CouponService extends AbstractService<Coupon>, EntityDtoConverter<Coupon, CouponDto> {

    /**
     * Find all by user with id list.
     *
     * @param userId the user id
     * @return the list
     * @throws NoEntityFoundException      the no entity found exception
     * @throws InvalidAssociationException the invalid association exception
     */
    List<CouponDto> findAllByUserWithId(Long userId)
            throws NoEntityFoundException, InvalidAssociationException;

    /**
     * Find all by customer role def with id list.
     *
     * @param customerRoleDefId the customer role def id
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<CouponDto> findAllByCouponOwnerWithId(Long customerRoleDefId)
            throws NoEntityFoundException;

    /**
     * Find all by coupon type list.
     *
     * @param couponType the coupon type
     * @return the list
     */
    List<CouponDto> findAllByCouponType(CouponType couponType);

    /**
     * Find all by discount type list.
     *
     * @param discountType the discount type
     * @return the list
     */
    List<CouponDto> findAllByDiscountType(DiscountType discountType);

}
