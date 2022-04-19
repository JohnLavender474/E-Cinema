package com.ecinema.app.utils.objects;

public interface TwoArgHandler<T, U> {
    void handle(T t, U u);
}
