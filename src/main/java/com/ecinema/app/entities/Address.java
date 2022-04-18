package com.ecinema.app.entities;


import com.ecinema.app.utils.constants.UsState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Address extends AbstractEntity {

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private UsState usState;

    @Column
    private String zipcode;

}
