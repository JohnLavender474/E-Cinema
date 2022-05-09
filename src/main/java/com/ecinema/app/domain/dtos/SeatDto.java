package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.ISeat;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SeatDto implements AbstractDto, ISeat {
    private Long id;
    private Letter rowLetter;
    private Integer seatNumber;
}
