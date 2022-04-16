package com.ecinema.app.exceptions;

public class NoEntityFoundException extends BusinessRuntimeException {

    public static final String ERROR = "No %s found associated with field %s: %s";

    public NoEntityFoundException(String entity, String field, String value) {
        super(String.format(ERROR, entity, field, value));
    }

}
