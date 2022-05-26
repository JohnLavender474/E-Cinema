package com.ecinema.app.services;

import com.ecinema.app.TooManyPaymentCardsException;
import com.ecinema.app.domain.dtos.PaymentCardDto;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.PaymentCard;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.domain.validators.PaymentCardValidator;
import com.ecinema.app.exceptions.InvalidArgumentException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerRepository;
import com.ecinema.app.repositories.PaymentCardRepository;
import com.ecinema.app.util.UtilMethods;
import org.modelmapper.internal.asm.tree.ModuleExportNode;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentCardService extends AbstractEntityService<PaymentCard, PaymentCardRepository, PaymentCardDto> {

    public static final int MAX_PAYMENT_CARDS_PER_CUSTOMER = 5;

    private final EncoderService encoderService;
    private final CustomerRepository customerRepository;
    private final PaymentCardValidator paymentCardValidator;

    public PaymentCardService(PaymentCardRepository repository,
                              EncoderService encoderService,
                              CustomerRepository customerRepository,
                              PaymentCardValidator paymentCardValidator) {
        super(repository);
        this.encoderService = encoderService;
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
        paymentCardDto.setUserId(repository.findUserIdByPaymentCardWithId(
                paymentCard.getId()).orElseThrow(
                () -> new NoEntityFoundException("user id", "payment card id", paymentCard.getId())));
        paymentCardDto.setToIPaymentCard(paymentCard, false);
        paymentCardDto.setCardNumber(paymentCard.getLast4Digits());
        return paymentCardDto;
    }

    public PaymentCardForm fetchAsForm(Long paymentCardId)
            throws NoEntityFoundException {
        PaymentCard paymentCard = repository.findById(paymentCardId).orElseThrow(
                () -> new NoEntityFoundException("payment card", "id", paymentCardId));
        PaymentCardForm paymentCardForm = new PaymentCardForm();
        paymentCardForm.setToIPaymentCard(paymentCard, false);
        return paymentCardForm;
    }

    public void submitPaymentCardForm(PaymentCardForm paymentCardForm)
            throws NoEntityFoundException, TooManyPaymentCardsException, InvalidArgumentException {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Payment card: " + paymentCardForm);
        List<String> errors = new ArrayList<>();
        paymentCardValidator.validate(paymentCardForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgumentException(errors);
        }
        logger.debug("Payment card form passed validation checks");
        if (paymentCardForm.getPaymentCardId() == null) {
            submitPaymentCardFormToAddNewPaymentCard(paymentCardForm);
        } else {
            submitPaymentCardFormToEditPaymentCard(paymentCardForm);
        }
    }

    private void submitPaymentCardFormToAddNewPaymentCard(PaymentCardForm paymentCardForm) {
        logger.debug("Submit payment card form to add new payment card");
        Customer customer = customerRepository.findByUserWithId(paymentCardForm.getUserId()).orElseThrow(
                () -> new NoEntityFoundException(
                        "customer authority", "user id", paymentCardForm.getUserId()));
        logger.debug("Found customer authority by user id " + paymentCardForm.getUserId());
        if (customer.getPaymentCards().size() >= MAX_PAYMENT_CARDS_PER_CUSTOMER) {
            throw new TooManyPaymentCardsException(customer.getPaymentCards().size());
        }
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardOwner(customer);
        customer.getPaymentCards().add(paymentCard);
        paymentCard.setToIPaymentCard(paymentCardForm, false);
        String cardNumber = paymentCardForm.getCardNumber();
        paymentCard.setCardNumber(encoderService.encode(cardNumber));
        paymentCard.setLast4Digits(cardNumber.substring(cardNumber.length() - 4));
        save(paymentCard);
        logger.debug("Instantiated and saved new payment card: " + paymentCard);
    }

    private void submitPaymentCardFormToEditPaymentCard(PaymentCardForm paymentCardForm) {
        PaymentCard paymentCard = repository.findById(paymentCardForm.getPaymentCardId()).orElseThrow(
                () -> new NoEntityFoundException(
                        "payment card", "id", paymentCardForm.getPaymentCardId()));
        paymentCard.setToIPaymentCard(paymentCardForm, false);
        String cardNumber = paymentCardForm.getCardNumber();
        paymentCard.setCardNumber(encoderService.encode(cardNumber));
        paymentCard.setLast4Digits(cardNumber.substring(cardNumber.length() - 4));
        save(paymentCard);
        logger.debug("Edited payment card: " + paymentCard);
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
