package com.ecinema.app.services;

import com.ecinema.app.entities.Registration;
import com.ecinema.app.utils.dtos.UserDTO;
import com.ecinema.app.utils.exceptions.ClashException;
import com.ecinema.app.utils.exceptions.EmailException;
import com.ecinema.app.utils.exceptions.InvalidArgsException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import com.ecinema.app.utils.forms.RegistrationForm;

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
     * Submit registration request and get token string.
     *
     * @param registrationForm the registration form
     * @return the string
     * @throws ClashException the clashes with existent object exception
     * @throws InvalidArgsException                the invalid arg exception
     * @throws EmailException                     the email exception
     */
    String submitRegistrationRequestAndGetToken(RegistrationForm registrationForm)
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
     * @throws NoEntityFoundException the no entity found exception
     */
    UserDTO confirmRegistrationRequest(String token)
            throws NoEntityFoundException;

    /**
     * Delete all by creation date time before.
     *
     * @param localDateTime the local date time
     */
    void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime);

}
