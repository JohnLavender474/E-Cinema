package com.ecinema.app.repositories;

import com.ecinema.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByIsAccountLocked(boolean isAccountLocked);
    List<User> findAllByIsAccountEnabled(boolean isAccountEnabled);
    List<User> findAllByIsAccountExpired(boolean isAccountExpired);
    List<User> findAllByIsCredentialsExpired(boolean isCredentialsExpired);
    List<User> findAllByCreationDateTimeBefore(LocalDateTime localDateTime);
    List<User> findAllByCreationDateTimeAfter(LocalDateTime localDateTime);
    List<User> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime);
    List<User> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime);
}
