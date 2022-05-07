package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.contracts.IShowroom;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Showroom.
 */
@Getter
@Setter
@Entity
public class Showroom extends AbstractEntity implements IShowroom {

    @Column
    private Letter showroomLetter;

    @Column
    private Integer numberOfRows;

    @Column
    private Integer numberOfSeatsPerRow;

    @OneToMany(mappedBy = "showroom", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<ShowroomSeat> showroomSeats = new HashSet<>();

    @OneToMany(mappedBy = "showroom", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}

