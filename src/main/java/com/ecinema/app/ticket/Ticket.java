package com.ecinema.app.ticket;

import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.payment.PaymentCard;
import com.ecinema.app.screening.Screening;
import com.ecinema.app.seat.Seat;
import com.ecinema.app.user.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Ticket extends AbstractEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Column
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentCard paymentCard;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Seat seat;

}
