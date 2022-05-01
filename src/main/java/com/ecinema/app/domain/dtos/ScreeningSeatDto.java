package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreeningSeatDto extends SeatDto {
    private Long screeningId;
    private Boolean isBooked;
}
