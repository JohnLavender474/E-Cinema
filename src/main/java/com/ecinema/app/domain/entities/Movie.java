package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import com.ecinema.app.utils.DurationConverter;
import com.ecinema.app.utils.Duration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

/**
 * The type Movie.
 */
@Getter
@Setter
@Entity
public class Movie extends AbstractEntity {

    @Column
    private String title;

    @Column
    private String director;

    @Column
    private String image;

    @Column
    private String trailer;

    @Column(length = 2000)
    private String synopsis;

    @Column
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column
    private LocalDate releaseDate;

    @Column
    @Enumerated(EnumType.STRING)
    private MsrbRating msrbRating;

    @ElementCollection
    private Set<String> cast = new HashSet<>();

    @ElementCollection
    private Set<String> writers = new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<MovieCategory> movieCategories = EnumSet.noneOf(MovieCategory.class);

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Screening> screenings = new HashSet<>();

}
