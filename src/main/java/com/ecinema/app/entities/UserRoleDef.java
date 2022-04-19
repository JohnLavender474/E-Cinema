package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserRoleDef extends AbstractEntity {

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.PRIVATE)
    private UserRole userRole;

    @Column
    private Boolean isAuthorityValid;

    public UserRoleDef() {
        setUserRole(defineUserRole());
    }

    protected abstract UserRole defineUserRole();

}
