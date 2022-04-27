package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.Letter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The type Theater.
 */
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

    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(mappedBy = "theater", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Map<Letter, Showroom> showrooms = new EnumMap<>(Letter.class);

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

}
