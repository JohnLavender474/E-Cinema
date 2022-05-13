package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.domain.validators.PaymentCardValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerAuthorityRepository;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.services.PaymentCardService;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class PaymentCardServiceImpl extends AbstractServiceImpl<PaymentCard,
        PaymentCardRepository> implements PaymentCardService {

    private final CustomerAuthorityRepository customerAuthorityRepository;
    private final PaymentCardValidator paymentCardValidator;

    public PaymentCardServiceImpl(PaymentCardRepository repository,
                                  CustomerAuthorityRepository customerAuthorityRepository,
                                  PaymentCardValidator paymentCardValidator) {
        super(repository);
        this.customerAuthorityRepository = customerAuthorityRepository;
        this.paymentCardValidator = paymentCardValidator;
    }

    @Override
    protected void onDelete(PaymentCard paymentCard) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Payment card on delete");
        // detach Customer
        CustomerAuthority customerAuthority = paymentCard.getCardOwner();
        logger.debug("Detach customer role def: " + customerAuthority);
        if (customerAuthority != null) {
            customerAuthority.getPaymentCards().remove(paymentCard);
            paymentCard.setCardOwner(null);
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        PaymentCard paymentCard = findById(id).orElseThrow(
                () -> new NoEntityFoundException("payment card", "id", id));
        onDeleteInfo(paymentCard, info);
    }

    @Override
    public void onDeleteInfo(PaymentCard paymentCard, Collection<String> info) {
        String username = paymentCard.getCardOwner().getUser().getUsername();
        info.add(username + " will lose " + paymentCard.getPaymentCardType() + " on delete");
    }

    @Override
    public void submitPaymentCardForm(PaymentCardForm paymentCardForm)
            throws NoEntityFoundException, InvalidArgsException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit payment card form");
        CustomerAuthority customerAuthority = customerAuthorityRepository.findById(
                paymentCardForm.getCustomerRoleDefId()).orElseThrow(
                        () -> new NoEntityFoundException(
                        "customer role def", "id", paymentCardForm.getCustomerRoleDefId()));
        logger.debug("Found customer role def by id " + paymentCardForm.getCustomerRoleDefId());
        List<String> errors = new ArrayList<>();
        paymentCardValidator.validate(paymentCardForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        logger.debug("Payment card form passed validation checks");
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardOwner(customerAuthority);
        customerAuthority.getPaymentCards().add(paymentCard);
        paymentCard.setBillingAddress(paymentCardForm.getBillingAddress());
        paymentCard.setPaymentCardType(paymentCardForm.getPaymentCardType());
        paymentCard.setCardNumber(paymentCardForm.getCardNumber());
        paymentCard.setFirstName(paymentCardForm.getFirstName());
        paymentCard.setLastName(paymentCardForm.getLastName());
        paymentCard.setExpirationDate(paymentCardForm.getExpirationDate());
        save(paymentCard);
        logger.debug("Instantiated and saved new payment card: " + paymentCard);
        logger.debug("Payment card form: " + paymentCardForm);
    }

    @Override
    public List<PaymentCard> findAllByCardOwner(CustomerAuthority customerAuthority) {
        return repository.findDistinctByCardOwner(customerAuthority);
    }

    @Override
    public List<PaymentCard> findAllByCardOwnerWithId(Long customerAuthId) {
        return repository.findDistinctByCardOwnerWithId(customerAuthId);
    }

}
