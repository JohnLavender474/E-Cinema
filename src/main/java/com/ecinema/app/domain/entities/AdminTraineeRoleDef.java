package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * {@inheritDoc}
 * The admin trainee role def grants a {@link User} the role of {@link UserRole#ADMIN_TRAINEE} along with its privileges.
 * {@link #percentageTrainingModulesCompleted} defines the completion of training the admin trainee has completed.
 * {@link #mentor} is required and defines the {@link AdminRoleDef} that is the mentor of the trainee.
 */
@Getter
@Setter
@Entity
public class AdminTraineeRoleDef extends UserRoleDef {

    @Column
    private Integer percentageTrainingModulesCompleted;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminRoleDef mentor;

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN_TRAINEE;
    }

}
