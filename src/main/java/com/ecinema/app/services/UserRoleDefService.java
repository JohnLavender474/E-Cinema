package com.ecinema.app.services;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;

import java.util.Optional;

/**
 * The interface User role def service.
 *
 * @param <T> the type parameter
 */
public interface UserRoleDefService<T extends UserRoleDef> extends AbstractService<T> {

    /**
     * Find by user optional.
     *
     * @param user the user
     * @return the optional
     */
    Optional<T> findByUser(User user);

}
