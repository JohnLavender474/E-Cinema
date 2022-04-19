package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
public class User extends AbstractEntity implements UserDetails {

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate birthDate;

    @Column
    private String securityQuestion1;

    @Column
    private String securityAnswer1;

    @Column
    private String securityQuestion2;

    @Column
    private String securityAnswer2;

    @Column
    private LocalDateTime creationDateTime;

    @Column
    private LocalDateTime lastActivityDateTime;

    @Column
    private Boolean isAccountEnabled;

    @Column
    private Boolean isAccountLocked;

    @Column
    private Boolean isAccountExpired;

    @Column
    private Boolean isCredentialsExpired;

    @MapKeyEnumerated(EnumType.STRING)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<UserRole, UserRoleDef> userRoleDefs = new EnumMap<>(UserRole.class);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Map.Entry<UserRole, UserRoleDef> userExtensionEntry : userRoleDefs.entrySet()) {
            if (userExtensionEntry.getValue() != null) {
                grantedAuthorities.add(new SimpleGrantedAuthority(userExtensionEntry.getKey().getAuthority()));
            }
        }
        return grantedAuthorities;
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