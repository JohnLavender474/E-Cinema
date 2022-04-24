package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.ScreeningSeat;
import com.ecinema.app.entities.Showroom;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.repositories.ShowroomSeatRepository;
import com.ecinema.app.services.ShowroomSeatService;
import com.ecinema.app.utils.constants.Letter;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShowroomSeatServiceImpl extends AbstractServiceImpl<ShowroomSeat, ShowroomSeatRepository>
        implements ShowroomSeatService {

    public ShowroomSeatServiceImpl(ShowroomSeatRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(ShowroomSeat showroomSeat) {

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

}
