package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.dtos.ShowroomSeatDto;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.forms.ShowroomForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.services.ShowroomSeatService;
import com.ecinema.app.services.ShowroomService;
import com.ecinema.app.utils.Letter;
import com.ecinema.app.validators.ShowroomValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The type Showroom service.
 */
@Service
@Transactional
public class ShowroomServiceImpl extends AbstractServiceImpl<Showroom, ShowroomRepository>
        implements ShowroomService {

    private final ShowroomSeatService showroomSeatService;
    private final ScreeningService screeningService;
    private final ShowroomValidator showroomValidator;

    /**
     * Instantiates a new Showroom service.
     *
     * @param repository the repository
     */
    public ShowroomServiceImpl(ShowroomRepository repository, ShowroomSeatService showroomSeatService,
                               ScreeningService screeningService, ShowroomValidator showroomValidator) {
        super(repository);
        this.showroomSeatService = showroomSeatService;
        this.screeningService = screeningService;
        this.showroomValidator = showroomValidator;
    }

    @Override
    protected void onDelete(Showroom showroom) {
        // cascade delete ShowroomSeats
        showroomSeatService.deleteAll(showroom.getShowroomSeats());
        // cascade delete Screenings
        screeningService.deleteAll(showroom.getScreenings());
    }

    @Override
    public void submitShowroomForm(ShowroomForm showroomForm)
            throws InvalidArgsException {
        List<String> errors = new ArrayList<>();
        showroomValidator.validate(showroomForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        Showroom showroom = new Showroom();
        saveAndFlush(showroom);
        for (Map.Entry<Letter, Integer> entry : showroomForm.getSeatMap().entrySet()) {
            for (int i = 1; i <= entry.getValue(); i++) {
                ShowroomSeat showroomSeat = new ShowroomSeat();
                showroomSeat.setRowLetter(entry.getKey());
                showroomSeat.setSeatNumber(i);
                showroomSeat.setShowroom(showroom);
                showroom.getShowroomSeats().add(showroomSeat);
                showroomSeatService.save(showroomSeat);
            }
        }
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
    public ShowroomDto convertToDto(Long id)
            throws NoEntityFoundException {
        Showroom showroom = findById(id).orElseThrow(
                () -> new NoEntityFoundException("showroom", "id", id));
        ShowroomDto showroomDto = new ShowroomDto();
        showroomDto.setId(showroom.getId());
        showroomDto.setShowroomLetter(showroom.getShowroomLetter());
        return showroomDto;
    }

}
