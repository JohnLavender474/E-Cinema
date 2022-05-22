package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IAddress;
import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.domain.enums.UsState;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AddressDto extends AbstractDto implements IAddress {

    private String street;
    private String city;
    private UsState usState;
    private String zipcode;

    public AddressDto(IAddress address) {
        set(address);
    }

}
