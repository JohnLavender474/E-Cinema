package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;
import com.ecinema.app.utils.converters.DurationConverter;
import com.ecinema.app.utils.objects.Duration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Movie.
 */
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
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column
    private LocalDate releaseDate;

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

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}
