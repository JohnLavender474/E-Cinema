package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.PaymentCardType;
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

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerRoleDef customerRoleDef;

    @JoinColumn
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Address billingAddress;

}
