package com.ecinema.app.controllers;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.services.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}")
    public Movie getMovie(@PathVariable("movieId") Long movieId) {
        return movieService.findById(movieId).orElseThrow(IllegalStateException::new);
    }

}
