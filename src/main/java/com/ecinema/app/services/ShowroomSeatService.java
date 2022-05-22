package com.ecinema.app.services;

import com.ecinema.app.domain.contracts.ISeat;
import com.ecinema.app.domain.dtos.ShowroomSeatDto;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.exceptions.InvalidAssociationException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.repositories.ShowroomSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ShowroomSeatService extends AbstractEntityService<ShowroomSeat, ShowroomSeatRepository, ShowroomSeatDto> {

    private final ScreeningSeatService screeningSeatService;

    public ShowroomSeatService(ShowroomSeatRepository repository, ScreeningSeatService screeningSeatService) {
        super(repository);
        this.screeningSeatService = screeningSeatService;
    }

    @Override
    public void onDelete(ShowroomSeat showroomSeat) {
        logger.debug("Showroom seat on delete");
        // detach Showroom
        Showroom showroom = showroomSeat.getShowroom();
        logger.debug("Detach showroom: " + showroom);
        if (showroom != null) {
            showroom.getShowroomSeats().remove(showroomSeat);
            showroomSeat.setShowroom(null);
        }
        // cascade delete ScreeningSeats
        logger.debug("Delete all associated screening seats");
        screeningSeatService.deleteAll(showroomSeat.getScreeningSeats());
    }

    @Override
    public ShowroomSeatDto convertToDto(ShowroomSeat showroomSeat) {
        ShowroomSeatDto showroomSeatDTO = new ShowroomSeatDto();
        showroomSeatDTO.setId(showroomSeat.getId());
        showroomSeatDTO.setRowLetter(showroomSeat.getRowLetter());
        showroomSeatDTO.setSeatNumber(showroomSeat.getSeatNumber());
        showroomSeatDTO.setShowroomId(showroomSeat.getShowroom().getId());
        showroomSeatDTO.setShowroomLetter(showroomSeat.getShowroom().getShowroomLetter());
        logger.debug("Convereted showroom seat to DTO: " + showroomSeatDTO);
        logger.debug("Showroom seat: " + showroomSeat);
        return showroomSeatDTO;
    }

    public Map<Letter, Set<ShowroomSeatDto>> findShowroomSeatMapByShowroomWithId(Long showroomId)
            throws InvalidAssociationException {
        List<ShowroomSeatDto> showroomSeatDtos = findAllByShowroomWithId(showroomId);
        if (showroomSeatDtos.isEmpty()) {
            throw new InvalidAssociationException("No showroom seats mapped to showroom with id " + showroomId);
        }
        Map<Letter, Set<ShowroomSeatDto>> showroomSeatMap = new EnumMap<>(Letter.class);
        for (ShowroomSeatDto showroomSeatDto : showroomSeatDtos) {
            showroomSeatMap.putIfAbsent(showroomSeatDto.getShowroomLetter(),
                                        new TreeSet<>(ISeat.SeatComparator.getInstance()));
            showroomSeatMap.get(showroomSeatDto.getRowLetter()).add(showroomSeatDto);
        }
        return showroomSeatMap;
    }

    public List<ShowroomSeatDto> findAllByShowroomWithId(Long showroomId)
            throws NoEntityFoundException {
        return sortAndConvert(repository.findAllByShowroomWithId(showroomId));
    }

    private List<ShowroomSeatDto> sortAndConvert(List<ShowroomSeat> showroomSeats) {
        showroomSeats.sort(ISeat.SeatComparator.getInstance());
        return convertToDto(showroomSeats);
    }

}
