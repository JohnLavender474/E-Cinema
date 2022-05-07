package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.ISeat;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDto implements AbstractDto, ISeat {
    private Long id;
    private Letter rowLetter;
    private Integer seatNumber;
}
