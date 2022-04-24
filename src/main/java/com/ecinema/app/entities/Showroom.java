package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.swing.text.html.ObjectView;
import java.util.*;

@Getter
@Setter
@Entity
public class Showroom extends AbstractEntity {

    @Column
    private Letter showroomLetter;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theater;

    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShowroomSeat> showroomSeats = new HashSet<>();

    @OneToMany(mappedBy = "showroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}

