package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Screening;
import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Ticket;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The type Screening seat service.
 */
@Service
@Transactional
public class ScreeningSeatServiceImpl extends AbstractServiceImpl<ScreeningSeat, ScreeningSeatRepository>
        implements ScreeningSeatService {

    private final TicketService ticketService;

    /**
     * Instantiates a new Screening seat service.
     *
     * @param repository the repository
     */
    public ScreeningSeatServiceImpl(ScreeningSeatRepository repository, TicketService ticketService) {
        super(repository);
        this.ticketService = ticketService;
    }

    @Override
    protected void onDelete(ScreeningSeat screeningSeat) {
        // cascade delete Ticket
        ticketService.delete(screeningSeat.getTicket());
        // detach Screening
        Screening screening = screeningSeat.getScreening();
        if (screening != null) {
            screening.getScreeningSeats().remove(screeningSeat);
            screeningSeat.setScreening(null);
        }
        // detach ShowroomSeat
        ShowroomSeat showroomSeat = screeningSeat.getShowroomSeat();
        if (showroomSeat != null) {
            showroomSeat.getScreeningSeats().remove(screeningSeat);
            screeningSeat.setShowroomSeat(null);
        }
    }

    @Override
    public List<ScreeningSeat> findAllByScreening(Screening screening) {
        return repository.findAllByScreening(screening);
    }

    @Override
    public List<ScreeningSeat> findAllByScreeningWithId(Long screeningId) {
        return repository.findAllByScreeningWithId(screeningId);
    }

    @Override
    public List<ScreeningSeat> findAllByShowroomSeat(ShowroomSeat showroomSeat) {
        return repository.findAllByShowroomSeat(showroomSeat);
    }

    @Override
    public List<ScreeningSeat> findAllByShowroomSeatWithId(Long showroomSeatId) {
        return repository.findAllByShowroomSeatWithId(showroomSeatId);
    }

    @Override
    public Optional<ScreeningSeat> findByTicket(Ticket ticket) {
        return repository.findByTicket(ticket);
    }

    @Override
    public Optional<ScreeningSeat> findByTicketWithId(Long ticketId) {
        return repository.findByTicketWithId(ticketId);
    }

}
