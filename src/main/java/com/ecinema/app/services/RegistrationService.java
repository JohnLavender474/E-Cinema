package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.Registration;
import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 * The interface Registration request service.
 */
public interface RegistrationService extends AbstractService<Registration> {

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<Registration> findByToken(String token);

    /**
     * Submit registration form.
     *
     * @param registrationForm the registration form
     * @throws ClashException       the clash exception
     * @throws InvalidArgsException the invalid args exception
     * @throws EmailException       the email exception
     */
    void submitRegistrationForm(RegistrationForm registrationForm)
        throws ClashException, InvalidArgsException, EmailException;

    /**
     * Submit registration request and get token string.
     *
     * @param registrationForm the registration form
     * @return the string
     * @throws ClashException       the clashes with existent object exception
     * @throws InvalidArgsException the invalid arg exception
     * @throws EmailException       the email exception
     */
    String submitRegistrationFormAndGetToken(RegistrationForm registrationForm)
            throws ClashException, InvalidArgsException, EmailException;

    /**
     * Find all by email list.
     *
     * @param email the email
     * @return the list
     */
    List<Registration> findAllByEmail(String email);

    /**
     * Delete all by email.
     *
     * @param email the email
     */
    void deleteAllByEmail(String email);

    /**
     * Confirm registration request.
     *
     * @param token the token
     * @return the user dto
     * @throws NoEntityFoundException the no entity found exception
     */
    UserDto confirmRegistrationRequest(String token)
            throws NoEntityFoundException;

    /**
     * Delete all by creation date time before.
     *
     * @param localDateTime the local date time
     */
    void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime);

}
