package com.ecinema.app.utils.validators;

import java.util.List;

public interface AbstractValidator<T> {
    void validate(T t, List<String> errors);
}
