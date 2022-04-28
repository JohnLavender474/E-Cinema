package com.ecinema.app.utils.exceptions;

import java.util.Collection;

public class NoAssociationException extends AbstractRuntimeException {

    public NoAssociationException(String... errors) {
        super(errors);
    }

    public NoAssociationException(Collection<String> errors) {
        super(errors);
    }

    public NoAssociationException(Object a, Object b) {
        super("There is no association between " + a.toString() + " and " + b.toString());
    }

}
