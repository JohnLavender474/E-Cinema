package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.CouponType;
import com.ecinema.app.domain.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CouponDto implements AbstractDto {
    private Long id;
    private Integer discount;
    private CouponType couponType;
    private DiscountType discountType;
}