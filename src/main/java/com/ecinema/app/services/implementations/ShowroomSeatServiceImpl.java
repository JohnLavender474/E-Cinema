package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.ShowroomSeatDto;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.exceptions.NoAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ShowroomSeatRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ShowroomSeatService;
import com.ecinema.app.utils.ISeat;
import com.ecinema.app.utils.Letter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Map<Letter, Set<ShowroomSeatDto>> findShowroomSeatMapByShowroomWithId(Long showroomId)
            throws NoAssociationException {
        List<ShowroomSeatDto> showroomSeatDtos = findAllByShowroomWithId(showroomId);
        if (showroomSeatDtos.isEmpty()) {
            throw new NoAssociationException("No showroom seats mapped to showroom with id " + showroomId);
        }
        Map<Letter, Set<ShowroomSeatDto>> showroomSeatMap = new EnumMap<>(Letter.class);
        for (ShowroomSeatDto showroomSeatDto : showroomSeatDtos) {
            showroomSeatMap.putIfAbsent(showroomSeatDto.getShowroomLetter(), new TreeSet<>(ISeat.comparator));
            showroomSeatMap.get(showroomSeatDto.getRowLetter()).add(showroomSeatDto);
        }
        return showroomSeatMap;
    }

    @Override
    public List<ShowroomSeatDto> findAllByShowroom(Showroom showroom) {
        return sortAndConvert(repository.findAllByShowroom(showroom));
    }

    @Override
    public List<ShowroomSeatDto> findAllByShowroomWithId(Long showroomId)
            throws NoEntityFoundException {
        return sortAndConvert(repository.findAllByShowroomWithId(showroomId));
    }

    @Override
    public List<ShowroomSeatDto> findAllByShowroomAndRowLetter(Showroom showroom, Letter rowLetter) {
        return sortAndConvert(repository.findAllByShowroomAndRowLetter(
                showroom, rowLetter));
    }

    @Override
    public List<ShowroomSeatDto> findAllByShowroomWithIdAndRowLetter(Long showroomId, Letter rowLetter)
            throws NoEntityFoundException {
        return sortAndConvert(repository.findAllByShowroomWithIdAndRowLetter(
                showroomId, rowLetter));
    }

    @Override
    public ShowroomSeatDto findByScreeningSeatsContains(ScreeningSeat screeningSeat) {
        return findByScreeningSeatsContainsWithId(screeningSeat.getId());
    }

    @Override
    public ShowroomSeatDto findByScreeningSeatsContainsWithId(Long screeningSeatId)
            throws NoEntityFoundException {
        ShowroomSeat showroomSeat = repository.findByScreeningSeatsContainsWithId(screeningSeatId)
                                              .orElseThrow(() -> new NoEntityFoundException(
                                                      "showroom seat", "screening seat id", screeningSeatId));
        return convertToDto(showroomSeat);
    }

    @Override
    public ShowroomSeatDto findByShowroomAndRowLetterAndSeatNumber(
            Showroom showroom, Letter rowLetter, Integer seatNumber)
            throws NoEntityFoundException {
        return findByShowroomWithIdAndRowLetterAndSeatNumber(
                showroom.getId(), rowLetter, seatNumber);
    }

    @Override
    public ShowroomSeatDto findByShowroomWithIdAndRowLetterAndSeatNumber(
            Long showroomId, Letter rowLetter, Integer seatNumber)
            throws NoEntityFoundException {
        ShowroomSeat showroomSeat = repository.findByShowroomWithIdAndRowLetterAndSeatNumber(
                showroomId, rowLetter, seatNumber).orElseThrow(
                () -> new NoEntityFoundException(
                        "showroom seat", "showroom id and row letter and seat number",
                        List.of(showroomId, rowLetter, seatNumber)));
        return convertToDto(showroomSeat);
    }

    @Override
    public ShowroomSeatDto convertToDto(Long id)
            throws NoEntityFoundException {
        ShowroomSeat showroomSeat = findById(id).orElseThrow(
                () -> new NoEntityFoundException("showroom seat", "id", id));
        ShowroomSeatDto showroomSeatDTO = new ShowroomSeatDto();
        showroomSeatDTO.setId(showroomSeat.getId());
        showroomSeatDTO.setRowLetter(showroomSeat.getRowLetter());
        showroomSeatDTO.setSeatNumber(showroomSeat.getSeatNumber());
        showroomSeatDTO.setShowroomId(showroomSeat.getShowroom().getId());
        showroomSeatDTO.setShowroomLetter(showroomSeat.getShowroom().getShowroomLetter());
        return showroomSeatDTO;
    }

    private List<ShowroomSeatDto> sortAndConvert(List<ShowroomSeat> showroomSeats) {
        showroomSeats.sort(ISeat.comparator);
        return convertToDto(showroomSeats);
    }

}
