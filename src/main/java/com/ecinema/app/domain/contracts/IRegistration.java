package com.ecinema.app.domain.contracts;

import com.ecinema.app.domain.enums.UserAuthority;

import java.time.LocalDate;
import java.util.Set;

public interface IRegistration extends IPassword {
    Set<UserAuthority> getUserAuthorities();
    void setUserAuthorities(Set<UserAuthority> userAuthorities);
    String getUsername();
    void setUsername(String username);
    String getEmail();
    void setEmail(String email);
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    LocalDate getBirthDate();
    void setBirthDate(LocalDate birthDate);
    String getSecurityQuestion1();
    void setSecurityQuestion1(String securityQuestion1);
    String getSecurityAnswer1();
    void setSecurityAnswer1(String securityAnswer1);
    String getSecurityQuestion2();
    void setSecurityQuestion2(String securityQuestion2);
    String getSecurityAnswer2();
    void setSecurityAnswer2(String securityAnswer2);
}
