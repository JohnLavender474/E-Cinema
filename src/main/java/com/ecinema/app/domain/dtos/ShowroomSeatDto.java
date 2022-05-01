package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowroomSeatDto extends SeatDto {
    private Long showroomId;
    private Letter showroomLetter;
}
