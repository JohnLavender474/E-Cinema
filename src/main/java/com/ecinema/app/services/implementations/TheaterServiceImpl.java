package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Theater;
import com.ecinema.app.repositories.TheaterRepository;
import com.ecinema.app.services.TheaterService;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The type Theater service.
 */
@Service
@Transactional
public class TheaterServiceImpl extends AbstractServiceImpl<Theater, TheaterRepository>
        implements TheaterService {

    /**
     * Instantiates a new Theater service.
     *
     * @param repository the repository
     */
    public TheaterServiceImpl(TheaterRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Theater theater) {

    }

    @Override
    public Optional<Theater> findByTheaterName(String theaterName) {
        return repository.findByTheaterName(theaterName);
    }

    @Override
    public Optional<Theater> findByTheaterNumber(Integer theaterNumber) {
        return repository.findByTheaterNumber(theaterNumber);
    }

    @Override
    public void addShowroomToTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public void removeShowroomFromTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public void addScreeningToTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

    @Override
    public void removeScreeningFromTheater(Long theaterId, Long screeningId)
            throws NoEntityFoundException {

    }

}
