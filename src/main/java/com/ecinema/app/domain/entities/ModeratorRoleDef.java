package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.UserRole;
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
public class ModeratorRoleDef extends UserRoleDef {

    @ToString.Exclude
    @OneToMany(mappedBy = "censoredBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CustomerRoleDef> censoredCustomers = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.MODERATOR;
    }

}
