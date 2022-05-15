package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.exceptions.DuplicateActionException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

/**
 * The interface Registration.
 */
public interface IRegistration extends IProfile, IPassword {

    /**
     * Is password encoded boolean.
     *
     * @return the boolean
     */
    Boolean getIsPasswordEncoded();

    /**
     * Sets is password encoded.
     *
     * @param isPasswordEncoded the is password encoded
     */
    void setIsPasswordEncoded(Boolean isPasswordEncoded);

    /**
     * Gets is security answer 1 encoded.
     *
     * @return the is security answer 1 encoded
     */
    Boolean getIsSecurityAnswer1Encoded();

    /**
     * Sets is security answer 1 encoded.
     *
     * @param isSecurityAnswer1Encoded the is security answer 1 encoded
     */
    void setIsSecurityAnswer1Encoded(Boolean isSecurityAnswer1Encoded);

    /**
     * Gets is security answer 2 encoded.
     *
     * @return the is security answer 2 encoded
     */
    Boolean getIsSecurityAnswer2Encoded();

    /**
     * Sets is security answer 2 encoded.
     *
     * @param isSecurityAnswer2Encoded the is security answer 2 encoded
     */
    void setIsSecurityAnswer2Encoded(Boolean isSecurityAnswer2Encoded);

    /**
     * Gets email.
     *
     * @return the email
     */
    String getEmail();

    /**
     * Sets email.
     *
     * @param email the email
     */
    void setEmail(String email);

    /**
     * Gets username.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Sets username.
     *
     * @param username the username
     */
    void setUsername(String username);

    /**
     * Gets security question 1.
     *
     * @return the security question 1
     */
    String getSecurityQuestion1();

    /**
     * Sets security question 1.
     *
     * @param sq1 the sq 1
     */
    void setSecurityQuestion1(String sq1);

    /**
     * Gets security answer 1.
     *
     * @return the security answer 1
     */
    String getSecurityAnswer1();

    /**
     * Sets security answer 1.
     *
     * @param sa1 the sa 1
     */
    void setSecurityAnswer1(String sa1);

    /**
     * Gets security question 2.
     *
     * @return the security question 2
     */
    String getSecurityQuestion2();

    /**
     * Sets security question 2.
     *
     * @param sq2 the first security question
     */
    void setSecurityQuestion2(String sq2);

    /**
     * Gets security answer 2.
     *
     * @return the security answer 2
     */
    String getSecurityAnswer2();

    /**
     * Sets security answer 2.
     *
     * @param sa2 the sa 2
     */
    void setSecurityAnswer2(String sa2);

    /**
     * Gets user authorities.
     *
     * @return the user authorities
     */
    Set<UserAuthority> getUserAuthorities();

    /**
     * Sets user authorities.
     *
     * @param userAuthorities the user authorities
     */
    void setUserAuthorities(Set<UserAuthority> userAuthorities);

    /**
     * Sets to i registration.
     *
     * @param o the o
     */
    default void setToIRegistration(IRegistration o) {
        setToIProfile(o);
        setToIPassword(o);
        setEmail(o.getEmail());
        setUsername(o.getUsername());
        setSecurityQuestion1(o.getSecurityQuestion1());
        setSecurityAnswer1(o.getSecurityAnswer1());
        setSecurityQuestion2(o.getSecurityQuestion2());
        setSecurityAnswer2(o.getSecurityAnswer2());
        setIsPasswordEncoded(o.getIsPasswordEncoded());
        setIsSecurityAnswer1Encoded(o.getIsSecurityAnswer1Encoded());
        setIsSecurityAnswer2Encoded(o.getIsSecurityAnswer2Encoded());
    }

}
