package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.UsState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto implements AbstractDto {
    private Long id;
    private String street;
    private String city;
    private UsState usState;
    private String zipcode;
}
