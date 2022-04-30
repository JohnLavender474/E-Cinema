package com.ecinema.app.services.implementations;

import com.ecinema.app.dtos.ShowroomSeatDto;
import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.Showroom;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.repositories.ShowroomSeatRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ShowroomSeatService;
import com.ecinema.app.utils.Letter;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShowroomSeatServiceImpl extends AbstractServiceImpl<ShowroomSeat, ShowroomSeatRepository>
        implements ShowroomSeatService {

    private final ScreeningSeatService screeningSeatService;

    public ShowroomSeatServiceImpl(ShowroomSeatRepository repository, ScreeningSeatService screeningSeatService) {
        super(repository);
        this.screeningSeatService = screeningSeatService;
    }

    @Override
    protected void onDelete(ShowroomSeat showroomSeat) {
        // detach Showroom
        Showroom showroom = showroomSeat.getShowroom();
        if (showroom != null) {
            showroom.getShowroomSeats().remove(showroomSeat);
            showroomSeat.setShowroom(null);
        }
        // cascade delete ScreeningSeats
        screeningSeatService.deleteAll(showroomSeat.getScreeningSeats());
    }

    @Override
    public List<ShowroomSeat> findAllByShowroom(Showroom showroom) {
        return repository.findAllByShowroom(showroom);
    }

    @Override
    public List<ShowroomSeat> findAllByShowroomWithId(Long showroomId)
            throws NoEntityFoundException {
        return repository.findAllByShowroomWithId(showroomId);
    }

    @Override
    public List<ShowroomSeat> findAllByShowroomAndRowLetter(Showroom showroom, Letter rowLetter) {
        return repository.findAllByShowroomAndRowLetter(showroom, rowLetter);
    }

    @Override
    public List<ShowroomSeat> findAllByShowroomWithIdAndRowLetter(Long showroomId, Letter rowLetter)
            throws NoEntityFoundException {
        return repository.findAllByShowroomWithIdAndRowLetter(showroomId, rowLetter);
    }

    @Override
    public Optional<ShowroomSeat> findByScreeningSeatsContains(ScreeningSeat screeningSeat) {
        return repository.findByScreeningSeatsContains(screeningSeat);
    }

    @Override
    public Optional<ShowroomSeat> findByScreeningSeatsContainsWithId(Long screeningSeatId)
            throws NoEntityFoundException {
        return repository.findByScreeningSeatsContainsWithId(screeningSeatId);
    }

    @Override
    public Optional<ShowroomSeat> findByShowroomAndRowLetterAndSeatNumber(Showroom showroom, Letter rowLetter,
                                                                          Integer seatNumber) {
        return repository.findByShowroomAndRowLetterAndSeatNumber(showroom, rowLetter, seatNumber);
    }

    @Override
    public Optional<ShowroomSeat> findByShowroomWithIdAndRowLetterAndSeatNumber(Long showroomId, Letter rowLetter,
                                                                                Integer seatNumber)
            throws NoEntityFoundException {
        return repository.findByShowroomWithIdAndRowLetterAndSeatNumber(showroomId, rowLetter, seatNumber);
    }

    @Override
    public ShowroomSeatDto convert(Long entityId)
            throws NoEntityFoundException {
        ShowroomSeat showroomSeat = findById(entityId).orElseThrow(
                () -> new NoEntityFoundException("showroom seat", "id", entityId));
        ShowroomSeatDto showroomSeatDTO = new ShowroomSeatDto();
        showroomSeatDTO.setId(showroomSeat.getId());
        showroomSeatDTO.setRowLetter(showroomSeat.getRowLetter());
        showroomSeatDTO.setSeatNumber(showroomSeat.getSeatNumber());
        showroomSeatDTO.setShowroomId(showroomSeat.getShowroom().getId());
        showroomSeatDTO.setShowroomLetter(showroomSeat.getShowroom().getShowroomLetter());
        return showroomSeatDTO;
    }

}
