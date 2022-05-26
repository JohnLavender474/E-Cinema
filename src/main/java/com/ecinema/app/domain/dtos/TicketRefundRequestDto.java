package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TicketRefundRequestDto extends AbstractDto {
    private Long ticketId;
    private String request;
}
