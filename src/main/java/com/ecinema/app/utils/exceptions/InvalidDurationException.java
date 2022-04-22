package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class InvalidDurationException extends BusinessRuntimeException {

    public InvalidDurationException(String... errors) {
        super(errors);
    }

    public InvalidDurationException(Collection<String> errors) {
        super(errors);
    }

}
