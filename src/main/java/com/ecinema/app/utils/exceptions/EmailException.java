package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class EmailException extends BusinessRuntimeException {

    public EmailException(Collection<String> errors) {
        super(errors);
    }

    public EmailException(String... errors) {
        super(errors);
    }

}
