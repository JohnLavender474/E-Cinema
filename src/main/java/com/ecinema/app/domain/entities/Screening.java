package com.ecinema.app.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Screening.
 */
@Getter
@Setter
@Entity
public class Screening extends AbstractEntity {

    @Column
    private LocalDateTime showDateTime;

    @Column
    private LocalDateTime endDateTime;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Showroom showroom;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<ScreeningSeat> screeningSeats = new HashSet<>();

}
