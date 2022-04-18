package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserAuthority extends AbstractEntity {

    @ManyToOne
    @JoinColumn
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column
    private Boolean isExtensionLocked;

    public UserAuthority() {
        setUserRole(defineUserRole());
    }

    protected abstract UserRole defineUserRole();

}
