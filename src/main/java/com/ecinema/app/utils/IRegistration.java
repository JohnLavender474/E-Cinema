package com.ecinema.app.utils;

import com.ecinema.app.utils.UserRole;

import java.util.Set;

public interface IRegistration {
    void setUserRoles(Set<UserRole> userRoles);
    Set<UserRole> getUserRoles();
    void setUsername(String username);
    String getUsername();
    void setEmail(String email);
    String getEmail();
    void setPassword(String password);
    String getPassword();
    void setConfirmPassword(String confirmPassword);
    String getConfirmPassword();
    void setFirstName(String firstName);
    String getFirstName();
    void setLastName(String lastName);
    String getLastName();
    void setSecurityQuestion1(String securityQuestion1);
    String getSecurityQuestion1();
    void setSecurityAnswer1(String securityAnswer1);
    String getSecurityAnswer1();
    void setSecurityQuestion2(String securityQuestion2);
    String getSecurityQuestion2();
    void setSecurityAnswer2(String securityAnswer2);
    String getSecurityAnswer2();
}
