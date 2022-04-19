package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;

import java.util.List;

public interface PaymentCardService extends AbstractService<PaymentCard> {
    List<PaymentCard> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef);
    List<PaymentCard> findAllByCustomerRoleDefWithId(Long customerAuthId);
}
