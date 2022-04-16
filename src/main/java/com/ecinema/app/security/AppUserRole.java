package com.ecinema.app.security;

import com.ecinema.app.user.Admin;
import com.ecinema.app.user.Customer;
import org.springframework.security.core.GrantedAuthority;

public enum AppUserRole implements GrantedAuthority {

    ADMIN(Admin.class),
    CUSTOMER(Customer.class);

    private final Class<? extends AppUser> userClass;

    AppUserRole(Class<? extends AppUser> userClass) {
        this.userClass = userClass;
    }

    public Class<? extends AppUser> getUserClass() {
        return userClass;
    }

    @Override
    public String getAuthority() {
        return name();
    }

}
