package com.ecinema.app.user;

import com.ecinema.app.coupon.Coupon;
import com.ecinema.app.payment.PaymentCard;
import com.ecinema.app.review.Review;
import com.ecinema.app.ticket.Ticket;
import com.ecinema.app.security.AppUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Customer extends AppUser {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentCard> paymentCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Coupon> coupons;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @Column
    private Integer credits;

    @Column
    private Boolean isRewardsMember;

    @Column
    private Boolean isReceivingNewsLetters;

}
