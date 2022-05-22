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
public class Customer extends AbstractUserAuthority {

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
    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ReviewVote> reviewVotes = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ReviewReport> reviewReports = new HashSet<>();

    @JoinColumn
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Moderator censoredBy = null;

    @Override
    protected UserAuthority defineUserRole() {
        return UserAuthority.CUSTOMER;
    }

}
