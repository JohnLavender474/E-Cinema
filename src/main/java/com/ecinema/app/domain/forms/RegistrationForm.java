package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IRegistration;
import com.ecinema.app.domain.enums.UserAuthority;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
public class RegistrationForm implements IRegistration, Serializable {
    private Boolean isPasswordEncoded = false;
    private Boolean isSecurityAnswer1Encoded = false;
    private Boolean isSecurityAnswer2Encoded = false;
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
    private LocalDate birthDate = LocalDate.of(2000, Month.JANUARY, 1);
    private Set<UserAuthority> userAuthorities = EnumSet.noneOf(UserAuthority.class);
}
