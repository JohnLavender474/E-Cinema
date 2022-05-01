package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.TicketStatus;
import com.ecinema.app.utils.TicketType;
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ScreeningSeat screeningSeat;

}
