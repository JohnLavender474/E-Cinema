package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private Set<AdminAuthority> admins;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Showroom> showrooms;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Screening> screenings;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

}
