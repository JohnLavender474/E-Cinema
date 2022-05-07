package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IScreening;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScreeningDto implements AbstractDto, IScreening {
    private Long id;
    private Long movieId;
    private Long showroomId;
    private String movieTitle;
    private Letter showroomLetter;
    private Integer seatsBooked;
    private Integer seatsAvailable;
    private Integer totalSeatsInRoom;
    private LocalDateTime showtime;
    private LocalDateTime endtime;
}
