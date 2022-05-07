package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowroomSeatDto extends SeatDto {
    private Long showroomId;
    private Letter showroomLetter;
}
