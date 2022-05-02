package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.ISeat;
import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDto implements AbstractDto, ISeat {
    private Long id;
    private Letter rowLetter;
    private Integer seatNumber;
}
