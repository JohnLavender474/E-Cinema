package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.CouponType;
import com.ecinema.app.domain.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
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
    @ToString.Exclude
    private Ticket ticket;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private CustomerAuthority couponOwner;

}
