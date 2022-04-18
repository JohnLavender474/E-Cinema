package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ScreeningSeat extends AbstractEntity {

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ShowroomSeat showroomSeat;

    @OneToOne(cascade = CascadeType.ALL)
    private Ticket ticket;

}
