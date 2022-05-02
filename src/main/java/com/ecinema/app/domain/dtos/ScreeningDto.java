package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
public class ScreeningDto implements AbstractDto {
    private Long id;
    private String movieTitle;
    private Letter showroomLetter;
    private Integer seatsBooked;
    private Integer seatsAvailable;
    private Integer totalSeatsInRoom;
    private LocalDateTime showDateTime;
}
