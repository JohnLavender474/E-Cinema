package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.Duration;
import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import lombok.Getter;
import lombok.Setter;

import java.time.Month;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Movie dto.
 */
@Getter
@Setter
public class MovieDto implements AbstractDto {
    private Long id;
    private String title;
    private String director;
    private String image;
    private String trailer;
    private String synopsis;
    private Duration duration;
    private Integer releaseYear;
    private Integer releaseDay;
    private Month releaseMonth;
    private MsrbRating msrbRating;
    private Integer averageRating;
    private Set<String> cast = new HashSet<>();
    private Set<String> writers = new HashSet<>();
    private Set<MovieCategory> movieCategories =
            EnumSet.noneOf(MovieCategory.class);
}
