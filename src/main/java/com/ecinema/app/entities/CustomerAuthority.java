package com.ecinema.app.entities;

import com.ecinema.app.utils.constants.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class CustomerAuthority extends UserAuthority {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Ticket> tickets;

    @Override
    protected UserRole defineUserRole() {
        return UserRole.CUSTOMER;
    }

}
