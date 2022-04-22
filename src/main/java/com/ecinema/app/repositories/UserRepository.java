package com.ecinema.app.repositories;

import com.ecinema.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsByEmail(String email);

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all by is account locked list.
     *
     * @param isAccountLocked the is account locked
     * @return the list
     */
    List<User> findAllByIsAccountLocked(boolean isAccountLocked);

    /**
     * Find all by is account enabled list.
     *
     * @param isAccountEnabled the is account enabled
     * @return the list
     */
    List<User> findAllByIsAccountEnabled(boolean isAccountEnabled);

    /**
     * Find all by is account expired list.
     *
     * @param isAccountExpired the is account expired
     * @return the list
     */
    List<User> findAllByIsAccountExpired(boolean isAccountExpired);

    /**
     * Find all by is credentials expired list.
     *
     * @param isCredentialsExpired the is credentials expired
     * @return the list
     */
    List<User> findAllByIsCredentialsExpired(boolean isCredentialsExpired);

    /**
     * Find all by creation date time before list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<User> findAllByCreationDateTimeBefore(LocalDateTime localDateTime);

    /**
     * Find all by creation date time after list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<User> findAllByCreationDateTimeAfter(LocalDateTime localDateTime);

    /**
     * Find all by last activity date time before list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<User> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime);

    /**
     * Find all by last activity date time after list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    List<User> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime);

}
