package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@ToString
public class AdminRoleDef extends UserRoleDef {

    @Override
    protected UserRole defineUserRole() {
        return UserRole.ADMIN;
    }

}
