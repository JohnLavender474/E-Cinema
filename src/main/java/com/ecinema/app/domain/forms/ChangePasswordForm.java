package com.ecinema.app.domain.forms;

import com.ecinema.app.utils.IPassword;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordForm implements IPassword, Serializable {
    private String email;
    private String password;
    private String confirmPassword;
}
