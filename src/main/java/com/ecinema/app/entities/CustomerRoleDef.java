package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class CustomerRoleDef extends UserRoleDef {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<PaymentCard> paymentCards = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.CUSTOMER;
    }

}
