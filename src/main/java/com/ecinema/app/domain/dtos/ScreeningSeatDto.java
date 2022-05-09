package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ScreeningSeatDto extends SeatDto {
    private Long screeningId = 0L;
    private Boolean isBooked = false;
}
