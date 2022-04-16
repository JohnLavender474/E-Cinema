package com.ecinema.app.security;

import com.ecinema.app.abstraction.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AppUser extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppAccountDetails appAccountDetails;

    @Column
    private LocalDateTime creationDateAndTime;

    @Column
    private LocalDateTime lastActiveSessionTime;

    @Column
    private String securityQuestion1;

    @Column
    private String securityAnswer1;

    @Column
    private String securityQuestion2;

    @Column
    private String securityAnswer2;

}
