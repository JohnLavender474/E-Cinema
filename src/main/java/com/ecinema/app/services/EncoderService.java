package com.ecinema.app.services;

/**
 * The interface PasswordEncoder service.
 */
public interface EncoderService {

    /**
     * Encrypt string.
     *
     * @param s the s
     * @return the string
     */
    String encode(String s);

    /**
     * Remove white space set to all upper case and then encode string.
     *
     * @param s the s
     * @return the string
     */
    String removeWhiteSpace_SetToAllUpperCase_AndThenEncode(String s);

    /**
     * Matches boolean.
     *
     * @param raw     the raw
     * @param encoded the encoded
     * @return the boolean
     */
    boolean matches(String raw, String encoded);

}
