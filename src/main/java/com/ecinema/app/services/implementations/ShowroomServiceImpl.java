package com.ecinema.app.services.implementations;

import com.ecinema.app.dtos.ShowroomDto;
import com.ecinema.app.dtos.ShowroomSeatDto;
import com.ecinema.app.entities.Screening;
import com.ecinema.app.entities.Showroom;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.services.ShowroomSeatService;
import com.ecinema.app.services.ShowroomService;
import com.ecinema.app.utils.Letter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * The type Showroom service.
 */
@Service
@Transactional
public class ShowroomServiceImpl extends AbstractServiceImpl<Showroom, ShowroomRepository>
        implements ShowroomService {

    private final ShowroomSeatService showroomSeatService;
    private final ScreeningService screeningService;

    /**
     * Instantiates a new Showroom service.
     *
     * @param repository the repository
     */
    public ShowroomServiceImpl(ShowroomRepository repository, ShowroomSeatService showroomSeatService,
                               ScreeningService screeningService) {
        super(repository);
        this.showroomSeatService = showroomSeatService;
        this.screeningService = screeningService;
    }

    @Override
    protected void onDelete(Showroom showroom) {
        // detach Theater
        Theater theater = showroom.getTheater();
        theater.getShowrooms().remove(showroom.getShowroomLetter());
        showroom.setTheater(null);
        // cascade delete ShowroomSeats
        showroomSeatService.deleteAll(showroom.getShowroomSeats());
        // cascade delete Screenings
        screeningService.deleteAll(showroom.getScreenings());
    }

    @Override
    public Optional<Showroom> findByShowroomLetter(Letter showroomLetter) {
        return repository.findByShowroomLetter(showroomLetter);
    }

    @Override
    public Optional<Showroom> findByShowroomSeatsContains(ShowroomSeat showroomSeat) {
        return repository.findByShowroomSeatsContains(showroomSeat);
    }

    @Override
    public Optional<Showroom> findByShowroomSeatsContainsWithId(Long showroomSeatId) {
        return repository.findByShowroomSeatsContainsWithId(showroomSeatId);
    }

    @Override
    public Optional<Showroom> findByScreeningsContains(Screening screening) {
        return repository.findByScreeningsContains(screening);
    }

    @Override
    public Optional<Showroom> findByScreeningsContainsWithId(Long screeningId) {
        return repository.findByScreeningsContainsWithId(screeningId);
    }

    @Override
    public List<Showroom> findAllByTheater(Theater theater) {
        return repository.findAllByTheater(theater);
    }

    @Override
    public List<Showroom> findAllByTheaterWithId(Long theaterId) {
        return repository.findAllByTheaterWithId(theaterId);
    }

    @Override
    public ShowroomDto convert(Long entityId)
            throws NoEntityFoundException {
        Showroom showroom = findById(entityId).orElseThrow(
                () -> new NoEntityFoundException("showroom", "id", entityId));
        ShowroomDto showroomDto = new ShowroomDto();
        showroomDto.setId(showroom.getId());
        showroomDto.setShowroomLetter(showroom.getShowroomLetter());
        Set<ShowroomSeatDto> showroomSeatDtos = new TreeSet<>();
        for (ShowroomSeat showroomSeat : showroom.getShowroomSeats()) {
            ShowroomSeatDto showroomSeatDto = showroomSeatService
                    .convert(showroomSeat.getId());
            showroomSeatDtos.add(showroomSeatDto);
        }
        showroomDto.setShowroomSeatDTOs(showroomSeatDtos);
        return showroomDto;
    }

}
