package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.PaymentCardType;

import java.time.LocalDate;

public interface IPaymentCard {
    PaymentCardType getPaymentCardType();
    void setPaymentCardType(PaymentCardType paymentCardType);
    String getCardNumber();
    void setCardNumber(String cardNumber);
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    LocalDate getExpirationDate();
    void setExpirationDate(LocalDate expirationDate);
    IAddress getBillingAddress();
    void setBillingAddress(IAddress billingAddress);
}
