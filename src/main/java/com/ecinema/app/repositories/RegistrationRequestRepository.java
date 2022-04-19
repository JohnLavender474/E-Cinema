package com.ecinema.app.repositories;

import com.ecinema.app.entities.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
    Optional<RegistrationRequest> findByToken(String token);
    List<RegistrationRequest> findAllByEmail(String email);
    void deleteAllByEmail(String email);
    void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime);
}
