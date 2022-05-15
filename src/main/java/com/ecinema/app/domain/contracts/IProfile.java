package com.ecinema.app.domain.contracts;

import java.time.LocalDate;

/**
 * The interface Profile.
 */
public interface IProfile {

    /**
     * Gets first name.
     *
     * @return the first name
     */
    String getFirstName();

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    void setFirstName(String firstName);

    /**
     * Gets last name.
     *
     * @return the last name
     */
    String getLastName();

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    void setLastName(String lastName);

    /**
     * Gets the birthdate.
     *
     * @return the birthdate
     */
    LocalDate getBirthDate();

    /**
     * Sets birthdate.
     *
     * @param birthDate the birthdate
     */
    void setBirthDate(LocalDate birthDate);

    default void setToIProfile(IProfile o) {
        setFirstName(o.getFirstName());
        setLastName(o.getLastName());
        setBirthDate(o.getBirthDate());
    }

}
