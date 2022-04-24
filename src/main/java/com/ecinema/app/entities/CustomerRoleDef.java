package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
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
 * A customer role def may optionally be blocked by a {@link ModeratorRoleDef} defined in {@link #censoredBy}
 * which means that the privileges of this instance defined by {@link UserRole#getAuthority()} are blocked
 * until the moderator has unblocked this instance.
 */
@Getter
@Setter
@Entity
public class CustomerRoleDef extends UserRoleDef {

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "customerRoleDef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(mappedBy = "customerRoleDef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentCard> paymentCards = new HashSet<>();

    @OneToMany(mappedBy = "customerRoleDef", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Coupon> coupons = new HashSet<>();

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ModeratorRoleDef censoredBy;

    @Override
    protected UserRole defineUserRole() {
        return UserRole.CUSTOMER;
    }

}
