package com.ecinema.app.repositories;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserRoleDefRepository<T extends UserRoleDef> extends JpaRepository<T, Long> {
    Optional<T> findByUser(User user);
}
