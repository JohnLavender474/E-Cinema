package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserAuthority;
import lombok.*;

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
@ToString
@NoArgsConstructor
public class CustomerAuthority extends AbstractUserAuthority {

    @ToString.Exclude
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "ticketOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "cardOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentCard> paymentCards = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "couponOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Coupon> coupons = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<Review> reportedReviews = new HashSet<>();

    @JoinColumn
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private ModeratorAuthority censoredBy;

    @Override
    protected UserAuthority defineUserRole() {
        return UserAuthority.CUSTOMER;
    }

}
