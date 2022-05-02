package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheaterDto implements AbstractDto {
    private Long id;
    private String theaterName;
    private AddressDto addressDTO;
}
