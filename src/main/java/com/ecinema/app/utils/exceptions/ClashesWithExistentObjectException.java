package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class ClashesWithExistentObjectException extends BusinessRuntimeException {

    public ClashesWithExistentObjectException(String... errors) {
        super(errors);
    }

    public ClashesWithExistentObjectException(Collection<String> errors) {
        super(errors);
    }

}
