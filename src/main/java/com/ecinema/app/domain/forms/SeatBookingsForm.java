package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.enums.TicketType;
import com.ecinema.app.domain.objects.SeatBooking;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class SeatBookingsForm implements Serializable {

    private Long customerId;
    private Long paymentCardId;
    private List<SeatBooking> seatBookings = new ArrayList<>();

    public void setSeatIds(Collection<Long> seatIds) {
        seatIds.forEach(id -> seatBookings.add(
                new SeatBooking(id, TicketType.ADULT)));
    }

}
