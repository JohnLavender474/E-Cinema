package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.ChangePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Change password request repository.
 */
@Repository
public interface ChangePasswordRepository extends JpaRepository<ChangePassword, Long>,
                                                  AbstractRepository {

    /**
     * Delete by user id.
     *
     * @param userId the user id
     */
    void deleteAllByUserId(Long userId);

    /**
     * Exists by token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    boolean existsByToken(String token);

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<ChangePassword> findByToken(String token);

}
