package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Showroom extends AbstractEntity {

    @Column
    private Letter showroomLetter;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theater;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShowroomSeat> showroomSeats = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}

