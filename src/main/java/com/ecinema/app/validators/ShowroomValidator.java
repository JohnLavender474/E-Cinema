package com.ecinema.app.validators;

import com.ecinema.app.domain.forms.ShowroomForm;
import com.ecinema.app.utils.Letter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShowroomValidator implements AbstractValidator<ShowroomForm> {

    @Override
    public void validate(ShowroomForm showroomForm, List<String> errors) {
        if (showroomForm.getShowroomLetter() == null) {
            errors.add("showroom letter cannot be null");
        }
        if (showroomForm.getSeatMap().isEmpty()) {
            errors.add("seat map cannot be empty");
        }
        for (Map.Entry<Letter, Integer> entry : showroomForm.getSeatMap().entrySet()) {
            if (entry.getValue() <= 0) {
                errors.add("number of seats in row " + entry.getKey() + " cannot be less than 1");
            }
        }
    }

}
