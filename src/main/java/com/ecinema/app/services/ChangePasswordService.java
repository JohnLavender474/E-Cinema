package com.ecinema.app.services;

import com.ecinema.app.domain.entities.ChangePassword;
import com.ecinema.app.domain.forms.ChangePasswordForm;
import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.exceptions.ExpirationException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;

/**
 * The interface Change password request service.
 */
public interface ChangePasswordService extends AbstractService<ChangePassword> {

    /**
     * Gets change password form.
     *
     * @param email the email
     * @return the change password form
     * @throws NoEntityFoundException the no entity found exception
     */
    ChangePasswordForm getChangePasswordForm(String email)
            throws NoEntityFoundException;

    /**
     * Submit change password form.
     *
     * @param changePasswordForm the change password form
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid args exception
     * @throws EmailException         the email exception
     */
    void submitChangePasswordForm(ChangePasswordForm changePasswordForm)
            throws NoEntityFoundException, InvalidArgsException, EmailException;

    /**
     * Confirm change password.
     *
     * @param token the token
     * @throws NoEntityFoundException the no entity found exception
     * @throws ExpirationException    the expiration exception
     */
    void confirmChangePassword(String token)
            throws NoEntityFoundException, ExpirationException;

    /**
     * Exists by token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    boolean existsByToken(String token);

}
