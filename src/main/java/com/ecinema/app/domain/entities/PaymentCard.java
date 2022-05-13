package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.contracts.IAddress;
import com.ecinema.app.domain.contracts.IPaymentCard;
import com.ecinema.app.domain.enums.PaymentCardType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
public class PaymentCard extends AbstractEntity implements IPaymentCard {

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentCardType paymentCardType;

    @Column
    private String cardNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate expirationDate;

    @JoinColumn
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerAuthority cardOwner;

    @Embedded
    @ToString.Exclude
    private Address billingAddress = new Address();

    @Override
    public void setBillingAddress(IAddress billingAddress) {
        this.billingAddress.setCity(billingAddress.getCity());
        this.billingAddress.setStreet(billingAddress.getStreet());
        this.billingAddress.setUsState(billingAddress.getUsState());
        this.billingAddress.setZipcode(billingAddress.getZipcode());
    }

}
