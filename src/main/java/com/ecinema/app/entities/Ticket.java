package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.TicketStatus;
import com.ecinema.app.utils.constants.TicketType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Ticket extends AbstractEntity {

    @Column
    private LocalDateTime createDateTime;

    @Column
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Column
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @ManyToOne
    @JoinColumn
    private CustomerAuthority customer;

    @ManyToOne
    @JoinColumn
    private PaymentCard paymentCard;

    @ManyToOne
    @JoinColumn
    private Screening screening;

    @ManyToOne
    @JoinColumn
    private ScreeningSeat screeningSeat;

}
