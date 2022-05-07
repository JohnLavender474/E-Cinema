package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IAddress;
import com.ecinema.app.domain.contracts.IPaymentCard;
import com.ecinema.app.domain.enums.PaymentCardType;
import com.ecinema.app.domain.enums.UsState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;

@Getter
@Setter
public class PaymentCardForm implements IPaymentCard, Serializable {

    private Long customerRoleDefId = 0L;
    private PaymentCardType paymentCardType = PaymentCardType.CREDIT;
    private String cardNumber = "1234123412341234";
    private String firstName = "John";
    private String lastName = "Doe";
    private Month expirationMonth = Month.JANUARY;
    private Integer expirationYear = 2020;
    private String street = "Street";
    private String city = "City";
    private UsState usState = UsState.GEORGIA;
    private String zipcode = "12345";

    @Override
    public LocalDate getExpirationDate() {
        return LocalDate.of(expirationYear, expirationMonth, 1);
    }

    @Override
    public void setExpirationDate(LocalDate expirationDate) {
        setExpirationYear(expirationDate.getYear());
        setExpirationMonth(expirationDate.getMonth());
    }

    @Override
    public IAddress getBillingAddress() {
        AddressForm addressForm = new AddressForm();
        addressForm.setCity(city);
        addressForm.setStreet(street);
        addressForm.setUsState(usState);
        addressForm.setZipcode(zipcode);
        return addressForm;
    }

    @Override
    public void setBillingAddress(IAddress billingAddress) {
        setStreet(billingAddress.getStreet());
        setCity(billingAddress.getCity());
        setUsState(billingAddress.getUsState());
        setZipcode(billingAddress.getZipcode());
    }

}
