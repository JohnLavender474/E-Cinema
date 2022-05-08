package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.UserRoleDef;
import com.ecinema.app.repositories.UserRoleDefRepository;
import com.ecinema.app.services.UserRoleDefService;

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
        logger.debug("Detach user role def from user: " + user);
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

    @Override
    public Optional<Long> findIdByUser(User user) {
        return repository.findIdByUser(user);
    }

    @Override
    public Optional<Long> findIdByUserWithId(Long userId) {
        return repository.findIdByUserWithId(userId);
    }

}
