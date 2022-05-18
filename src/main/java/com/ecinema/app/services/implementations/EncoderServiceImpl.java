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
        logger.debug("Encoder Service: encode s");
        logger.debug("First 3 chars of s before encryption: " + s.subSequence(0, 3));
        String encoded = passwordEncoder.encode(UtilMethods.removeWhitespace(s));
        logger.debug("First 3 chars of encrypted s: " + encoded.subSequence(0, 3));
        return encoded;
    }

    @Override
    public boolean matches(String raw, String encoded) {
        return passwordEncoder.matches(UtilMethods.removeWhitespace(raw), encoded);
    }

}
