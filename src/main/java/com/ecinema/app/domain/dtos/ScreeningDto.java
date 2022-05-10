package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IScreening;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.utils.UtilMethods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.print.attribute.standard.MediaSize.ISO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LocalDateTime showtime;
    private LocalDateTime endtime;

    public String showtimeFormatted() {
        return UtilMethods.localDateTimeFormatted(showtime);
    }

    public String endtimeFormatted() {
        return UtilMethods.localDateTimeFormatted(endtime);
    }

}
