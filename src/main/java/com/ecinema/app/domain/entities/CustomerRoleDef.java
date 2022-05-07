package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 * The customer role def class defines the permissions, fields, and relationships pertaining to the customer role.
 * {@link Review} instances owned by this instance are mapped to {@link #reviews}.
 * {@link Ticket} instances owned by this instance are mapped to {@link #tickets}.
 * {@link PaymentCard} instances owned by this instance are mapped to {@link #paymentCards}.
 */
@Getter
@Setter
@Entity
public class CustomerRoleDef extends UserRoleDef {

    @OneToMany(mappedBy = "writer", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "customerRoleDef", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(mappedBy = "customerRoleDef", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<PaymentCard> paymentCards = new HashSet<>();

    @OneToMany(mappedBy = "customerRoleDef", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Coupon> coupons = new HashSet<>();

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ModeratorRoleDef censoredBy;

    @Override
    protected UserRole defineUserRole() {
        return UserRole.CUSTOMER;
    }

}
