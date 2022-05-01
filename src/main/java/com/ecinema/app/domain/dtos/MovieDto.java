package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import com.ecinema.app.utils.Duration;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

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
    private List<ReviewDto> reviewDtos = new ArrayList<>();
    private List<ScreeningDto> screeningDtos = new ArrayList<>();

}
