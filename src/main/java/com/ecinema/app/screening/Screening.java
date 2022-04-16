package com.ecinema.app.screening;

import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.showroom.Showroom;
import com.ecinema.app.ticket.Ticket;
import com.ecinema.app.movie.Movie;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Screening extends AbstractEntity {

    @Column
    private LocalDateTime showtime;

    @ManyToOne
    @JoinColumn
    private Movie movie;

    @ManyToOne
    @JoinColumn
    private Showroom showroom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

}
