package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TicketForm implements Serializable {
    private TicketType ticketType;
    private Long screeningSeatId;
    private Long userId;
}
