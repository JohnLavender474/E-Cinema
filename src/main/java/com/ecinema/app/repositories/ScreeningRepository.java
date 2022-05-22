package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Screening repository.
 */
@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    /**
     * Find all by movie id page.
     *
     * @param movieId the movie id
     * @return the page
     */
    @Query("SELECT s FROM Screening s WHERE s.movie.id = ?1")
    Page<Screening> findAllByMovieId(Long movieId, Pageable pageable);

    /**
     * Find all by show date time less than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    @Query("SELECT s FROM Screening s WHERE s.showDateTime <= ?1 ORDER BY s.showDateTime ASC")
    List<Screening> findAllByShowDateTimeLessThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by show date time greater than equal list.
     *
     * @param localDateTime the local date time
     * @return the list
     */
    @Query("SELECT s FROM Screening s WHERE s.showDateTime >= ?1 ORDER BY s.showDateTime ASC")
    List<Screening> findAllByShowDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    /**
     * Find all by show date time between list.
     *
     * @param l1 the l 1
     * @param l2 the l 2
     * @return the list
     */
    @Query("SELECT s FROM Screening s WHERE s.showDateTime >= ?1 AND s.showDateTime <= ?2 ORDER BY s.showDateTime ASC")
    List<Screening> findAllByShowDateTimeBetween(LocalDateTime l1, LocalDateTime l2);

    /**
     * Find all by movie list.
     *
     * @param movie the movie
     * @return the list
     */
    List<Screening> findAllByMovie(Movie movie);

    /**
     * Find all by movie with id list.
     *
     * @param movieId the movie id
     * @return the list
     */
    @Query("SELECT s FROM Screening s JOIN s.movie m WHERE m.id = ?1")
    List<Screening> findAllByMovieWithId(Long movieId);

    /**
     * Find all by showroom list.
     *
     * @param showroom the showroom
     * @return the list
     */
    List<Screening> findAllByShowroom(Showroom showroom);

    /**
     * Find all by showroom with id list.
     *
     * @param showroomId the showroom id
     * @return the list
     */
    @Query("SELECT s FROM Screening s JOIN s.showroom sh WHERE sh.id = ?1")
    List<Screening> findAllByShowroomWithId(Long showroomId);

}
