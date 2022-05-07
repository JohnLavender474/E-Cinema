package com.ecinema.app.domain.contracts;

public interface IPassword {
    void setPassword(String password);
    String getPassword();
    void setConfirmPassword(String confirmPassword);
    String getConfirmPassword();
}
