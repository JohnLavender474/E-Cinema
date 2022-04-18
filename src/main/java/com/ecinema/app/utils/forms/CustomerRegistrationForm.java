package com.ecinema.app.utils.forms;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomerRegistrationForm implements Serializable {
    private String email;
    private String password;
    private String confirmPassword;

}
