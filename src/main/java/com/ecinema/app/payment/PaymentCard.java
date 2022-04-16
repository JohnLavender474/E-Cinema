package com.ecinema.app.payment;

import com.ecinema.app.address.Address;
import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.user.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Month;

@Getter
@Setter
@Entity
public class PaymentCard extends AbstractEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentCardType paymentCardType;

    @Column
    private Integer cardNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Enumerated(EnumType.STRING)
    private Month expirationMonth;

    @Column
    private Integer expirationYear;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address billingAddress;

}
