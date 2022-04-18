package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Screening extends AbstractEntity {

    @Column
    private LocalDateTime showDateTime;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theater;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Showroom showroom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

}
