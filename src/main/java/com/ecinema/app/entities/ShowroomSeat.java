package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ShowroomSeat extends AbstractEntity {

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Showroom showroom;

    @Column
    @Enumerated(EnumType.STRING)
    private Letter rowLetter;

    @Column
    private Integer seatNumber;

}
