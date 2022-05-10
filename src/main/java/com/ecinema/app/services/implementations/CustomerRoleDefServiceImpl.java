package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.CustomerRoleDefDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class CustomerRoleDefServiceImpl extends UserRoleDefServiceImpl<CustomerRoleDef,
        CustomerRoleDefRepository> implements CustomerRoleDefService {

    private final ReviewService reviewService;
    private final TicketService ticketService;
    private final CouponService couponService;
    private final PaymentCardService paymentCardService;

    public CustomerRoleDefServiceImpl(CustomerRoleDefRepository repository, ReviewService reviewService,
                                      TicketService ticketService, PaymentCardService paymentCardService,
                                      CouponService couponService) {
        super(repository);
        this.reviewService = reviewService;
        this.ticketService = ticketService;
        this.couponService = couponService;
        this.paymentCardService = paymentCardService;
    }

    @Override
    protected void onDelete(CustomerRoleDef customerRoleDef) {
        logger.debug("Customer role def on delete");
        // detach User
        super.onDelete(customerRoleDef);
        // cascade delete Reviews
        logger.debug("Deleting all associated reviews");
        reviewService.deleteAll(customerRoleDef.getReviews());
        // cascade delete Tickets
        logger.debug("Deleting all associated tickets");
        ticketService.deleteAll(customerRoleDef.getTickets());
        // cascade delete PaymentCards
        logger.debug("Deleting all associated payment cards");
        paymentCardService.deleteAll(customerRoleDef.getPaymentCards());
        // cascade delete Coupons
        logger.debug("Deleting all associated coupons");
        couponService.deleteAll(customerRoleDef.getCoupons());
        // detach Moderator censors
        ModeratorRoleDef moderatorRoleDef = customerRoleDef.getCensoredBy();
        logger.debug("Detaching moderator role def: " + moderatorRoleDef);
        if (moderatorRoleDef != null) {
            customerRoleDef.setCensoredBy(null);
            moderatorRoleDef.getCensoredCustomers().remove(customerRoleDef);
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = findById(id).orElseThrow(
                () -> new NoEntityFoundException("customer role def", "id", id));
        onDeleteInfo(customerRoleDef, info);
    }

    @Override
    public void onDeleteInfo(CustomerRoleDef customerRoleDef, Collection<String> info) {
        super.onDeleteInfo(customerRoleDef, info);
        customerRoleDef.getReviews().forEach(review -> reviewService.onDeleteInfo(review, info));
        customerRoleDef.getTickets().forEach(ticket -> ticketService.onDeleteInfo(ticket, info));
        customerRoleDef.getCoupons().forEach(coupon -> couponService.onDeleteInfo(coupon, info));
    }

    @Override
    public CustomerRoleDefDto findByPaymentCardsContains(PaymentCard paymentCard)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = repository.findByPaymentCardsContains(paymentCard)
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "payment card", paymentCard));
        return convertToDto(customerRoleDef);
    }

    @Override
    public CustomerRoleDefDto findByPaymentCardsContainsWithId(Long paymentCardId)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = repository.findByPaymentCardsContainsWithId(paymentCardId)
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "paymentcard id", paymentCardId));
        return convertToDto(customerRoleDef);
    }

    @Override
    public CustomerRoleDefDto findByTicketsContains(Ticket ticket)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = repository.findByTicketsContains(ticket).orElseThrow(
                () -> new NoEntityFoundException(
                        "customer role def", "ticket", ticket));
        return convertToDto(customerRoleDef);
    }

    @Override
    public CustomerRoleDefDto findByTicketsContainsWithId(Long ticketId)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = repository.findByTicketsContainsWithId(ticketId)
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "ticket id", ticketId));
        return convertToDto(customerRoleDef);
    }

    @Override
    public CustomerRoleDefDto findByReviewsContains(Review review)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = repository.findByReviewsContains(review).orElseThrow(
                () -> new NoEntityFoundException("customer role def", "review", review));
        return convertToDto(customerRoleDef);
    }

    @Override
    public CustomerRoleDefDto findByReviewsContainsWithId(Long reviewId)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = repository.findByReviewsContainsWithId(reviewId)
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "review id", reviewId));
        return convertToDto(customerRoleDef);
    }

    @Override
    public CustomerRoleDefDto convertToDto(Long id)
            throws NoEntityFoundException {
        CustomerRoleDef customerRoleDef = findById(id).orElseThrow(
                () -> new NoEntityFoundException("customer role def", "id", id));
        CustomerRoleDefDto customerRoleDefDto = new CustomerRoleDefDto();
        customerRoleDefDto.setId(customerRoleDef.getId());
        customerRoleDefDto.setUserId(customerRoleDef.getUser().getId());
        customerRoleDefDto.setIsCensored(customerRoleDef.getCensoredBy() != null);
        logger.debug("Converted " + customerRoleDef + " to DTO: " + customerRoleDefDto);
        return customerRoleDefDto;
    }

}
