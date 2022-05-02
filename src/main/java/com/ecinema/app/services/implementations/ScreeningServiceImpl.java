package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ScreeningRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ScreeningService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Screening service.
 */
@Service
@Transactional
public class ScreeningServiceImpl extends AbstractServiceImpl<Screening, ScreeningRepository>
        implements ScreeningService {

    private final ScreeningSeatService screeningSeatService;

    /**
     * Instantiates a new Screening service.
     *
     * @param repository the repository
     */
    public ScreeningServiceImpl(ScreeningRepository repository,
                                ScreeningSeatService screeningSeatService) {
        super(repository);
        this.screeningSeatService = screeningSeatService;
    }

    @Override
    protected void onDelete(Screening screening) {
        // detach Movie
        Movie movie = screening.getMovie();
        if (movie != null) {
            movie.getScreenings().remove(screening);
            screening.setMovie(null);
        }
        // detach Showroom
        Showroom showroom = screening.getShowroom();
        if (showroom != null) {
            showroom.getScreenings().remove(screening);
            screening.setShowroom(null);
        }
        // cascade delete ScreeningSeats
        screeningSeatService.deleteAll(screening.getScreeningSeats());
    }

    @Override
    public Page<ScreeningDto> findPageByMovieId(Long movieId, Pageable pageable) {
        return repository.findAllByMovieId(movieId, pageable).map(this::convertToDto);
    }

    @Override
    public List<ScreeningDto> findAllByShowDateTimeLessThanEqual(LocalDateTime localDateTime) {
        return repository.findAllByShowDateTimeLessThanEqual(localDateTime)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> findAllByShowDateTimeGreaterThanEqual(LocalDateTime localDateTime) {
        return repository.findAllByShowDateTimeGreaterThanEqual(localDateTime)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> findAllByShowDateTimeBetween(LocalDateTime l1, LocalDateTime l2) {
        return repository.findAllByShowDateTimeBetween(l1, l2)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> findAllByMovie(Movie movie) {
        return repository.findAllByMovie(movie)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> findAllByMovieWithId(Long movieId) {
        return repository.findAllByMovieWithId(movieId)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> findAllByShowroom(Showroom showroom) {
        return repository.findAllByShowroom(showroom)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> findAllByShowroomWithId(Long showroomId) {
        return repository.findAllByShowroomWithId(showroomId)
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    @Override
    public ScreeningDto convertToDto(Long id)
            throws NoEntityFoundException {
        Screening screening = findById(id).orElseThrow(
                () -> new NoEntityFoundException("screening", "id", id));
        ScreeningDto screeningDTO = new ScreeningDto();
        screeningDTO.setId(screening.getId());
        screeningDTO.setMovieTitle(screening.getMovie().getTitle());
        screeningDTO.setShowroomLetter(screening.getShowroom().getShowroomLetter());
        screeningDTO.setShowDateTime(screening.getShowDateTime());
        screeningDTO.setTotalSeatsInRoom(screening.getShowroom().getShowroomSeats().size());
        long numberOfSeatsBooked = screening
                .getScreeningSeats().stream().filter(
                        screeningSeat -> screeningSeat.getTicket() != null).count();
        screeningDTO.setSeatsBooked((int) numberOfSeatsBooked);
        screeningDTO.setSeatsAvailable(screeningDTO.getTotalSeatsInRoom() - (int) numberOfSeatsBooked);
        return screeningDTO;
    }

}
