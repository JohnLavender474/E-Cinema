package com.ecinema.app.domain.forms;

import lombok.Data;

import java.io.Serializable;

@Data
public class TicketRefundForm implements Serializable {
    private Long ticketId;
    private String request;
}
