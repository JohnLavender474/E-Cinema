package com.ecinema.app.domain.forms;

import com.ecinema.app.utils.IRegistration;
import com.ecinema.app.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
public class RegistrationForm implements IRegistration, Serializable {
    private Set<UserRole> userRoles =
            EnumSet.noneOf(UserRole.class);
    private String username = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private String firstName = "";
    private String lastName = "";
    private String securityQuestion1 = "";
    private String securityAnswer1 = "";
    private String securityQuestion2 = "";
    private String securityAnswer2 = "";
}
