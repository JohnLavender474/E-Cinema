package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class ModeratorRoleDef extends UserRoleDef {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Review> censoredReviews = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<User> censoredUsers = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.MODERATOR;
    }

}
