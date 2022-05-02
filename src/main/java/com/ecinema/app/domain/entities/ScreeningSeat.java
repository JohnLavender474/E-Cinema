package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.ISeat;
import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
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
        return showroomSeat.getRowLetter();
    }

    @Override
    public Integer getSeatNumber() {
        return showroomSeat.getSeatNumber();
    }

}
