package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.Coupon;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.repositories.CouponRepository;
import com.ecinema.app.services.CouponService;
import com.ecinema.app.utils.CouponType;
import com.ecinema.app.utils.DiscountType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CouponServiceImpl extends AbstractServiceImpl<Coupon, CouponRepository>
        implements CouponService {

    public CouponServiceImpl(CouponRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Coupon coupon) {
        // detach Customer
        CustomerRoleDef customerRoleDef = coupon.getCustomerRoleDef();
        if (customerRoleDef != null) {
            customerRoleDef.getCoupons().remove(coupon);
            coupon.setCustomerRoleDef(null);
        }
    }

    @Override
    public List<Coupon> findAllByCouponType(CouponType couponType) {
        return repository.findAllByCouponType(couponType);
    }

    @Override
    public List<Coupon> findAllByDiscountType(DiscountType discountType) {
        return repository.findAllByDiscountType(discountType);
    }

}
