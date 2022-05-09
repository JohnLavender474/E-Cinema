package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserRole;
import lombok.*;

import javax.persistence.*;

/**
 * {@inheritDoc}
 * This class defines a set of fields and mappings for the permissions granted by a user role.
 * Each class extending this has a direct one-to-one relationship with a value defined in {@link UserRole}.
 * The relationship between this and {@link User} is required and many-to-one.
 */
@Getter
@Setter
@Entity
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserRoleDef extends AbstractEntity {

    @JoinColumn
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private UserRole userRole;

    @Column
    private Boolean isRoleValid;

    /**
     * Instantiates a new User role def.
     */
    public UserRoleDef() {
        userRole = defineUserRole();
    }

    /**
     * The value of {@link #userRole} must be immutable, so it is defined once by this abstract method.
     * Its setter has been excluded to ensure that the value of userRole is final in this class's database
     * representation
     *
     * @return the final value of userRole.
     */
    protected abstract UserRole defineUserRole();

}
