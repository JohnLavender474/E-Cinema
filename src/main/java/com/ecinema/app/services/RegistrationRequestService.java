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

public interface RegistrationRequestService extends AbstractService<RegistrationRequest> {
    Optional<RegistrationRequest> findByToken(String token);
    String submitRegistrationRequestAndGetToken(RegistrationForm registrationForm)
            throws ClashesWithExistentObjectException, InvalidArgException, EmailException;
    List<RegistrationRequest> findAllByEmail(String email);
    void deleteAllByEmail(String email);
    void confirmRegistrationRequest(String token)
            throws NoEntityFoundException;
    void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime);
}
