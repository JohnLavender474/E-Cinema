package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.entities.Review;
import com.ecinema.app.repositories.ReviewRepository;
import com.ecinema.app.services.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl extends AbstractServiceImpl<Review, ReviewRepository>
        implements ReviewService {

    public ReviewServiceImpl(ReviewRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(Review review) {
        
    }

    @Override
    public List<Review> findAllByMovie(Movie movie) {
        return repository.findAllByMovie(movie);
    }

    @Override
    public List<Review> findAllByMovieWithId(Long movieId) {
        return repository.findAllByMovieWithId(movieId);
    }

}
