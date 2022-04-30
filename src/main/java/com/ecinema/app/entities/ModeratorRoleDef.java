package com.ecinema.app.entities;

import com.ecinema.app.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class ModeratorRoleDef extends UserRoleDef {

    @Override
    protected UserRole defineUserRole() {
        return UserRole.MODERATOR;
    }

}
