package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.contracts.IShowroom;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Showroom.
 */
@Getter
@Setter
@Entity
@ToString
public class Showroom extends AbstractEntity implements IShowroom {

    @Column
    private Letter showroomLetter;

    @Column
    private Integer numberOfRows;

    @Column
    private Integer numberOfSeatsPerRow;

    @ToString.Exclude
    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShowroomSeat> showroomSeats = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}

