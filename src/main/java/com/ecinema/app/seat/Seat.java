package com.ecinema.app.seat;

import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.showroom.Showroom;
import com.ecinema.app.ticket.Ticket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Seat extends AbstractEntity {

    @ManyToOne
    @JoinColumn
    private Showroom showroom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

    @Column
    private Integer rowNumber;

    @Column
    private Integer seatNumber;

}
