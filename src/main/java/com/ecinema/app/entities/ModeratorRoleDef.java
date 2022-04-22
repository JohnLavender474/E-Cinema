package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 * This class represents a {@link User} that has the role of {@link UserRole#MODERATOR} along with if privileges.
 * The moderator is able to censor {@link Review} instances (contained in {@link #censoredReviews}) which means that
 * the review is no longer publicly viewable until the moderator drops the censorship. The moderator is also able
 * to censor {@link CustomerRoleDef} instances (contained in {@link #censoredCustomers}) from posting new reviews.
 */
@Getter
@Setter
@Entity
public class ModeratorRoleDef extends UserRoleDef {

    @OneToMany(mappedBy = "censor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> censoredReviews = new HashSet<>();

    @OneToMany(mappedBy = "censoredBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CustomerRoleDef> censoredCustomers = new HashSet<>();

    @Override
    protected UserRole defineUserRole() {
        return UserRole.MODERATOR;
    }

}
