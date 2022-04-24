package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.CouponType;
import com.ecinema.app.utils.constants.DiscountType;
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

    @ManyToOne
    @JoinColumn
    private CustomerRoleDef customerRoleDef;

}
