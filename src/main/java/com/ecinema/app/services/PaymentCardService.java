package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.PaymentCardDto;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.domain.validators.PaymentCardValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerRepository;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.util.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentCardService extends AbstractEntityService<PaymentCard, PaymentCardRepository, PaymentCardDto> {

    private final CustomerRepository customerRepository;
    private final PaymentCardValidator paymentCardValidator;

    public PaymentCardService(PaymentCardRepository repository,
                              CustomerRepository customerRepository,
                              PaymentCardValidator paymentCardValidator) {
        super(repository);
        this.customerRepository = customerRepository;
        this.paymentCardValidator = paymentCardValidator;
    }

    @Override
    protected void onDelete(PaymentCard paymentCard) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Payment card on delete");
        // detach Customer
        Customer customer = paymentCard.getCardOwner();
        logger.debug("Detach customer role def: " + customer);
        if (customer != null) {
            customer.getPaymentCards().remove(paymentCard);
            paymentCard.setCardOwner(null);
        }
    }

    @Override
    public PaymentCardDto convertToDto(PaymentCard paymentCard) {
        PaymentCardDto paymentCardDto = new PaymentCardDto();
        paymentCardDto.setId(paymentCard.getId());
        paymentCardDto.setCardOwnerId(paymentCard.getCardOwner().getId());
        paymentCardDto.setFirstName(paymentCard.getFirstName());
        paymentCardDto.setLastName(paymentCard.getLastName());
        paymentCardDto.setExpirationDate(
                UtilMethods.localDateFormatted(
                        paymentCard.getExpirationDate()));
        return paymentCardDto;
    }

    public void submitPaymentCardForm(PaymentCardForm paymentCardForm)
            throws NoEntityFoundException, InvalidArgsException {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Submit payment card form");
        Customer customer = customerRepository.findById(
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
        paymentCard.setCardOwner(customer);
        customer.getPaymentCards().add(paymentCard);
        paymentCard.setBillingAddress(paymentCardForm.getBillingAddress());
        paymentCard.setPaymentCardType(paymentCardForm.getPaymentCardType());
        paymentCard.setCardNumber(paymentCardForm.getCardNumber());
        paymentCard.setFirstName(paymentCardForm.getFirstName());
        paymentCard.setLastName(paymentCardForm.getLastName());
        paymentCard.setExpirationDate(paymentCardForm.getExpirationDate());
        repository.save(paymentCard);
        logger.debug("Instantiated and saved new payment card: " + paymentCard);
        logger.debug("Payment card form: " + paymentCardForm);
    }

    public List<PaymentCardDto> findAllByCardCustomerWithId(Long customerId) {
        return repository.findDistinctByCardCustomerWithId(customerId)
                .stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentCardDto> findAllByCardUserWithId(Long userId) {
        return repository.findDistinctByCardUserWithId(userId)
                .stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    boolean isPaymentCardOwnedByUser(Long paymentCardId, Long userId) {
        return repository.isPaymentCardOwnedByUser(paymentCardId, userId);
    }

    boolean isPaymentCardOwnedByCustomer(Long paymentCardId, Long customerId) {
        return repository.isPaymentCardOwnedByCustomer(paymentCardId, customerId);
    }

}
