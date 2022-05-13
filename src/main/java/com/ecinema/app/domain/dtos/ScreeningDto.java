package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IScreening;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.utils.UtilMethods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ScreeningDto implements AbstractDto, IScreening {

    private Long id;
    private Long movieId;
    private Long showroomId;
    private String movieTitle;
    private Letter showroomLetter;
    private Integer seatsBooked;
    private Integer seatsAvailable;
    private Integer totalSeatsInRoom;
    private LocalDateTime showDateTime;
    private LocalDateTime endDateTime;

    public String showDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(showDateTime);
    }

    public String endDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(endDateTime);
    }

}
