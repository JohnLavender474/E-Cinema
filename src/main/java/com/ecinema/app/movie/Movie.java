package com.ecinema.app.movie;

import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.review.Review;
import com.ecinema.app.screening.Screening;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Movie extends AbstractEntity {

    @Column
    private String title;

    @ElementCollection
    private List<String> directors;

    @ElementCollection
    private List<String> producers;

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
    private List<MovieCategory> movieCategories;

    @ElementCollection
    private List<String> cast;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Screening> screenings;

}
