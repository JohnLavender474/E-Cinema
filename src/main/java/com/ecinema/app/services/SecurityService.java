package com.ecinema.app.services;

import com.ecinema.app.utils.exceptions.NoEntityFoundException;

public interface SecurityService {

    /**
     * Login.
     *
     * @param s the login credential
     * @param password the password
     * @throws NoEntityFoundException the no entity found exception
     */
    void login(final String s, final String password)
            throws NoEntityFoundException;

    /**
     * Find logged in user email string.
     *
     * @return the string
     */
    String findLoggedInUserEmail();

}
