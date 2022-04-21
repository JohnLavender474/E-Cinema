package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Theater;
import com.ecinema.app.repositories.TheaterRepository;
import com.ecinema.app.services.TheaterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
