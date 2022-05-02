package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Moderator role def.
 */
@Getter
@Setter
@Entity
public class ModeratorRoleDef extends UserRoleDef {

    @OneToMany(mappedBy = "censoredBy", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<CustomerRoleDef> censoredCustomers = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.MODERATOR;
    }

}
