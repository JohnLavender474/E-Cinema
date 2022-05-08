package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.enums.TicketType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
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
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerRoleDef customerRoleDef;

    @JoinColumn
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private ScreeningSeat screeningSeat;

    @ToString.Exclude
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Coupon> coupons = new HashSet<>();

}
