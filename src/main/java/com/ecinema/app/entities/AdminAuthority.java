package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class AdminAuthority extends UserAuthority {

    @ManyToMany
    private Set<Theater> theaters;

    @OneToMany
    private Set<AdminTraineeAuthority> adminTraineeAuthorities;

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN;
    }

}
