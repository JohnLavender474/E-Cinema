package com.ecinema.app.utils.constants;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public enum UserRole implements GrantedAuthority {

    ADMIN(new HashSet<>() {{
        add(UserPermission.CUSTOMER_READ.getPermission());
        add(UserPermission.CUSTOMER_WRITE.getPermission());
        add(UserPermission.CUSTOMER_DELETE.getPermission());
    }}),
    CUSTOMER(new HashSet<>() {}),
    MODERATOR(new HashSet<>() {}),
    ADMIN_TRAINEE(new HashSet<>() {});

    private final Set<String> privileges;

    UserRole(Set<String> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String getAuthority() {
        return name();
    }

}
