package com.ecinema.app.showroom;

import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.screening.Screening;
import com.ecinema.app.seat.Seat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Showroom extends AbstractEntity {

    @Column
    private Integer showroomNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Screening> screenings;

}

