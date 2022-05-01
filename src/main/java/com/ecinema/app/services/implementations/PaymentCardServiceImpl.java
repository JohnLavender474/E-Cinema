package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.Address;
import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.services.AddressService;
import com.ecinema.app.services.PaymentCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentCardServiceImpl extends AbstractServiceImpl<PaymentCard,
        PaymentCardRepository> implements PaymentCardService {

    private final AddressService addressService;

    public PaymentCardServiceImpl(PaymentCardRepository repository, AddressService addressService) {
        super(repository);
        this.addressService = addressService;
    }

    @Override
    protected void onDelete(PaymentCard paymentCard) {
        // detach Customer
        CustomerRoleDef customerRoleDef = paymentCard.getCustomerRoleDef();
        if (customerRoleDef != null) {
            customerRoleDef.getPaymentCards().remove(paymentCard);
            paymentCard.setCustomerRoleDef(null);
        }
        // cascade delete Address
        Address address = paymentCard.getBillingAddress();
        if (address != null) {
            paymentCard.setBillingAddress(null);
            addressService.delete(address);
        }
    }

    @Override
    public List<PaymentCard> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef) {
        return repository.findDistinctByCustomerRoleDef(customerRoleDef);
    }

    @Override
    public List<PaymentCard> findAllByCustomerRoleDefId(Long customerAuthId) {
        return repository.findDistinctByCustomerRoleDefWithId(customerAuthId);
    }

}
