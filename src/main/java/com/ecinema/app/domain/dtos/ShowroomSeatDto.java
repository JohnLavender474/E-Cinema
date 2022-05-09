package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ShowroomSeatDto extends SeatDto {
    private Long showroomId;
    private Letter showroomLetter;
}
