package com.ecinema.app.utils.validators;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class EmailValidator implements AbstractValidator<String> {

    private static final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\" +
            ".[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void validate(String email, List<String> errors) {
        boolean valid = Pattern.compile(regexPattern).matcher(email).matches();
        if (!valid) {
            errors.add("Email fails regex pattern test");
        }
    }

}
