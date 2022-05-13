package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.CustomerAuthorityDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerAuthorityRepository;
import com.ecinema.app.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class CustomerAuthorityServiceImpl extends AbstractUserAuthorityServiceImpl<CustomerAuthority,
        CustomerAuthorityRepository> implements CustomerAuthorityService {

    private final ReviewService reviewService;
    private final TicketService ticketService;
    private final CouponService couponService;
    private final PaymentCardService paymentCardService;

    public CustomerAuthorityServiceImpl(CustomerAuthorityRepository repository, ReviewService reviewService,
                                        TicketService ticketService, PaymentCardService paymentCardService,
                                        CouponService couponService) {
        super(repository);
        this.reviewService = reviewService;
        this.ticketService = ticketService;
        this.couponService = couponService;
        this.paymentCardService = paymentCardService;
    }

    @Override
    protected void onDelete(CustomerAuthority customerAuthority) {
        logger.debug("Customer role def on delete");
        // detach User
        super.onDelete(customerAuthority);
        // cascade delete Reviews
        logger.debug("Deleting all associated reviews");
        reviewService.deleteAll(customerAuthority.getReviews());
        // cascade delete Tickets
        logger.debug("Deleting all associated tickets");
        ticketService.deleteAll(customerAuthority.getTickets());
        // cascade delete PaymentCards
        logger.debug("Deleting all associated payment cards");
        paymentCardService.deleteAll(customerAuthority.getPaymentCards());
        // cascade delete Coupons
        logger.debug("Deleting all associated coupons");
        couponService.deleteAll(customerAuthority.getCoupons());
        // detach Moderator censors
        ModeratorAuthority moderatorAuthority = customerAuthority.getCensoredBy();
        logger.debug("Detaching moderator role def: " + moderatorAuthority);
        if (moderatorAuthority != null) {
            customerAuthority.setCensoredBy(null);
            moderatorAuthority.getCensoredCustomers().remove(customerAuthority);
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = findById(id).orElseThrow(
                () -> new NoEntityFoundException("customer role def", "id", id));
        onDeleteInfo(customerAuthority, info);
    }

    @Override
    public void onDeleteInfo(CustomerAuthority customerAuthority, Collection<String> info) {
        super.onDeleteInfo(customerAuthority, info);
        customerAuthority.getReviews().forEach(review -> reviewService.onDeleteInfo(review, info));
        customerAuthority.getTickets().forEach(ticket -> ticketService.onDeleteInfo(ticket, info));
        customerAuthority.getCoupons().forEach(coupon -> couponService.onDeleteInfo(coupon, info));
    }

    @Override
    public CustomerAuthorityDto findByPaymentCardsContains(PaymentCard paymentCard)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = repository.findByPaymentCardsContains(paymentCard)
                                                        .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "payment card", paymentCard));
        return convertToDto(customerAuthority);
    }

    @Override
    public CustomerAuthorityDto findByPaymentCardsContainsWithId(Long paymentCardId)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = repository.findByPaymentCardsContainsWithId(paymentCardId)
                                                        .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "paymentcard id", paymentCardId));
        return convertToDto(customerAuthority);
    }

    @Override
    public CustomerAuthorityDto findByTicketsContains(Ticket ticket)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = repository.findByTicketsContains(ticket).orElseThrow(
                () -> new NoEntityFoundException(
                        "customer role def", "ticket", ticket));
        return convertToDto(customerAuthority);
    }

    @Override
    public CustomerAuthorityDto findByTicketsContainsWithId(Long ticketId)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = repository.findByTicketsContainsWithId(ticketId)
                                                        .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "ticket id", ticketId));
        return convertToDto(customerAuthority);
    }

    @Override
    public CustomerAuthorityDto findByReviewsContains(Review review)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = repository.findByReviewsContains(review).orElseThrow(
                () -> new NoEntityFoundException("customer role def", "review", review));
        return convertToDto(customerAuthority);
    }

    @Override
    public CustomerAuthorityDto findByReviewsContainsWithId(Long reviewId)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = repository.findByReviewsContainsWithId(reviewId)
                                                        .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "review id", reviewId));
        return convertToDto(customerAuthority);
    }

    @Override
    public CustomerAuthorityDto convertIdToDto(Long id)
            throws NoEntityFoundException {
        CustomerAuthority customerAuthority = findById(id).orElseThrow(
                () -> new NoEntityFoundException("customer role def", "id", id));
        CustomerAuthorityDto customerAuthorityDto = new CustomerAuthorityDto();
        customerAuthorityDto.setId(customerAuthority.getId());
        customerAuthorityDto.setUserId(customerAuthority.getUser().getId());
        customerAuthorityDto.setIsCensored(customerAuthority.getCensoredBy() != null);
        logger.debug("Converted " + customerAuthority + " to DTO: " + customerAuthorityDto);
        return customerAuthorityDto;
    }

}
