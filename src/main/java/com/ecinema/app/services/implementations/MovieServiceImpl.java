package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.services.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository> implements MovieService {

    public MovieServiceImpl(MovieRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Movie movie) {

    }

}
