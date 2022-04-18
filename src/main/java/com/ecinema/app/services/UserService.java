package com.ecinema.app.services;

import com.ecinema.app.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService extends AbstractService<User>, UserDetailsService {
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
