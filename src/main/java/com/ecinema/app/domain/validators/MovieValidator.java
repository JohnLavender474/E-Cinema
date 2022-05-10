package com.ecinema.app.domain.validators;

import com.ecinema.app.domain.contracts.IMovie;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * The type Movie validator.
 */
@Component
public class MovieValidator implements AbstractValidator<IMovie> {

    @Override
    public void validate(IMovie iMovie, Collection<String> errors) {
        if (iMovie.getTitle().isBlank()) {
            errors.add("title cannot be blank");
        }
        if (iMovie.getDirector().isBlank()) {
            errors.add("director cannot be blank");
        }
        if (iMovie.getImage().isBlank()) {
            errors.add("image cannot be blank");
        }
        if (iMovie.getTrailer().isBlank()) {
            errors.add("trailer cannot be blank");
        }
        if (iMovie.getSynopsis().isBlank()) {
            errors.add("synopsis cannot be blank");
        }
        if (iMovie.getHours() < 0) {
            errors.add("hours cannot be less than 0");
        }
        if (iMovie.getMinutes() <= 0 && iMovie.getHours() <= 0) {
            errors.add("minutes cannot be less than or equal to 0 when hours is less than or equal to 0");
        }
        if (iMovie.getReleaseYear() < 1888) {
            errors.add("the earliest surviving movie is Roundhay Garden Scene from 1888, year must be at least 1888");
        }
        if (iMovie.getReleaseDay() < 1) {
            errors.add("day must be greater than 0");
        }
        if (iMovie.getReleaseMonth() == null) {
            errors.add("month cannot be null");
        } else if (iMovie.getReleaseMonth().maxLength() < iMovie.getReleaseDay()) {
            errors.add("release day cannot exceed max length of month");
        }
        if (iMovie.getMsrbRating() == null) {
            errors.add("msrb rating cannot be null");
        }
        if (iMovie.getCast().isEmpty()) {
            errors.add("cast cannot be empty");
        }
        if (iMovie.getWriters().isEmpty()) {
            errors.add("writers cannot be empty");
        }
        if (iMovie.getMovieCategories().isEmpty()) {
            errors.add("movie categories cannot be empty");
        }
    }

}
