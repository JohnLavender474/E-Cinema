package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketForm implements Serializable {
    private List<Long> couponIds = new ArrayList<>();
    private TicketType ticketType = TicketType.ADULT;
    private Long screeningSeatId = 0L;
    private Long userId = 0L;
}
