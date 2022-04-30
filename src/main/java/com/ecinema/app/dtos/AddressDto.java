package com.ecinema.app.dtos;

import com.ecinema.app.utils.UsState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressDto implements Serializable {
    private Long id;
    private String street;
    private String city;
    private UsState usState;
    private String zipcode;
}
