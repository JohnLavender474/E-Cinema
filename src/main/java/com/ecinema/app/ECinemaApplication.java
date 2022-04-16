package com.ecinema.app;

import com.ecinema.app.movie.Movie;
import com.ecinema.app.movie.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECinemaApplication implements CommandLineRunner {

    private final MovieService movieService;

    public ECinemaApplication(MovieService movieService) {
        this.movieService = movieService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ECinemaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Movie movie = new Movie();
        movie.setTitle("Test");
        movieService.save(movie);
    }

}
