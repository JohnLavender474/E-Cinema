package com.ecinema.app.security;

import com.ecinema.app.abstraction.AbstractServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppAccountDetailsServiceImpl extends AbstractServiceImpl<AppAccountDetails, AppAccountDetailsRepository> implements AppAccountDetailsService {

    public AppAccountDetailsServiceImpl(AppAccountDetailsRepository repository) {
        super(repository);
    }

    @Override
    public Optional<AppAccountDetails> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<AppAccountDetails> findAllByUserRole(AppUserRole appUserRole) {
        return repository.findAllByAppUserRole(appUserRole);
    }

    @Override
    public List<AppAccountDetails> findAllByIsAccountLocked(Boolean isAccountLocked) {
        return repository.findAllByIsAccountLocked(isAccountLocked);
    }

    @Override
    public List<AppAccountDetails> findAllByIsAccountEnabled(Boolean isAccountEnabled) {
        return repository.findAllByIsAccountEnabled(isAccountEnabled);
    }

    @Override
    public List<AppAccountDetails> findAllByIsAccountExpired(Boolean isAccountExpired) {
        return repository.findAllByIsAccountExpired(isAccountExpired);
    }

    @Override
    public List<AppAccountDetails> findAllByIsCredentialsExpired(Boolean isCredentialsExpired) {
        return repository.findAllByIsCredentialsExpired(isCredentialsExpired);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("No user found with email " + username));
    }

}
