package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.MovieCategory;
import com.ecinema.app.domain.enums.MsrbRating;

import java.time.Month;
import java.util.Collection;

public interface IMovie {
    String getTitle();
    String getDirector();
    String getImage();
    String getTrailer();
    String getSynopsis();
    Integer getHours();
    Integer getMinutes();
    Integer getReleaseYear();
    Integer getReleaseDay();
    Month getReleaseMonth();
    MsrbRating getMsrbRating();
    <T extends Collection<String>> T getCast();
    <T extends Collection<String>> T getWriters();
    <T extends Collection<MovieCategory>> T getMovieCategories();
}
