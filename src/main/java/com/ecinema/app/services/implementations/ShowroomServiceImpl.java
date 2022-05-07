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
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.validators.ShowroomValidator;
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
    public Map<Letter, Set<ShowroomSeatDto>> findShowroomSeatMapByShowroomWithId(Long showroomId) {
        return showroomSeatService.findShowroomSeatMapByShowroomWithId(showroomId);
    }

    @Override
    public void submitShowroomForm(ShowroomForm showroomForm)
            throws InvalidArgsException {
        List<String> errors = new ArrayList<>();
        showroomValidator.validate(showroomForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        logger.debug("Submit Showroom Form: passed validation checks");
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(showroomForm.getShowroomLetter());
        showroom.setNumberOfRows(showroomForm.getNumberOfRows());
        showroom.setNumberOfSeatsPerRow(showroomForm.getNumberOfSeatsPerRow());
        save(showroom);
        logger.debug("Instantiated and saved showroom " + showroom.getShowroomLetter());
        for (int i = 0; i < showroomForm.getNumberOfRows(); i++) {
            for (int j = 0; j < showroomForm.getNumberOfSeatsPerRow(); j++) {
                Letter rowLetter = Letter.values()[i];
                ShowroomSeat showroomSeat = new ShowroomSeat();
                showroomSeat.setRowLetter(rowLetter);
                showroomSeat.setSeatNumber(j);
                showroomSeat.setShowroom(showroom);
                showroom.getShowroomSeats().add(showroomSeat);
                showroomSeatService.save(showroomSeat);
                logger.debug("Instantiated and saved showroom seat " +
                                     showroomSeat.getRowLetter() + "-" +
                                     showroomSeat.getSeatNumber());
            }
        }
    }

    @Override
    public ShowroomDto findByShowroomLetter(Letter showroomLetter) {
        Showroom showroom = repository.findByShowroomLetter(showroomLetter).orElseThrow(
                () -> new NoEntityFoundException("showroom", "room letter", showroomLetter));
        return convertToDto(showroom);
    }

    @Override
    public ShowroomDto findByShowroomSeatsContains(ShowroomSeat showroomSeat) {
        Showroom showroom = repository.findByShowroomSeatsContains(showroomSeat)
                .orElseThrow( () -> new NoEntityFoundException(
                        "showroom", "showroom seat", showroomSeat));
        return convertToDto(showroom);
    }

    @Override
    public ShowroomDto findByShowroomSeatsContainsWithId(Long showroomSeatId) {
        Showroom showroom = repository.findByShowroomSeatsContainsWithId(showroomSeatId)
                .orElseThrow(() -> new NoEntityFoundException(
                        "showroom", "showroom seat with id", showroomSeatId));
        return convertToDto(showroom);
    }

    @Override
    public ShowroomDto findByScreeningsContains(Screening screening) {
        Showroom showroom = repository.findByScreeningsContains(screening).orElseThrow(
                () -> new NoEntityFoundException("showroom", "screening", screening));
        return convertToDto(showroom);
    }

    @Override
    public ShowroomDto findByScreeningsContainsWithId(Long screeningId) {
        Showroom showroom = repository.findByScreeningsContainsWithId(screeningId).orElseThrow(
                () -> new NoEntityFoundException("showroom", "screening with id", screeningId));
        return convertToDto(showroom);
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
