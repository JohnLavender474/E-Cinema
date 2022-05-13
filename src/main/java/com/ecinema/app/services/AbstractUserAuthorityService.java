package com.ecinema.app.services;

import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.AbstractUserAuthority;

import java.util.Optional;

/**
 * The interface User role def service.
 *
 * @param <T> the type parameter
 */
public interface AbstractUserAuthorityService<T extends AbstractUserAuthority> extends AbstractService<T> {

    /**
     * Find by user optional.
     *
     * @param user the user
     * @return the optional
     */
    Optional<T> findByUser(User user);

    /**
     * Find by user with id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<T> findByUserWithId(Long userId);

    /**
     * Find id by user optional.
     *
     * @param user the user
     * @return the optional
     */
    Optional<Long> findIdByUser(User user);

    /**
     * Find id by user with id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<Long> findIdByUserWithId(Long userId);

}
