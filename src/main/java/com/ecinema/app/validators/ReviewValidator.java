package com.ecinema.app.validators;

import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewValidator implements AbstractValidator<ReviewForm> {

    public static final Integer MIN_NON_BLANK_CHARS = 25;
    public static final Integer MAX_CHARS = 2000;
    public static final Integer MAX_RATING = 10;
    public static final Integer MIN_RATING = 0;

    @Override
    public void validate(ReviewForm reviewForm, List<String> errors) {
        if (reviewForm.getReview().isBlank()) {
            errors.add("review cannot be blank");
        }
        if (UtilMethods.removeWhitespace(reviewForm.getReview()).length() < MIN_NON_BLANK_CHARS) {
            errors.add("review must have at least 25 non-blank chars");
        }
        if (reviewForm.getReview().length() > MAX_CHARS) {
            errors.add("review cannot exceed 2000 chars");
        }
        if (reviewForm.getRating() > MAX_RATING) {
            errors.add("rating cannot exceed 10 stars");
        }
        if (reviewForm.getRating() < MIN_RATING) {
            errors.add("rating cannot be less than 0 stars");
        }
    }

}
