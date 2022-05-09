package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.enums.TicketType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TicketDto implements AbstractDto {
    private String username;
    private String movieTitle;
    private Letter showroomLetter;
    private Letter rowLetter;
    private Integer seatNumber;
    private LocalDateTime showtime;
    private TicketType ticketType;
    private TicketStatus ticketStatus;
    private LocalDateTime creationDateTime;
}
