package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.services.TicketService;
import com.ecinema.app.domain.enums.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl extends AbstractServiceImpl<Ticket, TicketRepository>
        implements TicketService {

    public TicketServiceImpl(TicketRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Ticket ticket) {
        // detach ScreeningSeat
        ScreeningSeat screeningSeat = ticket.getScreeningSeat();
        if (screeningSeat != null) {
            screeningSeat.setTicket(null);
            ticket.setScreeningSeat(null);
        }
        // detach CustomerRoleDef
        CustomerRoleDef customerRoleDef = ticket.getCustomerRoleDef();
        if (customerRoleDef != null) {
            customerRoleDef.getTickets().remove(ticket);
            ticket.setCustomerRoleDef(null);
        }
    }

    @Override
    public List<Ticket> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime) {
        return repository.findAllByCreationDateTimeLessThanEqual(localDateTime);
    }

    @Override
    public List<Ticket> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime) {
        return repository.findAllByCreationDateTimeGreaterThanEqual(localDateTime);
    }

    @Override
    public List<Ticket> findAllByTicketStatus(TicketStatus ticketStatus) {
        return repository.findAllByTicketStatus(ticketStatus);
    }

}
