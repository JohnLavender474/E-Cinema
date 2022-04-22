package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Ticket;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.services.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketServiceImpl extends AbstractServiceImpl<Ticket, TicketRepository>
        implements TicketService {

    public TicketServiceImpl(TicketRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Ticket ticket) {

    }

}
