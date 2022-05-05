package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.ChangePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
     * Delete by expiration date time is before.
     *
     * @param localDateTime the local date time
     */
    void deleteByExpirationDateTimeIsBefore(LocalDateTime localDateTime);

    /**
     * Exists by user id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    boolean existsByUserId(Long userId);

    /**
     * Exists by token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    boolean existsByToken(String token);

    /**
     * Find by user id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<ChangePassword> findByUserId(Long userId);

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<ChangePassword> findByToken(String token);

    /**
     * Find all by creation date time is before list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<ChangePassword> findAllByCreationDateTimeIsBefore(LocalDateTime localDateTime);

    /**
     * Find all by creation date time is after list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<ChangePassword> findAllByCreationDateTimeIsAfter(LocalDateTime localDateTime);

    /**
     * Find all by expiration date time is before list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<ChangePassword> findAllByExpirationDateTimeIsBefore(LocalDateTime localDateTime);

    /**
     * Find all by expiration date time is after list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<ChangePassword> findAllByExpirationDateTimeIsAfter(LocalDateTime localDateTime);

}
