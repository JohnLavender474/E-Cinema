package com.ecinema.app.repositories;

import com.ecinema.app.entities.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The interface Registration request repository.
 */
@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<RegistrationRequest> findByToken(String token);

    /**
     * Find all by email list.
     *
     * @param email the email
     * @return the list
     */
    List<RegistrationRequest> findAllByEmail(String email);

    /**
     * Delete all by email.
     *
     * @param email the email
     */
    void deleteAllByEmail(String email);

    /**
     * Delete all by creation date time before.
     *
     * @param localDateTime the local date time
     */
    void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime);

}
