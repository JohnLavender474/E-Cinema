package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class AdminTraineeRoleDef extends UserRoleDef {

    @Column
    private Integer percentageTrainingModulesCompleted;

    @ManyToOne
    @JoinColumn
    private AdminRoleDef mentor;

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN_TRAINEE;
    }

}
