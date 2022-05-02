package com.ecinema.app.utils;

import java.util.Set;

public interface IRegistration {
    Set<UserRole> getUserRoles();
    void setUserRoles(Set<UserRole> userRoles);
    String getUsername();
    void setUsername(String username);
    String getEmail();
    void setEmail(String email);
    String getPassword();
    void setPassword(String password);
    String getConfirmPassword();
    void setConfirmPassword(String confirmPassword);
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getSecurityQuestion1();
    void setSecurityQuestion1(String securityQuestion1);
    String getSecurityAnswer1();
    void setSecurityAnswer1(String securityAnswer1);
    String getSecurityQuestion2();
    void setSecurityQuestion2(String securityQuestion2);
    String getSecurityAnswer2();
    void setSecurityAnswer2(String securityAnswer2);
}
