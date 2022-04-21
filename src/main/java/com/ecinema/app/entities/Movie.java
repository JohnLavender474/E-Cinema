package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Movie extends AbstractEntity {

    @Column
    private String title;

    @ElementCollection
    private Set<String> directors = new HashSet<>();

    @ElementCollection
    private Set<String> producers = new HashSet<>();

    @Column
    private String synopsis;

    @Column
    private Integer durationInMinutes;

    @Column
    private String image;

    @Column
    private String trailer;

    @Column
    @Enumerated(EnumType.STRING)
    private MsrbRating msrbRating;

    @ElementCollection
    private Set<String> cast = new HashSet<>();

    @ElementCollection
    private Set<MovieCategory> movieCategories = EnumSet.noneOf(MovieCategory.class);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}
