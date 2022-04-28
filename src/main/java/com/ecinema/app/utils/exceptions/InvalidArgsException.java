package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class InvalidArgsException extends AbstractRuntimeException {

    public InvalidArgsException(String... errors) {
        super(errors);
    }

    public InvalidArgsException(Collection<String> errors) {
        super(errors);
    }

}
