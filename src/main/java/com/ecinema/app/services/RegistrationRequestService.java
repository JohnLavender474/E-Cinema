package com.ecinema.app.services;

import com.ecinema.app.entities.RegistrationRequest;
import com.ecinema.app.utils.exceptions.ClashesWithExistentObjectException;
import com.ecinema.app.utils.exceptions.EmailException;
import com.ecinema.app.utils.exceptions.InvalidArgException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import com.ecinema.app.utils.forms.RegistrationForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Registration request service.
 */
public interface RegistrationRequestService extends AbstractService<RegistrationRequest> {

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<RegistrationRequest> findByToken(String token);

    /**
     * Submit registration request and get token string.
     *
     * @param registrationForm the registration form
     * @return the string
     * @throws ClashesWithExistentObjectException the clashes with existent object exception
     * @throws InvalidArgException                the invalid arg exception
     * @throws EmailException                     the email exception
     */
    String submitRegistrationRequestAndGetToken(RegistrationForm registrationForm)
            throws ClashesWithExistentObjectException, InvalidArgException, EmailException;

    /**
     * Find all by email list.
     *
     * @param email the email
     * @return the list
     */
    List<RegistrationRequest> findAllByEmail(String email);

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
    void confirmRegistrationRequest(String token)
            throws NoEntityFoundException;

    /**
     * Delete all by creation date time before.
     *
     * @param localDateTime the local date time
     */
    void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime);

}
