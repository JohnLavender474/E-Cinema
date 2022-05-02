package com.ecinema.app.validators;

import com.ecinema.app.domain.forms.MovieForm;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Movie validator.
 */
@Component
public class MovieValidator implements AbstractValidator<MovieForm> {

    @Override
    public void validate(MovieForm movieForm, List<String> errors) {
        if (movieForm.getTitle().isBlank()) {
            errors.add("title cannot be blank");
        }
        if (movieForm.getDirector().isBlank()) {
            errors.add("director cannot be blank");
        }
        if (movieForm.getImage().isBlank()) {
            errors.add("image cannot be blank");
        }
        if (movieForm.getTrailer().isBlank()) {
            errors.add("trailer cannot be blank");
        }
        if (movieForm.getSynopsis().isBlank()) {
            errors.add("synopsis cannot be blank");
        }
        if (movieForm.getHours() < 0) {
            errors.add("hours cannot be less than 0");
        }
        if (movieForm.getMinutes() <= 0 && movieForm.getHours() <= 0) {
            errors.add("minutes cannot be less than or equal to 0 when hours is less than or equal to 0");
        }
        if (movieForm.getReleaseYear() < 1888) {
            errors.add("the earliest surviving movie is Roundhay Garden Scene from 1888, year must be at least 1888");
        }
        if (movieForm.getReleaseDay() < 1) {
            errors.add("day must be greater than 0");
        }
        if (movieForm.getReleaseMonth() == null) {
            errors.add("month cannot be null");
        } else if (movieForm.getReleaseMonth().maxLength() < movieForm.getReleaseDay()) {
            errors.add("release day cannot exceed max length of month");
        }
        if (movieForm.getMsrbRating() == null) {
            errors.add("msrb rating cannot be null");
        }
        if (movieForm.getCast().isEmpty()) {
            errors.add("cast cannot be empty");
        }
        if (movieForm.getWriters().isEmpty()) {
            errors.add("writers cannot be empty");
        }
        if (movieForm.getMovieCategories().isEmpty()) {
            errors.add("movie categories cannot be empty");
        }
    }

}
