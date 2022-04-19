package com.ecinema.app.services;

import com.ecinema.app.entities.User;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.ClashesWithExistentObjectException;
import com.ecinema.app.utils.exceptions.InvalidArgException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
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
    boolean existsByEmail(String email);
    void addUserRoleDefToUser(Long userId, UserRole userRole)
            throws NoEntityFoundException, InvalidArgException, ClashesWithExistentObjectException;
    void addUserRoleDefToUser(User user, UserRole userRole)
            throws InvalidArgException, ClashesWithExistentObjectException;
    void removeUserRoleDefFromUser(Long userId, UserRole userRole)
            throws NoEntityFoundException, ClashesWithExistentObjectException;
}
