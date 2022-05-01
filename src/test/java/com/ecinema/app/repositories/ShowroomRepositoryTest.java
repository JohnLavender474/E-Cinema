package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.entities.ShowroomSeat;
import com.ecinema.app.domain.entities.Theater;
import com.ecinema.app.utils.Letter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Showroom repository test.
 */
@DataJpaTest
class ShowroomRepositoryTest {

    @Autowired
    private ShowroomRepository showroomRepository;

    @Autowired
    private ShowroomSeatRepository showroomSeatRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        showroomRepository.deleteAll();
        showroomSeatRepository.deleteAll();
        screeningRepository.deleteAll();
        theaterRepository.deleteAll();
    }

    /**
     * Find by showroom letter.
     */
    @Test
    void findByShowroomLetter() {
        // given
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroomRepository.save(showroom);
        // when
        Optional<Showroom> showroomOptional =
                showroomRepository.findByShowroomLetter(Letter.A);
        // then
        assertTrue(showroomOptional.isPresent());
        assertEquals(showroom, showroomOptional.get());
    }

    /**
     * Find by showroom seats contains.
     */
    @Test
    void findByShowroomSeatsContains() {
        // given
        Showroom showroom = new Showroom();
        showroomRepository.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setShowroom(showroom);
        showroom.getShowroomSeats().add(showroomSeat);
        showroomSeatRepository.save(showroomSeat);
        // when
        Optional<Showroom> showroomOptional1 = showroomRepository
                .findByShowroomSeatsContains(showroomSeat);
        Optional<Showroom> showroomOptional2 = showroomRepository
                .findByShowroomSeatsContainsWithId(showroomSeat.getId());
        // then
        assertTrue(showroomOptional1.isPresent());
        assertTrue(showroomOptional2.isPresent());
        assertEquals(showroom, showroomOptional1.get());
        assertEquals(showroom, showroomOptional2.get());
    }

    /**
     * Find by screenings contains.
     */
    @Test
    void findByScreeningsContains() {
        // given
        Showroom showroom = new Showroom();
        showroomRepository.save(showroom);
        Screening screening = new Screening();
        screening.setShowroom(showroom);
        showroom.getScreenings().add(screening);
        screeningRepository.save(screening);
        // when
        Optional<Showroom> showroomOptional1 = showroomRepository
                .findByScreeningsContains(screening);
        Optional<Showroom> showroomOptional2 = showroomRepository
                .findByScreeningsContainsWithId(screening.getId());
        // then
        assertTrue(showroomOptional1.isPresent());
        assertTrue(showroomOptional2.isPresent());
        assertEquals(showroom, showroomOptional1.get());
        assertEquals(showroom, showroomOptional2.get());
    }

    /**
     * Find all by theater.
     */
    @Test
    void findAllByTheater() {
        // given
        Theater theater = new Theater();
        theaterRepository.save(theater);
        Showroom showroom = new Showroom();
        showroom.setTheater(theater);
        showroom.setShowroomLetter(Letter.A);
        theater.getShowrooms().put(Letter.A, showroom);
        showroomRepository.save(showroom);
        // when
        List<Showroom> showrooms1 = showroomRepository
                .findAllByTheater(theater);
        List<Showroom> showrooms2 = showroomRepository
                .findAllByTheaterWithId(theater.getId());
        // then
        assertTrue(showrooms1.contains(showroom));
        assertTrue(showrooms2.contains(showroom));
        assertEquals(showrooms1, showrooms2);
    }

}