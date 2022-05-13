package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.exceptions.NoEntityFoundException;

/**
 * The interface Security service.
 */
public interface SecurityService {

    /**
     * Login.
     *
     * @param s        the login credential
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
    UserDto findLoggedInUserDTO();

    /**
     * Find id of logged-in user long.
     *
     * @return the long
     */
    Long findIdOfLoggedInUser();

}
