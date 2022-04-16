package com.ecinema.app.movie;

import com.ecinema.app.abstraction.AbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovieServiceImpl extends AbstractServiceImpl<Movie, MovieRepository> implements MovieService {

    public MovieServiceImpl(MovieRepository repository) {
        super(repository);
    }

}
