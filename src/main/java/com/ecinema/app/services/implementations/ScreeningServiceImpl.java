package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.ScreeningRepository;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.services.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<Screening> findAllByShowDateTimeLessThanEqual(LocalDateTime localDateTime) {
        return repository.findAllByShowDateTimeLessThanEqual(localDateTime);
    }

    @Override
    public List<Screening> findAllByShowDateTimeGreaterThanEqual(LocalDateTime localDateTime) {
        return repository.findAllByShowDateTimeGreaterThanEqual(localDateTime);
    }

    @Override
    public List<Screening> findAllByShowDateTimeBetween(LocalDateTime l1, LocalDateTime l2) {
        return repository.findAllByShowDateTimeBetween(l1, l2);
    }

    @Override
    public List<Screening> findAllByMovie(Movie movie) {
        return repository.findAllByMovie(movie);
    }

    @Override
    public List<Screening> findAllByMovieWithId(Long movieId) {
        return repository.findAllByMovieWithId(movieId);
    }

    @Override
    public List<Screening> findAllByShowroom(Showroom showroom) {
        return repository.findAllByShowroom(showroom);
    }

    @Override
    public List<Screening> findAllByShowroomWithId(Long showroomId) {
        return repository.findAllByShowroomWithId(showroomId);
    }

}
