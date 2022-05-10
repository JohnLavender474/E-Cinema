package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.CouponDto;
import com.ecinema.app.domain.entities.Coupon;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CouponRepository;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.CouponService;
import com.ecinema.app.domain.enums.CouponType;
import com.ecinema.app.domain.enums.DiscountType;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CouponServiceImpl extends AbstractServiceImpl<Coupon, CouponRepository>
        implements CouponService {

    private final UserRepository userRepository;
    private final CustomerRoleDefRepository customerRoleDefRepository;

    public CouponServiceImpl(CouponRepository repository, UserRepository userRepository,
                             CustomerRoleDefRepository customerRoleDefRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.customerRoleDefRepository = customerRoleDefRepository;
    }

    @Override
    protected void onDelete(Coupon coupon) {
        // detach Customer
        logger.debug("Coupon on delete");
        CustomerRoleDef customerRoleDef = coupon.getCustomerRoleDef();
        logger.debug("Detaching " + customerRoleDef + " from " + coupon);
        if (customerRoleDef != null) {
            customerRoleDef.getCoupons().remove(coupon);
            coupon.setCustomerRoleDef(null);
        }
        // detach Ticket
        Ticket ticket = coupon.getTicket();
        logger.debug("Detaching " + ticket + " from " + coupon);
        if (ticket != null) {
            ticket.getCoupons().remove(coupon);
            coupon.setTicket(null);
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        Coupon coupon = findById(id).orElseThrow(
                () -> new NoEntityFoundException("coupon", "id", id));
        onDeleteInfo(coupon, info);
    }

    @Override
    public void onDeleteInfo(Coupon coupon, Collection<String> info) {
        String username = coupon.getCustomerRoleDef().getUser().getUsername();
        info.add(username + " will lose coupon: " + coupon.getCouponType() + ", " + coupon.getDiscountType());
        if (coupon.getTicket() != null) {
            info.add(coupon.getTicket() + " will lose coupon");
        }
    }

    @Override
    public List<CouponDto> findAllByUserWithId(Long userId)
            throws NoEntityFoundException, InvalidAssociationException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Find all coupons by user with id " + userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        logger.debug("Found " + user + " by id");
        CustomerRoleDef customerRoleDef = (CustomerRoleDef) user.getUserRoleDefs().get(UserRole.CUSTOMER);
        if (customerRoleDef == null) {
            throw new InvalidAssociationException("User does not have CUSTOMER role def");
        }
        logger.debug("Customer role def found by user: " + customerRoleDef);
        return findAllByCustomerRoleDef(customerRoleDef);
    }

    @Override
    public List<CouponDto> findAllByCustomerRoleDefWithId(Long customerRoleDefId)
            throws NoEntityFoundException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Find coupons by customer role def id: " + customerRoleDefId);
        CustomerRoleDef customerRoleDef = customerRoleDefRepository.findById(customerRoleDefId)
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "id", customerRoleDefId));
        logger.debug("Found customer role def by id: " + customerRoleDef);
        return findAllByCustomerRoleDef(customerRoleDef);
    }

    private List<CouponDto> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef) {
        return convertToDto(customerRoleDef.getCoupons());
    }

    @Override
    public List<CouponDto> findAllByCouponType(CouponType couponType) {
        return convertToDto(repository.findAllByCouponType(couponType));
    }

    @Override
    public List<CouponDto> findAllByDiscountType(DiscountType discountType) {
        return convertToDto(repository.findAllByDiscountType(discountType));
    }

    @Override
    public CouponDto convertToDto(Long id)
            throws NoEntityFoundException {
        Coupon coupon = findById(id).orElseThrow(
                () -> new NoEntityFoundException("coupon", "id", id));
        CouponDto couponDto = new CouponDto();
        couponDto.setId(coupon.getId());
        couponDto.setDiscount(coupon.getDiscount());
        couponDto.setCouponType(coupon.getCouponType());
        couponDto.setDiscountType(coupon.getDiscountType());
        logger.debug("Converted " + coupon + " to DTO: " + couponDto);
        return couponDto;
    }

}
