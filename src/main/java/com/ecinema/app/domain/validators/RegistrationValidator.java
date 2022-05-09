package com.ecinema.app.domain.validators;

import com.ecinema.app.domain.contracts.IRegistration;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RegistrationValidator implements AbstractValidator<IRegistration> {

    private final EmailValidator emailValidator;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;

    public RegistrationValidator(EmailValidator emailValidator, UsernameValidator usernameValidator,
                                 PasswordValidator passwordValidator) {
        this.emailValidator = emailValidator;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void validate(IRegistration registration, Collection<String> errors) {
        passwordValidator.validate(registration, errors);
        emailValidator.validate(registration.getEmail(), errors);
        usernameValidator.validate(registration.getUsername(), errors);
        if (!registration.getPassword().equals(registration.getConfirmPassword())) {
            errors.add("Password does not match password confirmation");
        }
        if (registration.getFirstName().isBlank()) {
            errors.add("First name is blank");
        } else if (!UtilMethods.isAlphabeticalOnly(registration.getFirstName())) {
            errors.add("First name must contain only alphabetical characters");
        }
        if (registration.getLastName().isBlank()) {
            errors.add("Last name is blank");
        } else if (!UtilMethods.isAlphabeticalOnly(registration.getLastName())) {
            errors.add("Last name must contain only alphabetical characters");
        }
        if (registration.getSecurityQuestion1().isBlank() ||
                registration.getSecurityAnswer1().isBlank() ||
                registration.getSecurityQuestion2().isBlank() ||
                registration.getSecurityAnswer2().isBlank()) {
            errors.add("No security question or answer can be empty");
        }
    }

}
