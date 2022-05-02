package com.ecinema.app.validators;

import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ecinema.app.utils.UsernameCriteria.MAX_LENGTH;
import static com.ecinema.app.utils.UsernameCriteria.MIN_LENGTH;

@Component
public class UsernameValidator implements AbstractValidator<String> {

    @Override
    public void validate(String username, List<String> errors) {
        if (!UtilMethods.isAlphaAndDigitsOnly(username)) {
            errors.add("Username must contain only letters and numbers");
        }
        if (username.length() < MIN_LENGTH || username.length() > MAX_LENGTH) {
            errors.add("Username must be between 6 and 64 characters long.");
        }
    }

}