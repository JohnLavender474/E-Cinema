package com.ecinema.app.services;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;

import java.util.Optional;

public interface UserRoleDefService<T extends UserRoleDef> extends AbstractService<T> {
    Optional<T> findByUser(User user);
}
