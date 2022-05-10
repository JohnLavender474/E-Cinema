package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@inheritDoc}
 * The User class contains the fields relevant for Spring Security and also acts as the "bucket"
 * for instances of {@link UserRoleDef} with one-to-one relationships with User.
 */
@Getter
@Setter
@Entity
@ToString
public class User extends AbstractEntity implements UserDetails {

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

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

    @ToString.Exclude
    @MapKey(name = "userRole")
    @MapKeyEnumerated(EnumType.ORDINAL)
    @OneToMany(targetEntity = UserRoleDef.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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