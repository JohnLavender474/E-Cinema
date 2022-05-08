package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.TicketDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.domain.forms.TicketForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.repositories.TicketRepository;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.TicketService;
import com.ecinema.app.domain.enums.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Ticket service.
 */
@Service
@Transactional
public class TicketServiceImpl extends AbstractServiceImpl<Ticket, TicketRepository>
        implements TicketService {

    private final UserRepository userRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    /**
     * Instantiates a new Ticket service.
     *
     * @param repository the repository
     */
    public TicketServiceImpl(TicketRepository repository,
                             UserRepository userRepository,
                             ScreeningSeatRepository screeningSeatRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.screeningSeatRepository = screeningSeatRepository;
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
    public void submitTicketForm(TicketForm ticketForm)
            throws NoEntityFoundException, ClashException, InvalidAssociationException {
        ScreeningSeat screeningSeat = screeningSeatRepository
                .findById(ticketForm.getScreeningSeatId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "screening seat", "id", ticketForm.getScreeningSeatId()));
        if (screeningSeat.getTicket() != null) {
            throw new ClashException("Ticket for screening seat already exists");
        }
        User user = userRepository.findById(ticketForm.getUserId()).orElseThrow(
                () -> new NoEntityFoundException("user", "id", ticketForm.getUserId()));
        if (!user.getUserRoleDefs().containsKey(UserRole.CUSTOMER)) {
            throw new InvalidAssociationException("User does not have customer authority and as " +
                                                          "a result cannot book tickets");
        }
        CustomerRoleDef customerRoleDef = (CustomerRoleDef) user.getUserRoleDefs().get(UserRole.CUSTOMER);
        Ticket ticket = new Ticket();
        ticket.setCreationDateTime(LocalDateTime.now());
        ticket.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getTickets().add(ticket);
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        ticket.setTicketStatus(TicketStatus.VALID);
        ticket.setTicketType(ticketForm.getTicketType());
        save(ticket);
    }

    @Override
    public List<TicketDto> findAllByCreationDateTimeLessThanEqual(LocalDateTime localDateTime) {
        return convertToDto(repository.findAllByCreationDateTimeLessThanEqual(localDateTime));
    }

    @Override
    public List<TicketDto> findAllByCreationDateTimeGreaterThanEqual(LocalDateTime localDateTime) {
        return convertToDto(repository.findAllByCreationDateTimeGreaterThanEqual(localDateTime));
    }

    @Override
    public List<TicketDto> findAllByTicketStatus(TicketStatus ticketStatus) {
        return convertToDto(repository.findAllByTicketStatus(ticketStatus));
    }

    @Override
    public TicketDto convertToDto(Long id)
            throws NoEntityFoundException {
        Ticket ticket = findById(id).orElseThrow(
                () -> new NoEntityFoundException("ticket", "id", id));
        String username = ticket.getCustomerRoleDef().getUser().getUsername();
        String movieTitle = ticket.getScreeningSeat().getScreening().getMovie().getTitle();
        Letter showroomLetter = ticket.getScreeningSeat().getScreening().getShowroom().getShowroomLetter();
        LocalDateTime showtime = ticket.getScreeningSeat().getScreening().getShowDateTime();
        ShowroomSeat showroomSeat = ticket.getScreeningSeat().getShowroomSeat();
        TicketDto ticketDto = new TicketDto();
        ticketDto.setUsername(username);
        ticketDto.setShowtime(showtime);
        ticketDto.setMovieTitle(movieTitle);
        ticketDto.setShowroomLetter(showroomLetter);
        ticketDto.setTicketType(ticket.getTicketType());
        ticketDto.setTicketStatus(ticket.getTicketStatus());
        ticketDto.setRowLetter(showroomSeat.getRowLetter());
        ticketDto.setSeatNumber(showroomSeat.getSeatNumber());
        ticketDto.setCreationDateTime(ticket.getCreationDateTime());
        return ticketDto;
    }

}
