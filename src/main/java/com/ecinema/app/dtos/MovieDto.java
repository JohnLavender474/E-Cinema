package com.ecinema.app.dtos;

import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import com.ecinema.app.utils.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class MovieDto implements Serializable {
    private Long id;
    private String title;
    private String director;
    private String image;
    private String trailer;
    private String synopsis;
    private Duration duration;
    private LocalDate releaseDate;
    private MsrbRating msrbRating;
    private Set<String> cast = new HashSet<>();
    private Set<String> writers = new HashSet<>();
    private Set<String> producers = new HashSet<>();
    private Set<MovieCategory> movieCategories =
            EnumSet.noneOf(MovieCategory.class);
    private List<ReviewDto> reviewDtos = new ArrayList<>();
    private List<ScreeningDto> screeningDtos = new ArrayList<>();
}
