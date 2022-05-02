package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class AdminRoleDef extends UserRoleDef {

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN;
    }

}
