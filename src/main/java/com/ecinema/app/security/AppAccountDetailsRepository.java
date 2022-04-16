package com.ecinema.app.security;

import com.ecinema.app.security.AppAccountDetails;
import com.ecinema.app.security.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppAccountDetailsRepository extends JpaRepository<AppAccountDetails, Long> {
    Optional<AppAccountDetails> findByEmail(String email);
    List<AppAccountDetails> findAllByAppUserRole(AppUserRole appUserRole);
    List<AppAccountDetails> findAllByIsAccountLocked(Boolean isAccountLocked);
    List<AppAccountDetails> findAllByIsAccountEnabled(Boolean isAccountEnabled);
    List<AppAccountDetails> findAllByIsAccountExpired(Boolean isAccountExpired);
    List<AppAccountDetails> findAllByIsCredentialsExpired(Boolean isCredentialsExpired);
}
