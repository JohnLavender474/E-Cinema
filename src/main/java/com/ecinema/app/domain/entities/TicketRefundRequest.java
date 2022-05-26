package com.ecinema.app.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
public class TicketRefundRequest extends AbstractEntity {

    @Column
    private Long ticketId;

    @Column(length = 2000)
    private String request;

}
