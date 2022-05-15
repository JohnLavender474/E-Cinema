package com.ecinema.app.domain.contracts;

/**
 * The interface Password.
 */
public interface IPassword {

    /**
     * Sets password.
     *
     * @param password the password
     */
    void setPassword(String password);

    /**
     * Gets password.
     *
     * @return the password
     */
    String getPassword();

    /**
     * Sets confirm password.
     *
     * @param confirmPassword the confirmation password
     */
    void setConfirmPassword(String confirmPassword);

    /**
     * Gets confirm password.
     *
     * @return the confirmation password
     */
    String getConfirmPassword();

    /**
     * Sets to i password.
     *
     * @param o the o
     */
    default void setToIPassword(IPassword o) {
        setPassword(o.getPassword());
        setConfirmPassword(o.getConfirmPassword());
    }

}
