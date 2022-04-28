package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * {@inheritDoc}
 * The User class contains the fields relevant for Spring Security and also acts as the "bucket"
 * for instances of {@link UserRoleDef} with one-to-one relationships with User.
 */
@Getter
@Setter
@Entity
public class User extends AbstractEntity implements UserDetails {

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String confirmPassword;

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

    @MapKey(name = "userRole")
    @MapKeyEnumerated(EnumType.ORDINAL)
    @OneToMany(targetEntity = UserRoleDef.class, mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Map<UserRole, UserRoleDef> userRoleDefs = new EnumMap<>(UserRole.class);

    @Override
    public Set<UserRole> getAuthorities() {
        return new HashSet<>(userRoleDefs.keySet());
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