package com.ecinema.app.domain.entities;

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
    @ManyToOne(fetch = FetchType.EAGER)
    private ShowroomSeat showroomSeat;

    @OneToOne(mappedBy = "screeningSeat")
    private Ticket ticket;

}
