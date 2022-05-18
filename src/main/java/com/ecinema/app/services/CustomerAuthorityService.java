package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.CustomerAuthorityDto;
import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Customer role def service.
 */
public interface CustomerAuthorityService extends AbstractUserAuthorityService<CustomerAuthority>,
                                                  EntityDtoConverter<CustomerAuthority, CustomerAuthorityDto> {

    /**
     * Find by payment cards contains optional.
     *
     * @param paymentCard the payment card
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerAuthorityDto findByPaymentCardsContains(PaymentCard paymentCard)
            throws NoEntityFoundException;

    /**
     * Find by payment cards contains with id optional.
     *
     * @param paymentCardId the payment card id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerAuthorityDto findByPaymentCardsContainsWithId(Long paymentCardId)
            throws NoEntityFoundException;

    /**
     * Find by tickets contains optional.
     *
     * @param ticket the ticket
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerAuthorityDto findByTicketsContains(Ticket ticket)
            throws NoEntityFoundException;

    /**
     * Find by tickets contains with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerAuthorityDto findByTicketsContainsWithId(Long ticketId)
            throws NoEntityFoundException;

    /**
     * Find by reviews contains optional.
     *
     * @param review the review
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerAuthorityDto findByReviewsContains(Review review)
            throws NoEntityFoundException;

    /**
     * Find by reviews contains with id optional.
     *
     * @param reviewId the review id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerAuthorityDto findByReviewsContainsWithId(Long reviewId)
            throws NoEntityFoundException;

    /**
     * Find all by is censored list.
     *
     * @param isCensored the is censored
     * @return the list
     */
    List<CustomerAuthorityDto> findAllByIsCensored(boolean isCensored);

    /**
     * Find all dtos list.
     *
     * @return the list
     */
    List<CustomerAuthorityDto> findAllDtos();

    /**
     * Find all dtos page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<CustomerAuthorityDto> findAllDtos(Pageable pageable);

}
