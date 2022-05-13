package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserAuthority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@ToString
public class AdminAuthority extends AbstractUserAuthority {

    @Override
    protected UserAuthority defineUserRole() {
        return UserAuthority.ADMIN;
    }

}
