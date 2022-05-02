package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.Letter;
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
public class Showroom extends AbstractEntity {

    @Column
    private Letter showroomLetter;

    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShowroomSeat> showroomSeats = new HashSet<>();

    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}

