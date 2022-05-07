package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.domain.validators.PaymentCardValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.services.PaymentCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PaymentCardServiceImpl extends AbstractServiceImpl<PaymentCard,
        PaymentCardRepository> implements PaymentCardService {

    private final CustomerRoleDefRepository customerRoleDefRepository;
    private final PaymentCardValidator paymentCardValidator;

    public PaymentCardServiceImpl(PaymentCardRepository repository,
                                  CustomerRoleDefRepository customerRoleDefRepository,
                                  PaymentCardValidator paymentCardValidator) {
        super(repository);
        this.customerRoleDefRepository = customerRoleDefRepository;
        this.paymentCardValidator = paymentCardValidator;
    }

    @Override
    protected void onDelete(PaymentCard paymentCard) {
        // detach Customer
        CustomerRoleDef customerRoleDef = paymentCard.getCustomerRoleDef();
        if (customerRoleDef != null) {
            customerRoleDef.getPaymentCards().remove(paymentCard);
            paymentCard.setCustomerRoleDef(null);
        }
    }

    @Override
    public void submitPaymentCardForm(PaymentCardForm paymentCardForm)
            throws NoEntityFoundException, InvalidArgsException {
        CustomerRoleDef customerRoleDef = customerRoleDefRepository.findById(
                paymentCardForm.getCustomerRoleDefId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "id", paymentCardForm.getCustomerRoleDefId()));
        List<String> errors = new ArrayList<>();
        paymentCardValidator.validate(paymentCardForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getPaymentCards().add(paymentCard);
        paymentCard.setBillingAddress(paymentCardForm.getBillingAddress());
        paymentCard.setPaymentCardType(paymentCardForm.getPaymentCardType());
        paymentCard.setCardNumber(paymentCardForm.getCardNumber());
        paymentCard.setFirstName(paymentCardForm.getFirstName());
        paymentCard.setLastName(paymentCardForm.getLastName());
        paymentCard.setExpirationDate(paymentCardForm.getExpirationDate());
        save(paymentCard);
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
