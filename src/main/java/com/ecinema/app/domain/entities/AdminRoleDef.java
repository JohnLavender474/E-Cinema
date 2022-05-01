package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 * The admin role def grants a {@link User} the role of {@link UserRole#ADMIN} along with its permissions.
 * {@link Theater} instances this class has admin privileges over are mapped to {@link #theatersBeingManaged}.
 * {@link AdminTraineeRoleDef} instances this class has admin privileges over are mapped to {@link #trainees}.
 */
@Getter
@Setter
@Entity
public class AdminRoleDef extends UserRoleDef {

    @ManyToMany
    private Set<Theater> theatersBeingManaged = new HashSet<>();

    @OneToMany(mappedBy = "mentor")
    private Set<AdminTraineeRoleDef> trainees = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN;
    }

}
