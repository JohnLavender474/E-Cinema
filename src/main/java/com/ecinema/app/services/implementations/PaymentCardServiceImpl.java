package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.services.PaymentCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentCardServiceImpl extends AbstractServiceImpl<PaymentCard,
        PaymentCardRepository> implements PaymentCardService {

    public PaymentCardServiceImpl(PaymentCardRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(PaymentCard paymentCard) {

    }

    @Override
    public List<PaymentCard> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef) {
        return repository.findAllByCustomerRoleDef(customerRoleDef);
    }

    @Override
    public List<PaymentCard> findAllByCustomerRoleDefWithId(Long customerAuthId) {
        return repository.findAllByCustomerRoleDefWithId(customerAuthId);
    }

}
