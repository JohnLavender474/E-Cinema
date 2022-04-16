package com.ecinema.app.security;

import com.ecinema.app.abstraction.AbstractService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface AppAccountDetailsService extends AbstractService<AppAccountDetails>, UserDetailsService {
    Optional<AppAccountDetails> findByEmail(String email);
    List<AppAccountDetails> findAllByUserRole(AppUserRole appUserRole);
    List<AppAccountDetails> findAllByIsAccountLocked(Boolean isAccountLocked);
    List<AppAccountDetails> findAllByIsAccountEnabled(Boolean isAccountEnabled);
    List<AppAccountDetails> findAllByIsAccountExpired(Boolean isAccountExpired);
    List<AppAccountDetails> findAllByIsCredentialsExpired(Boolean isCredentialsExpired);
}
