package com.ecinema.app.utils.validators;

import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.forms.RegistrationForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrationFormValidator implements AbstractValidator<RegistrationForm> {

    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public RegistrationFormValidator(EmailValidator emailValidator, PasswordValidator passwordValidator) {
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void validate(RegistrationForm registrationForm, List<String> errors) {
        emailValidator.validate(registrationForm.getEmail(), errors);
        passwordValidator.validate(registrationForm.getPassword(), errors);
        if (!registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
            errors.add("Password does not match password confirmation");
        }
        if (registrationForm.getFirstName().isBlank()) {
            errors.add("First name is blank");
        } else if (!UtilMethods.isAlphabeticalOnly(registrationForm.getFirstName())) {
            errors.add("First name must contain only alphabetical characters");
        }
        if (registrationForm.getLastName().isBlank()) {
            errors.add("Last name is blank");
        } else if (!UtilMethods.isAlphabeticalOnly(registrationForm.getLastName())) {
            errors.add("Last name must contain only alphabetical characters");
        }
        if (registrationForm.getSecurityQuestion1().isBlank() ||
                registrationForm.getSecurityAnswer1().isBlank() ||
                registrationForm.getSecurityQuestion2().isBlank() ||
                registrationForm.getSecurityAnswer2().isBlank()) {
            errors.add("No security question or answer can be empty");
        }
    }

}
