package com.ecinema.app.repositories;

import com.ecinema.app.entities.Movie;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.MovieCategory;
import com.ecinema.app.utils.MsrbRating;
import com.ecinema.app.utils.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static com.ecinema.app.utils.MovieCategory.*;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    void findAllPagination() {
        // given
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setTitle("title" + i);
            movies.add(movie);
            movieRepository.save(movie);
        }
        List<Movie> control = movies.subList(0, 5);
        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<Movie> page = movieRepository.findAll(pageable);
        List<Movie> test = page.getContent();
        // then
        for (int i = 0; i < 5; i++) {
            Movie movie1 = control.get(i);
            Movie movie2 = test.get(i);
            assertEquals(movie1.getId(), movie2.getId());
            assertEquals(movie1.getTitle(), movie2.getTitle());
        }
    }

    @Test
    void findAllLikeTitlePagination() {
        // given
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setId((long) i + 1);
            movie.setTitle(i < 15 ? "title" : "dummy");
            movieRepository.save(movie);
        }
        // when
        Pageable pageable =  PageRequest.of(0, 15);
        Page<Movie> page = movieRepository.findByTitleContaining("t", pageable);
        List<Movie> test = page.getContent();
        // then
        assertEquals(15, test.size());
        for (Movie movie : test) {
            assertTrue(movie.getTitle().contains("t"));
        }
    }

    @Test
    void findAllLikeTitle() {
        // given
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setId((long) i + 1);
            movie.setTitle(i < 15 ? "title" + i : "dummy");
            movieRepository.save(movie);
        }
        // when
        List<Movie> test = movieRepository.findByTitleContaining("t");
        // then
        assertEquals(15, test.size());
        for (Movie movie : test) {
            assertTrue(movie.getTitle().contains("t"));
        }
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