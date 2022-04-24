package com.ecinema.app.repositories;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.MovieCategory;
import com.ecinema.app.utils.constants.MsrbRating;
import com.ecinema.app.utils.objects.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static com.ecinema.app.utils.constants.MovieCategory.*;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    void findAllByMsrbRating() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Movie movie = new Movie();
            int j = i % MsrbRating.values().length;
            movie.setMsrbRating(MsrbRating.values()[j]);
            movieRepository.save(movie);
            movies.add(movie);
        }
        List<Movie> control = movies.stream()
                .filter(movie -> movie.getMsrbRating().equals(MsrbRating.PG))
                .collect(Collectors.toList());
        // when
        List<Movie> test = movieRepository.findAllByMsrbRating(MsrbRating.PG);
        // then
        assertEquals(5, control.size());
        assertEquals(control.size(), test.size());
        assertEquals(control, test);
    }

    @Test
    void findAllByMoviesCategoriesContainsOne() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Movie movie = new Movie();
            int j = i % MovieCategory.values().length;
            movie.getMovieCategories().add(MovieCategory.values()[j]);
            movieRepository.save(movie);
            movies.add(movie);
        }
        List<Movie> control = movies.stream()
                .filter(movie -> movie.getMovieCategories().contains(MovieCategory.ROMANCE))
                .collect(Collectors.toList());
        // when
        List<Movie> test = movieRepository.findAllByMoviesCategoriesContains(MovieCategory.ROMANCE);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByMoviesCategoriesContainsSet() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Movie movie = new Movie();
            movie.setTitle(UUID.randomUUID().toString());
            int j = i % MovieCategory.values().length;
            while (j >= 0) {
                movie.getMovieCategories().add(MovieCategory.values()[j]);
                j--;
            }
            movieRepository.save(movie);
            movies.add(movie);
        }
        List<Movie> control = movies.stream()
                .filter(movie -> {
                    Set<MovieCategory> movieCategories = movie.getMovieCategories();
                    return movieCategories.contains(HORROR) ||
                            movieCategories.contains(DARK) ||
                            movieCategories.contains(EPIC) ||
                            movieCategories.contains(RAUNCHY);

                }).collect(Collectors.toList());
        Comparator<Movie> movieComparator = Comparator.comparing(Movie::getTitle);
        control.sort(movieComparator);
        // when
        List<Movie> test = movieRepository.findAllByMovieCategoriesContainsSet(
                new HashSet<>() {{
                    add(HORROR);
                    add(DARK);
                    add(EPIC);
                    add(RAUNCHY);
                }});
        test.sort(movieComparator);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllOrderByReleaseDateAscending() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Movie movie = new Movie();
            movie.setReleaseDate(UtilMethods.randomDate());
            movieRepository.save(movie);
            movies.add(movie);
        }
        Comparator<Movie> movieComparator = Comparator.comparing(Movie::getReleaseDate);
        movies.sort(movieComparator);
        // when
        List<Movie> test = movieRepository.findAllOrderByReleaseDateAscending();
        // then
        assertEquals(movies, test);
    }

    @Test
    void findAllOrderByReleaseDateDescending() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Movie movie = new Movie();
            movie.setReleaseDate(UtilMethods.randomDate());
            movieRepository.save(movie);
            movies.add(movie);
        }
        Comparator<Movie> movieComparator = (o1, o2) -> o2.getReleaseDate().compareTo(o1.getReleaseDate());
        movies.sort(movieComparator);
        // when
        List<Movie> test = movieRepository.findAllOrderByReleaseDateDescending();
        // then
        assertEquals(movies, test);
    }

    @Test
    void findAllOrderByDurationAscending() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Movie movie = new Movie();
            movie.setDuration(Duration.randomDuration());
            movieRepository.save(movie);
            movies.add(movie);
        }
        Comparator<Movie> movieComparator = Comparator.comparing(Movie::getDuration);
        movies.sort(movieComparator);
        // when
        List<Movie> test = movieRepository.findAllOrderByDurationAscending();
        // then
        assertEquals(movies.size(), test.size());
        for (int i = 0; i < movies.size(); i++) {
            assertTrue(movies.get(i).getDuration().compareTo(test.get(i).getDuration()) <= 0);
        }
    }

    @Test
    void findAllOrderByDurationDescending() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Movie movie = new Movie();
            movie.setDuration(Duration.randomDuration());
            movieRepository.save(movie);
            movies.add(movie);
        }
        Comparator<Movie> movieComparator = (o1, o2) -> o2.getDuration().compareTo(o1.getDuration());
        movies.sort(movieComparator);
        // when
        List<Movie> test = movieRepository.findAllOrderByDurationDescending();
        // then
        assertEquals(movies.size(), test.size());
        for (int i = 0; i < movies.size(); i++) {
            assertTrue(movies.get(i).getDuration().compareTo(test.get(i).getDuration()) >= 0);
        }
    }

}