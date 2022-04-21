package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import com.ecinema.app.repositories.UserRoleDefRepository;
import com.ecinema.app.services.UserRoleDefService;
import com.ecinema.app.utils.constants.UserRole;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public abstract class UserRoleDefServiceImpl<T extends UserRoleDef, R extends UserRoleDefRepository<T>>
        extends AbstractServiceImpl<T, R> implements UserRoleDefService<T> {

    public UserRoleDefServiceImpl(R repository) {
        super(repository);
    }

    @Override
    public Optional<T> findByUser(User user) {
        return repository.findByUser(user);
    }

}
