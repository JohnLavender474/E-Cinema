package com.ecinema.app.services;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.PaymentCard;

import java.util.List;

/**
 * The interface Payment card service.
 */
public interface PaymentCardService extends AbstractService<PaymentCard> {

    /**
     * Find all by customer role def list.
     *
     * @param customerRoleDef the customer role def
     * @return the list
     */
    List<PaymentCard> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef);

    /**
     * Find all by customer role def id list.
     *
     * @param customerAuthId the customer auth id
     * @return the list
     */
    List<PaymentCard> findAllByCustomerRoleDefId(Long customerAuthId);

}
