package com.ecinema.app.validators;

import com.ecinema.app.domain.forms.ShowroomForm;
import com.ecinema.app.utils.Letter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShowroomFormValidator implements AbstractValidator<ShowroomForm> {

    @Override
    public void validate(ShowroomForm showroomForm, List<String> errors) {
        if (showroomForm.getShowroomLetter() == null) {
            errors.add("showroom letter cannot be null");
        }
        if (showroomForm.getNumberOfRows() <= 0) {
            errors.add("number of rows cannot be zero");
        }
        if (showroomForm.getNumberOfRows() > Letter.values().length) {
            errors.add("number of rows cannot exceed 26");
        }
        if (showroomForm.getNumberOfSeatsPerRow() <= 0) {
            errors.add("number of seats per row cannot be zero");
        }
        if (showroomForm.getNumberOfSeatsPerRow() > 50) {
            errors.add("number of seats per row cannot exceed 50");
        }
    }

}
