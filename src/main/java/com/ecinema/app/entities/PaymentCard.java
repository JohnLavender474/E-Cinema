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

    @ManyToOne
    @JoinColumn
    private CustomerRoleDef customerRoleDef;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address billingAddress;

}
