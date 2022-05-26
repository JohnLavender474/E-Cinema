package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.TicketRefundRequestDto;
import com.ecinema.app.domain.entities.TicketRefundRequest;
import com.ecinema.app.repositories.TicketRefundRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketRefundRequestService extends AbstractEntityService<
        TicketRefundRequest, TicketRefundRequestRepository, TicketRefundRequestDto> {

    public TicketRefundRequestService(TicketRefundRequestRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(TicketRefundRequest entity) {

    }

    @Override
    protected TicketRefundRequestDto convertToDto(TicketRefundRequest ticketRefundRequest) {
        return null;
    }

}
