package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IRegistration;
import com.ecinema.app.domain.enums.UserRole;
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
    private Integer birthYear = 2020;
    private Integer birthDay = 1;
    private Month birthMonth = Month.JANUARY;

    @Override
    public LocalDate getBirthDate() {
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }

    @Override
    public void setBirthDate(LocalDate birthDate) {
        setBirthYear(birthDate.getYear());
        setBirthMonth(birthDate.getMonth());
        setBirthDay(birthDate.getDayOfMonth());
    }

}
