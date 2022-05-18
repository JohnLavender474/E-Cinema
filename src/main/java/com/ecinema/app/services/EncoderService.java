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
     * Matches boolean.
     *
     * @param raw     the raw
     * @param encoded the encoded
     * @return the boolean
     */
    boolean matches(String raw, String encoded);

}
