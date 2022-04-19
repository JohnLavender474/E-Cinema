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

    @ManyToMany(mappedBy = "theaters")
    private Set<AdminRoleDef> admins = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Showroom> showrooms = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Screening> screenings = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

}
