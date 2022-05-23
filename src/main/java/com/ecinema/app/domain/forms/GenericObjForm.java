package com.ecinema.app.domain.forms;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenericObjForm<T> implements Serializable {
    private T t;
}
