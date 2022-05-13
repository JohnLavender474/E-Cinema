package com.ecinema.app.services;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.util.List;

/**
 * The interface Payment card service.
 */
public interface PaymentCardService extends AbstractService<PaymentCard> {

    /**
     * Submit payment card form.
     *
     * @param paymentCardForm the payment card form
     * @throws NoEntityFoundException the no entity found exception
     */
    void submitPaymentCardForm(PaymentCardForm paymentCardForm)
        throws NoEntityFoundException, InvalidArgsException;

    /**
     * Find all by customer role def list.
     *
     * @param cardOwner the customer role def
     * @return the list
     */
    List<PaymentCard> findAllByCardOwner(CustomerAuthority cardOwner);

    /**
     * Find all by customer role def id list.
     *
     * @param cardOwnerId the customer auth id
     * @return the list
     */
    List<PaymentCard> findAllByCardOwnerWithId(Long cardOwnerId);

}
