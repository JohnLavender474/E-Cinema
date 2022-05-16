package com.ecinema.app.services.implementations;

import com.ecinema.app.services.EncoderService;
import com.ecinema.app.utils.UtilMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Encoder service.
 */
@Service
public class EncoderServiceImpl implements EncoderService {

    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(EncoderService.class);

    /**
     * Instantiates a new Encoder service.
     *
     * @param passwordEncoder the password encoder
     */
    public EncoderServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String s) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Encoder Service: encode s");
        logger.debug("First 3 chars of s before encryption: " + s.subSequence(0, 3));
        String encoded = passwordEncoder.encode(s);
        logger.debug("First 3 chars of encrypted s: " + encoded.subSequence(0, 3));
        return encoded;
    }

    @Override
    public String removeWhiteSpace_SetToAllUpperCase_AndThenEncode(String s) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Encoder Service: remove white space, set to all upper case, and then encode s");
        logger.debug("First 3 chars of s before formatted: " + s.subSequence(0, 3));
        String formatted = UtilMethods.removeWhitespace(s).toUpperCase();
        logger.debug("First 3 chars of s after formatted: " + formatted.subSequence(0, 3));
        return encode(formatted);
    }

    @Override
    public boolean matches(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

}
