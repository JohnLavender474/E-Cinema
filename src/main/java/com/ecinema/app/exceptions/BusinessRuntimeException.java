package com.ecinema.app.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
public class BusinessRuntimeException extends RuntimeException {

    private Collection<String> errors;

    public BusinessRuntimeException(Collection<String> errors) {
        this.errors = errors;
    }

    public BusinessRuntimeException(String... errors) {
        this.errors = Arrays.asList(errors);
    }

}
