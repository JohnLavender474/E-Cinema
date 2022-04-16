package com.ecinema.app.coupon;

import com.ecinema.app.abstraction.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Coupon extends AbstractEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column
    private Integer discount;

}
