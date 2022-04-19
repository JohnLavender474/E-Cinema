package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class AdminRoleDef extends UserRoleDef {

    @ManyToMany
    private Set<Theater> theaters;

    @OneToMany
    private Set<AdminTraineeRoleDef> trainees = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN;
    }

}
