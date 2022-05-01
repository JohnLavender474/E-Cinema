package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.entities.Theater;
import com.ecinema.app.utils.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Showroom repository.
 */
@Repository
public interface ShowroomRepository extends JpaRepository<Showroom, Long>, AbstractRepository {

    /**
     * Find by showroom letter optional.
     *
     * @param showroomLetter the showroom letter
     * @return the optional
     */
    Optional<Showroom> findByShowroomLetter(Letter showroomLetter);

    /**
     * Find by showroom seats contains optional.
     *
     * @param showroomSeat the showroom seat
     * @return the optional
     */
    @Query("SELECT s FROM Showroom s JOIN s.showroomSeats ss WHERE ss = ?1")
    Optional<Showroom> findByShowroomSeatsContains(ShowroomSeat showroomSeat);

    /**
     * Find by showroom seats contains with id optional.
     *
     * @param showroomSeatId the showroom seat id
     * @return the optional
     */
    @Query("SELECT s FROM Showroom s JOIN s.showroomSeats ss WHERE ss.id = ?1")
    Optional<Showroom> findByShowroomSeatsContainsWithId(Long showroomSeatId);

    /**
     * Find by screenings contains optional.
     *
     * @param screening the screening
     * @return the optional
     */
    @Query("SELECT s FROM Showroom s JOIN s.screenings sc WHERE sc = ?1")
    Optional<Showroom> findByScreeningsContains(Screening screening);

    /**
     * Find by screenings contains with id optional.
     *
     * @param screeningId the screening id
     * @return the optional
     */
    @Query("SELECT s FROM Showroom s JOIN s.screenings sc WHERE sc.id = ?1")
    Optional<Showroom> findByScreeningsContainsWithId(Long screeningId);

    /**
     * Find all by theater list.
     *
     * @param theater the theater
     * @return the list
     */
    List<Showroom> findAllByTheater(Theater theater);

    /**
     * Find all by theater with id list.
     *
     * @param theaterId the theater id
     * @return the list
     */
    @Query("SELECT s FROM Showroom s WHERE s.theater.id = ?1")
    List<Showroom> findAllByTheaterWithId(Long theaterId);

}
