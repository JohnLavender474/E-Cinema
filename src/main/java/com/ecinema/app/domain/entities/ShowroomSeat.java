package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.contracts.ISeat;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ShowroomSeat extends AbstractEntity implements ISeat {

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Showroom showroom;

    @Column
    @Enumerated(EnumType.STRING)
    private Letter rowLetter;

    @Column
    private Integer seatNumber;

    @OneToMany(mappedBy = "showroomSeat", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<ScreeningSeat> screeningSeats = new HashSet<>();

}
