package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.contracts.IRegistration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Registration extends AbstractEntity implements IRegistration {

    @Column
    private LocalDateTime creationDateTime;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRoles = EnumSet.noneOf(UserRole.class);

    @Column
    private String token;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String confirmPassword;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String securityQuestion1;

    @Column
    private String securityAnswer1;

    @Column
    private String securityQuestion2;

    @Column
    private String securityAnswer2;

}
