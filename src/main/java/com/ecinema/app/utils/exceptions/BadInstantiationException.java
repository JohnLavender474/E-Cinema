package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class BadInstantiationException extends AbstractRuntimeException {

    public BadInstantiationException(String... errors) {
        super(errors);
    }

    public BadInstantiationException(Collection<String> errors) {
        super(errors);
    }

}
