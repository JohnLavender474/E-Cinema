package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.enums.TicketType;
import com.ecinema.app.domain.objects.SeatDesignation;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TicketDto extends AbstractDto {
    private String username;
    private String movieTitle;
    private Boolean isRefundable;
    private LocalDateTime endtime;
    private TicketType ticketType;
    private Letter showroomLetter;
    private LocalDateTime showtime;
    private TicketStatus ticketStatus;
    private LocalDateTime creationDateTime;
    private SeatDesignation seatDesignation;
}
