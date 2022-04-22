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
    private LocalDateTime creationDateTime;

    @Column
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Column
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerRoleDef customerRoleDef;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentCard paymentCard;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ScreeningSeat screeningSeat;

}
