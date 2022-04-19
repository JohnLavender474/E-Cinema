package com.ecinema.app.utils.exceptions;

public class NoEntityFoundException extends BusinessRuntimeException {

    public static final String ERROR = "No %s found associated with field %s with value = %s";

    public NoEntityFoundException(String entity, String field, String value) {
        super(String.format(ERROR, entity, field, value));
    }

}
