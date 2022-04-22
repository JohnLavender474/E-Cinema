package com.ecinema.app.repositories;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * The interface User role def repository.
 *
 * @param <T> the type parameter
 */
@NoRepositoryBean
public interface UserRoleDefRepository<T extends UserRoleDef> extends JpaRepository<T, Long> {

    /**
     * Find by user optional.
     *
     * @param user the user
     * @return the optional
     */
    Optional<T> findByUser(User user);

}
