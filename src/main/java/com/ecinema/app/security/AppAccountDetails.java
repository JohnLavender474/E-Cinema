package com.ecinema.app.security;

import com.ecinema.app.abstraction.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
public class AppAccountDetails extends AbstractEntity implements UserDetails {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppUser appUser;

    @Column
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private Boolean isAccountLocked;

    @Column
    private Boolean isAccountEnabled;

    @Column
    private Boolean isAccountExpired;

    @Column
    private Boolean isCredentialsExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>() {{
            add(new SimpleGrantedAuthority(appUserRole.getAuthority()));
        }};
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return isAccountEnabled;
    }

}
