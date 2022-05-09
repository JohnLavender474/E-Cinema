package com.ecinema.app.domain.validators;

import com.ecinema.app.domain.contracts.IScreening;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;

@Component
public class ScreeningValidator implements AbstractValidator<IScreening> {
    
    @Override
    public void validate(IScreening iScreening, Collection<String> errors) {
        if (iScreening.getShowtime().getYear() < LocalDate.now().getYear()) {
            errors.add("Showtime year cannot be before current year");
        }
        if (iScreening.getShowtime().getMonth() == null) {
            errors.add("Showtime month cannot be null");
        }
        if (iScreening.getShowtime().getDayOfMonth() > iScreening.getShowtime().getMonth().maxLength()) {
            errors.add("Showtime day cannot exceed max day value of month");
        }
    }
    
}
