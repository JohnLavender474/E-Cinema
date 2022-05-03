package com.ecinema.app.validators;

import com.ecinema.app.domain.forms.ScreeningForm;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScreeningFormValidator implements AbstractValidator<ScreeningForm> {
    
    @Override
    public void validate(ScreeningForm screeningForm, List<String> errors) {
        if (screeningForm.getShowtimeYear() < LocalDate.now().getYear()) {
            errors.add("Showtime year cannot be before current year");
        }
        if (screeningForm.getShowtimeMonth() == null) {
            errors.add("Showtime month cannot be null");
        }
        if (screeningForm.getShowtimeDay() == null) {
            errors.add("Showtime day cannot be null");
        } else if (screeningForm.getShowtimeDay() > screeningForm.getShowtimeMonth().maxLength()) {
            errors.add("Showtime day cannot exceed max day value of month");
        }
        if (screeningForm.getShowtimeHour() == null) {
            errors.add("Showtime error cannot be null");
        } else if (screeningForm.getShowtimeHour() > 23 || screeningForm.getShowtimeHour() < 0) {
            errors.add("Showtime hour must be between 0 and 23");
        }
        if (screeningForm.getShowtimeMinute() == null) {
            errors.add("Showtime must cannot be null");
        } else if (screeningForm.getShowtimeMinute() > 59 || screeningForm.getShowtimeMinute() < 0) {
            errors.add("Showtime minute must be between 0 and 59");
        }
    }
    
}
