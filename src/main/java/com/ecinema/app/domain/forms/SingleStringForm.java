package com.ecinema.app.domain.forms;

import lombok.Data;

import java.io.Serializable;

@Data
public class SingleStringForm implements Serializable {
    private String string = "";
}
