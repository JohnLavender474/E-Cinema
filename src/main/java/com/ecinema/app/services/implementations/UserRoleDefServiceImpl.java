package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import com.ecinema.app.repositories.UserRoleDefRepository;
import com.ecinema.app.services.UserRoleDefService;
import com.ecinema.app.utils.constants.UserRole;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * The type User role def service.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public abstract class UserRoleDefServiceImpl<T extends UserRoleDef, R extends UserRoleDefRepository<T>>
        extends AbstractServiceImpl<T, R> implements UserRoleDefService<T> {

    /**
     * Instantiates a new User role def service.
     *
     * @param repository the repository
     */
    public UserRoleDefServiceImpl(R repository) {
        super(repository);
    }

    @Override
    protected void onDelete(T userRoleDef) {
        User user = userRoleDef.getUser();
        if (user != null) {
            user.getUserRoleDefs().remove(userRoleDef.getUserRole());
            userRoleDef.setUser(null);
        }
    }

    @Override
    public Optional<T> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public Optional<T> findByUserWithId(Long userId) {
        return repository.findByUserWithId(userId);
    }

}
