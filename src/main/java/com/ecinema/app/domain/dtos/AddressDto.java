package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.UsState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressDto implements AbstractDto {
    private Long id;
    private String street;
    private String city;
    private UsState usState;
    private String zipcode;
}
