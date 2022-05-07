package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.CustomerRoleDefDto;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.entities.Review;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.modelmapper.internal.asm.tree.ModuleExportNode;

import java.util.Optional;

/**
 * The interface Customer role def service.
 */
public interface CustomerRoleDefService extends UserRoleDefService<CustomerRoleDef>,
                                                EntityDtoConverter<CustomerRoleDef, CustomerRoleDefDto> {

    /**
     * Find by payment cards contains optional.
     *
     * @param paymentCard the payment card
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerRoleDefDto findByPaymentCardsContains(PaymentCard paymentCard)
            throws NoEntityFoundException;

    /**
     * Find by payment cards contains with id optional.
     *
     * @param paymentCardId the payment card id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerRoleDefDto findByPaymentCardsContainsWithId(Long paymentCardId)
            throws NoEntityFoundException;

    /**
     * Find by tickets contains optional.
     *
     * @param ticket the ticket
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerRoleDefDto findByTicketsContains(Ticket ticket)
            throws NoEntityFoundException;

    /**
     * Find by tickets contains with id optional.
     *
     * @param ticketId the ticket id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerRoleDefDto findByTicketsContainsWithId(Long ticketId)
            throws NoEntityFoundException;

    /**
     * Find by reviews contains optional.
     *
     * @param review the review
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerRoleDefDto findByReviewsContains(Review review)
            throws NoEntityFoundException;

    /**
     * Find by reviews contains with id optional.
     *
     * @param reviewId the review id
     * @return the optional
     * @throws NoEntityFoundException the no entity found exception
     */
    CustomerRoleDefDto findByReviewsContainsWithId(Long reviewId)
            throws NoEntityFoundException;

}
