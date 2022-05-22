package com.ecinema.app.beans;

import com.ecinema.app.domain.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContext {

    protected User findLoggedInUser() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object o = authentication.getPrincipal();
        return o instanceof User user ? user : null;
    }

    public Long findIdOfLoggedInUser() {
        User user = findLoggedInUser();
        return user != null ? user.getId() : null;
    }

    public boolean userIsLoggedIn() {
        return findLoggedInUser() != null;
    }

}
