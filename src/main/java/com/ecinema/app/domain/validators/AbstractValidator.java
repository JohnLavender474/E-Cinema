package com.ecinema.app.domain.validators;

import java.util.List;

public interface AbstractValidator<T> {
    void validate(T t, List<String> errors);
}
