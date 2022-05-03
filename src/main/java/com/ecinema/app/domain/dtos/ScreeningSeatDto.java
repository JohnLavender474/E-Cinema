package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreeningSeatDto extends SeatDto {
    private Long screeningId = 0L;
    private Boolean isBooked = false;
}
