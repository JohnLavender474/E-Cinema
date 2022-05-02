package com.ecinema.app.validators;

import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ecinema.app.utils.PasswordCriteria.MIN_LENGTH;
import static com.ecinema.app.utils.PasswordCriteria.MIN_SPECIAL_CHARS;

@Component
public class PasswordValidator implements AbstractValidator<String> {

    @Override
    public void validate(String s, List<String> errors) {
        if (s.length() < MIN_LENGTH) {
            errors.add("Password length must be at least " + MIN_LENGTH);
        }
        if (UtilMethods.numSpecialChars(s) < MIN_SPECIAL_CHARS) {
            errors.add("Password must contain at least " + MIN_SPECIAL_CHARS + " special chars");
        }
    }

}
