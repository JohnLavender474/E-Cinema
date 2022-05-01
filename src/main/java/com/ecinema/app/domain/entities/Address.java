package com.ecinema.app.domain.entities;

import com.ecinema.app.utils.UsState;
import com.ecinema.app.utils.IAddress;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * {@inheritDoc}
 * Specifies the details of an address. Has no bidirectional mapping with any entity.
 */
@Getter
@Setter
@Entity
public class Address extends AbstractEntity implements IAddress {

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private UsState usState;

    @Column
    private String zipcode;

}
