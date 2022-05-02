package com.ecinema.app.domain.forms;

import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MovieForm implements Serializable {
    private String title = "";
    private String director = "";
    private String image = "";
    private String trailer = "";
    private String synopsis = "";
    private Integer hours = 0;
    private Integer minutes = 0;
    private Integer releaseYear = 1888;
    private Integer releaseDay = 1;
    private Month releaseMonth = Month.JANUARY;
    private MsrbRating msrbRating = MsrbRating.G;
    private List<String> cast = new ArrayList<>();
    private List<String> writers = new ArrayList<>();
    private List<MovieCategory> movieCategories = new ArrayList<>();
}
