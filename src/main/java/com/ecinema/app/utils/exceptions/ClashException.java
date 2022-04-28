package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class ClashException extends AbstractRuntimeException {

    public ClashException(String... errors) {
        super(errors);
    }

    public ClashException(Collection<String> errors) {
        super(errors);
    }

}
