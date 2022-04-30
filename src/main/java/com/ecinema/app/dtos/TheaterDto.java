package com.ecinema.app.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TheaterDto implements Serializable {
    private Long id;
    private String theaterName;
    private Integer theaterNumber;
    private AddressDto addressDTO;
}
