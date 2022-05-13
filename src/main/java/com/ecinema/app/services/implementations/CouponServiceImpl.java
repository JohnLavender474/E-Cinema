package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.CouponDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CouponRepository;
import com.ecinema.app.repositories.CustomerAuthorityRepository;
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
    private final CustomerAuthorityRepository customerAuthorityRepository;

    public CouponServiceImpl(CouponRepository repository, UserRepository userRepository,
                             CustomerAuthorityRepository customerAuthorityRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.customerAuthorityRepository = customerAuthorityRepository;
    }

    @Override
    protected void onDelete(Coupon coupon) {
        // detach Customer
        logger.debug("Coupon on delete");
        CustomerAuthority customerAuthority = coupon.getCouponOwner();
        logger.debug("Detaching " + customerAuthority + " from " + coupon);
        if (customerAuthority != null) {
            customerAuthority.getCoupons().remove(coupon);
            coupon.setCouponOwner(null);
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
        String username = coupon.getCouponOwner().getUser().getUsername();
        info.add(coupon.getCouponType() + " will be deleted and no longer usable by " + username);
        if (coupon.getTicket() != null) {
            Screening screening = coupon.getTicket().getScreeningSeat().getScreening();
            String movieTitle = screening.getMovie().getTitle();
            info.add("Ticket for seat " + coupon.getTicket().getScreeningSeat().seatDesignation() +
                             " in screening for " + movieTitle + " at " + screening.showtimeFormatted() +
                             " in showroom " + screening.getShowroom().getShowroomLetter() +
                             " purchased by " + username + " will lose coupon discount");
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
        CustomerAuthority customerAuthority = (CustomerAuthority) user.getUserAuthorities().get(UserAuthority.CUSTOMER);
        if (customerAuthority == null) {
            throw new InvalidAssociationException("User does not have CUSTOMER role def");
        }
        logger.debug("Customer role def found by user: " + customerAuthority);
        return findAllByCustomerRoleDef(customerAuthority);
    }

    @Override
    public List<CouponDto> findAllByCouponOwnerWithId(Long customerRoleDefId)
            throws NoEntityFoundException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Find coupons by customer role def id: " + customerRoleDefId);
        CustomerAuthority customerAuthority = customerAuthorityRepository.findById(customerRoleDefId)
                                                                         .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "id", customerRoleDefId));
        logger.debug("Found customer role def by id: " + customerAuthority);
        return findAllByCustomerRoleDef(customerAuthority);
    }

    private List<CouponDto> findAllByCustomerRoleDef(CustomerAuthority customerAuthority) {
        return convertToDto(customerAuthority.getCoupons());
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
    public CouponDto convertIdToDto(Long id)
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
