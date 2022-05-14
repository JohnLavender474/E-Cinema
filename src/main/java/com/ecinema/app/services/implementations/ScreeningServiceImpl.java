package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.forms.ScreeningForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ScreeningRepository;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.domain.validators.ScreeningValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Screening service.
 */
@Service
@Transactional
public class ScreeningServiceImpl extends AbstractServiceImpl<Screening, ScreeningRepository>
        implements ScreeningService {

    private final MovieRepository movieRepository;
    private final ShowroomRepository showroomRepository;
    private final ScreeningSeatService screeningSeatService;
    private final ScreeningValidator screeningValidator;

    /**
     * Instantiates a new Screening service.
     *
     * @param repository the repository
     */
    public ScreeningServiceImpl(ScreeningRepository repository,
                                MovieRepository movieRepository,
                                ShowroomRepository showroomRepository,
                                ScreeningSeatService screeningSeatService,
                                ScreeningValidator screeningValidator) {
        super(repository);
        this.movieRepository = movieRepository;
        this.showroomRepository = showroomRepository;
        this.screeningSeatService = screeningSeatService;
        this.screeningValidator = screeningValidator;
    }

    @Override
    protected void onDelete(Screening screening) {
        logger.debug("Screening on delete");
        // detach Movie
        Movie movie = screening.getMovie();
        logger.debug("Detach movie: " + movie);
        if (movie != null) {
            movie.getScreenings().remove(screening);
            screening.setMovie(null);
        }
        // detach Showroom
        Showroom showroom = screening.getShowroom();
        logger.debug("Detach showroom: " + showroom);
        if (showroom != null) {
            showroom.getScreenings().remove(screening);
            screening.setShowroom(null);
        }
        // cascade delete ScreeningSeats
        logger.debug("Delete all associated screening seats");
        screeningSeatService.deleteAll(screening.getScreeningSeats());
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        Screening screening = findById(id).orElseThrow(
                () -> new NoEntityFoundException("screening", "id", id));
        onDeleteInfo(screening, info);
    }

    @Override
    public void onDeleteInfo(Screening screening, Collection<String> info) {
        info.add("Screening at " + UtilMethods.localDateTimeFormatted(screening.getShowDateTime()) +
                         " in showroom " + screening.getShowroom().getShowroomLetter() +
                         " for \"" + screening.getMovie().getTitle() + "\" will be deleted");
        screening.getScreeningSeats().forEach(
                screeningSeat -> screeningSeatService.onDeleteInfo(screeningSeat, info));
    }

    @Override
    public int findNumberOfTicketsBooked(Screening screening) {
        return (int) screening.getScreeningSeats().stream().filter(ScreeningSeat::isBooked).count();
    }

    @Override
    public Map<Letter, Set<ScreeningSeatDto>> findScreeningSeatMapByScreeningWithId(Long screeningId) {
        return screeningSeatService.findScreeningSeatMapByScreeningWithId(screeningId);
    }

    @Override
    public void submitScreeningForm(ScreeningForm screeningForm)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Add new screening");
        List<String> errors = new ArrayList<>();
        screeningValidator.validate(screeningForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        Showroom showroom = showroomRepository
                .findById(screeningForm.getShowroomId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "showroom", "showroom id", screeningForm.getShowroomId()));
        Movie movie = movieRepository
                .findById(screeningForm.getMovieId())
                .orElseThrow(() -> new NoEntityFoundException(
                        "movie", "movie id", screeningForm.getMovieId()));
        LocalDateTime endDateTime = screeningForm.getShowDateTime()
                                                 .plusHours(movie.getDuration().getHours())
                                                 .plusMinutes(movie.getDuration().getMinutes());
        Optional<ScreeningDto> optionalOverlap = findScreeningByShowroomAndInBetweenStartTimeAndEndTime(
                showroom, screeningForm.getShowDateTime(), endDateTime);
        if (optionalOverlap.isPresent()) {
            ScreeningDto overlap = optionalOverlap.get();
            throw new ClashException("Screening for " + movie.getTitle() +
                                             " in showroom " + showroom.getShowroomLetter() +
                                             " at " + UtilMethods.localDateTimeFormatted(
                                                     screeningForm.getShowDateTime()) +
                                             " cannot be created because it overlaps" +
                                             " screening for " + overlap.getMovieTitle() +
                                             " in showroom " + overlap.getShowroomLetter() +
                                             " at " + UtilMethods.localDateTimeFormatted(
                                                     overlap.getShowDateTime()));
        }
        Screening screening = new Screening();
        screening.setShowDateTime(screeningForm.getShowDateTime());
        screening.setEndDateTime(endDateTime);
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screening.setMovie(movie);
        movie.getScreenings().add(screening);
        save(screening);
        for (ShowroomSeat showroomSeat : showroom.getShowroomSeats()) {
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setShowroomSeat(showroomSeat);
            showroomSeat.getScreeningSeats().add(screeningSeat);
            screeningSeat.setScreening(screening);
            screening.getScreeningSeats().add(screeningSeat);
            screeningSeat.setTicket(null);
            screeningSeatService.save(screeningSeat);
        }
        logger.debug("Saved and instantiated new screening: " + screening);
    }

    @Override
    public ScreeningDto findDtoById(Long screeningId)
            throws NoEntityFoundException {
        return convertIdToDto(screeningId);
    }

    @Override
    public Optional<ScreeningDto> findScreeningByShowroomAndInBetweenStartTimeAndEndTime(
            Showroom showroom, LocalDateTime startTime, LocalDateTime endTime) {
        return findScreeningByShowroomIdAndInBetweenStartTimeAndEndTime(
                showroom.getId(), startTime, endTime);
    }

    @Override
    public Optional<ScreeningDto> findScreeningByShowroomIdAndInBetweenStartTimeAndEndTime(
            Long showroomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Screening> screenings = repository.findAllByShowroomWithId(showroomId);
        for (Screening screening : screenings) {
            if (UtilMethods.localDateTimeOverlap(
                    startTime, endTime,
                    screening.getShowDateTime(), screening.getEndDateTime())) {
                return Optional.of(convertToDto(screening));
            }
        }
        return Optional.empty();
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
    public ScreeningDto convertIdToDto(Long id)
            throws NoEntityFoundException {
        Screening screening = findById(id).orElseThrow(
                () -> new NoEntityFoundException("screening", "id", id));
        ScreeningDto screeningDTO = new ScreeningDto();
        screeningDTO.setId(screening.getId());
        screeningDTO.setMovieId(screening.getMovie().getId());
        screeningDTO.setShowroomId(screening.getShowroom().getId());
        screeningDTO.setMovieTitle(screening.getMovie().getTitle());
        screeningDTO.setShowroomLetter(screening.getShowroom().getShowroomLetter());
        screeningDTO.setShowDateTime(screening.getShowDateTime());
        screeningDTO.setEndDateTime(screening.getEndDateTime());
        screeningDTO.setTotalSeatsInRoom(screening.getShowroom().getShowroomSeats().size());
        long numberOfSeatsBooked = screening
                .getScreeningSeats().stream().filter(
                        screeningSeat -> screeningSeat.getTicket() != null).count();
        screeningDTO.setSeatsBooked((int) numberOfSeatsBooked);
        screeningDTO.setSeatsAvailable(screeningDTO.getTotalSeatsInRoom() - (int) numberOfSeatsBooked);
        logger.debug("Converted screening to DTO: " + screeningDTO);
        logger.debug("Screening: " + screening);
        return screeningDTO;
    }

}
