package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class InvalidArgException extends BusinessRuntimeException {

    public InvalidArgException(String... errors) {
        super(errors);
    }

    public InvalidArgException(Collection<String> errors) {
        super(errors);
    }

}
