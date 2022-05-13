package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.contracts.ISeat;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
public class ScreeningSeat extends AbstractEntity implements ISeat {

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private ShowroomSeat showroomSeat;

    @OneToOne(mappedBy = "screeningSeat")
    private Ticket ticket;

    @Override
    public Letter getRowLetter() {
        return showroomSeat != null ? showroomSeat.getRowLetter() : null;
    }

    @Override
    public Integer getSeatNumber() {
        return showroomSeat != null ? showroomSeat.getSeatNumber() : null;
    }

    public boolean isBooked() {
        return ticket != null;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ScreeningSeat screeningSeat &&
                showroomSeat.equals(screeningSeat.getShowroomSeat()) &&
                getRowLetter().equals(screeningSeat.getRowLetter()) &&
                getSeatNumber().equals(screeningSeat.getSeatNumber());
    }

}
