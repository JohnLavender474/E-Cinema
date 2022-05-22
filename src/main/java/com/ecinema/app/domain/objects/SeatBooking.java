package com.ecinema.app.domain.objects;

import com.ecinema.app.domain.enums.TicketType;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatBooking implements Serializable {

    private Long seatId;
    private TicketType ticketType;

    @Override
    public boolean equals(Object o) {
        return o instanceof SeatBooking seatBooking &&
                seatBooking.seatId.equals(seatId);
    }

}
