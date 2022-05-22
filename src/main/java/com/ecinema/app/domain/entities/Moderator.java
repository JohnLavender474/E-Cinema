package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserAuthority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Moderator role def.
 */
@Getter
@Setter
@Entity
@ToString
public class Moderator extends AbstractUserAuthority {

    @ToString.Exclude
    @OneToMany(mappedBy = "censoredBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Customer> censoredCustomers = new HashSet<>();

    @Override
    protected UserAuthority defineUserRole() {
        return UserAuthority.MODERATOR;
    }

}
