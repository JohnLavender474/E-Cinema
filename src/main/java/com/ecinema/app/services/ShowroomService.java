package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.forms.ShowroomForm;
import com.ecinema.app.domain.validators.ShowroomValidator;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.util.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShowroomService extends AbstractEntityService<Showroom, ShowroomRepository, ShowroomDto> {

    private final ShowroomSeatService showroomSeatService;
    private final ShowroomValidator showroomValidator;
    private final ScreeningService screeningService;

    public ShowroomService(ShowroomRepository repository, ShowroomSeatService showroomSeatService,
                           ScreeningService screeningService, ShowroomValidator showroomValidator) {
        super(repository);
        this.showroomSeatService = showroomSeatService;
        this.screeningService = screeningService;
        this.showroomValidator = showroomValidator;
    }

    @Override
    public void onDelete(Showroom showroom) {
        logger.debug("Showroom on delete");
        // cascade delete ShowroomSeats
        logger.debug("Delete all associated showroom seats");
        showroomSeatService.deleteAll(showroom.getShowroomSeats());
        // cascade delete Screenings
        logger.debug("Delete all associated screenings");
        screeningService.deleteAll(showroom.getScreenings());
    }

    @Override
    public ShowroomDto convertToDto(Showroom showroom) {
        ShowroomDto showroomDto = new ShowroomDto();
        showroomDto.setId(showroom.getId());
        showroomDto.setShowroomLetter(showroom.getShowroomLetter());
        showroomDto.setNumberOfRows(showroom.getNumberOfRows());
        showroomDto.setNumberOfSeatsPerRow(showroom.getNumberOfSeatsPerRow());
        logger.debug("Convert showroom to DTO: " + showroomDto);
        logger.debug("Showroom: " + showroom);
        return showroomDto;
    }

    public List<Letter> findAllShowroomLetters() {
        return repository.findAllShowroomLetters();
    }

    public void submitShowroomForm(ShowroomForm showroomForm)
            throws ClashException, InvalidArgsException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit showroom form");
        List<String> errors = new ArrayList<>();
        showroomValidator.validate(showroomForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        logger.debug("Submit Showroom Form: passed validation checks");
        if (repository.existsByShowroomLetter(showroomForm.getShowroomLetter())) {
            throw new ClashException("Showroom with room letter " +
                                             showroomForm.getShowroomLetter() + " already exists");
        }
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(showroomForm.getShowroomLetter());
        showroom.setNumberOfRows(showroomForm.getNumberOfRows());
        showroom.setNumberOfSeatsPerRow(showroomForm.getNumberOfSeatsPerRow());
        repository.save(showroom);
        logger.debug("Instantiated and saved showroom " + showroom);
        List<ShowroomSeat> showroomSeats = new ArrayList<>();
        for (int i = 0; i < showroomForm.getNumberOfRows(); i++) {
            Letter rowLetter = Letter.values()[i];
            for (int j = 1; j <= showroomForm.getNumberOfSeatsPerRow(); j++) {
                ShowroomSeat showroomSeat = new ShowroomSeat();
                showroomSeat.setRowLetter(rowLetter);
                showroomSeat.setSeatNumber(j);
                showroomSeat.setShowroom(showroom);
                showroom.getShowroomSeats().add(showroomSeat);
                showroomSeats.add(showroomSeat);
                logger.debug("Instantiated and queuing to save showroom seat " +
                                     showroomSeat.getRowLetter() + "-" +
                                     showroomSeat.getSeatNumber());
            }
        }
        showroomSeatService.saveAll(showroomSeats);
        logger.debug("Saved collection of showroom seats: " + showroomSeats);
    }

    public ShowroomDto findByShowroomLetter(Letter showroomLetter) {
        Showroom showroom = repository.findByShowroomLetter(showroomLetter).orElseThrow(
                () -> new NoEntityFoundException("showroom", "room letter", showroomLetter));
        return convertToDto(showroom);
    }

    public ShowroomDto findByShowroomSeatsContains(ShowroomSeat showroomSeat) {
        Showroom showroom = repository.findByShowroomSeatsContains(showroomSeat)
                                      .orElseThrow( () -> new NoEntityFoundException(
                                              "showroom", "showroom seat", showroomSeat));
        return convertToDto(showroom);
    }

}
