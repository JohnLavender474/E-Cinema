package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.CustomerRoleDef;
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
        // detach Customer
        CustomerRoleDef customerRoleDef = review.getWriter();
        if (customerRoleDef != null) {
            customerRoleDef.getReviews().remove(review);
            review.setWriter(null);
        }
        // detatch Movie
        Movie movie = review.getMovie();
        if (movie != null) {
            movie.getReviews().remove(review);
            review.setMovie(null);
        }
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
