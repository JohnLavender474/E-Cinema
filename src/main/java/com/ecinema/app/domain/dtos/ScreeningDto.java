package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.domain.contracts.IScreening;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.util.UtilMethods;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ScreeningDto extends AbstractDto implements IScreening {

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
