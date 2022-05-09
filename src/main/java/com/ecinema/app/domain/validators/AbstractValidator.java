package com.ecinema.app.domain.validators;

import java.util.Collection;

public interface AbstractValidator<T> {
    void validate(T t, Collection<String> errors);
}
