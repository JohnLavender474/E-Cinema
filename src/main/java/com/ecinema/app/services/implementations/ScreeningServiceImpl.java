package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.ScreeningRepository;
import com.ecinema.app.services.ScreeningService;
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

    /**
     * Instantiates a new Screening service.
     *
     * @param repository the repository
     */
    public ScreeningServiceImpl(ScreeningRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Screening screening) {

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
    public List<Screening> findAllByTheater(Theater theater) {
        return repository.findAllByTheater(theater);
    }

    @Override
    public List<Screening> findAllByTheaterWithId(Long theaterId) {
        return repository.findAllByTheaterWithId(theaterId);
    }

    @Override
    public List<Screening> findAllByShowroom(Showroom showroom) {
        return repository.findAllByShowroom(showroom);
    }

    @Override
    public List<Screening> findAllByShowroomWithId(Long showroomId) {
        return repository.findAllByShowroomWithId(showroomId);
    }

    @Override
    public Optional<Screening> findByTicketsContains(Ticket ticket) {
        return repository.findByTicketsContains(ticket);
    }

    @Override
    public Optional<Screening> findByTicketsContainsWithId(Long ticketId) {
        return repository.findByTicketsContainsWithId(ticketId);
    }

}
