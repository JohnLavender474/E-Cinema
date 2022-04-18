package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Movie extends AbstractEntity {

    @Column
    private String title;

    @ElementCollection
    private Set<String> directors;

    @ElementCollection
    private Set<String> producers;

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
    private Set<String> cast;

    @ElementCollection
    private Set<MovieCategory> movieCategories;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Screening> screenings;

}
