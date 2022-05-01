package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TheaterDto implements AbstractDto {
    private Long id;
    private String theaterName;
    private Integer theaterNumber;
    private AddressDto addressDTO;
}
