package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Theater extends AbstractEntity {

    @Column
    private Integer theaterNumber;

    @Column
    private String theaterName;

    @ManyToMany(mappedBy = "theatersBeingManaged")
    private Set<AdminRoleDef> admins = new HashSet<>();

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Showroom> showrooms = new HashSet<>();

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

}
