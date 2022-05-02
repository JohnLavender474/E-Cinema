package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.entities.Ticket;
import com.ecinema.app.exceptions.NoAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ScreeningSeatRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.TicketService;
import com.ecinema.app.utils.ISeat;
import com.ecinema.app.utils.Letter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Map<Letter, Set<ScreeningSeatDto>> getMapOfScreeningSeatsForScreeningWithId(Long screeningId)
            throws NoAssociationException {
        List<ScreeningSeatDto> screeningSeatDtos = findAllByScreeningWithId(screeningId);
        if (screeningSeatDtos.isEmpty()) {
            throw new NoAssociationException("No screening seats mapped to screening with id " + screeningId);
        }
        Map<Letter, Set<ScreeningSeatDto>> mapOfScreeningSeats = new TreeMap<>();
        for (ScreeningSeatDto screeningSeatDto : screeningSeatDtos) {
            mapOfScreeningSeats.putIfAbsent(screeningSeatDto.getRowLetter(), new TreeSet<>(ISeat.comparator));
            mapOfScreeningSeats.get(screeningSeatDto.getRowLetter()).add(screeningSeatDto);
        }
        return mapOfScreeningSeats;
    }

    @Override
    public List<ScreeningSeatDto> findAllByScreening(Screening screening) {
        return sortAndConvert(repository.findAllByScreening(screening));
    }

    @Override
    public List<ScreeningSeatDto> findAllByScreeningWithId(Long screeningId) {
        return sortAndConvert(repository.findAllByScreeningWithId(screeningId));
    }

    @Override
    public List<ScreeningSeatDto> findAllByShowroomSeat(ShowroomSeat showroomSeat) {
        return sortAndConvert(repository.findAllByShowroomSeat(showroomSeat));
    }

    @Override
    public List<ScreeningSeatDto> findAllByShowroomSeatWithId(Long showroomSeatId) {
        return sortAndConvert(repository.findAllByShowroomSeatWithId(showroomSeatId));
    }

    @Override
    public ScreeningSeatDto findByTicket(Ticket ticket) {
        return findByTicketWithId(ticket.getId());
    }

    @Override
    public ScreeningSeatDto findByTicketWithId(Long ticketId) {
        ScreeningSeat screeningSeat = repository.findByTicketWithId(ticketId).orElseThrow(
                () -> new NoEntityFoundException("screening seat", "ticket id", ticketId));
        return convertToDto(screeningSeat);
    }

    @Override
    public ScreeningSeatDto convertToDto(Long id)
            throws NoEntityFoundException {
        ScreeningSeat screeningSeat = findById(id).orElseThrow(
                () -> new NoEntityFoundException("screening seat", "id", id));
        ScreeningSeatDto screeningSeatDTO = new ScreeningSeatDto();
        screeningSeatDTO.setId(screeningSeat.getId());
        screeningSeatDTO.setRowLetter(screeningSeat.getShowroomSeat().getRowLetter());
        screeningSeatDTO.setSeatNumber(screeningSeat.getShowroomSeat().getSeatNumber());
        screeningSeatDTO.setIsBooked(screeningSeat.getTicket() != null);
        screeningSeatDTO.setScreeningId(screeningSeat.getScreening().getId());
        return screeningSeatDTO;
    }

    private List<ScreeningSeatDto> sortAndConvert(List<ScreeningSeat> screeningSeats) {
        screeningSeats.sort(ISeat.comparator);
        return convertToDto(screeningSeats);
    }

}
