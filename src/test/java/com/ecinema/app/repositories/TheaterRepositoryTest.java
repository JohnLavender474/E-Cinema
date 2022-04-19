package com.ecinema.app.repositories;

import com.ecinema.app.entities.Theater;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TheaterRepositoryTest {

    @Autowired
    private TheaterRepository theaterRepository;

    @Test
    void testFindByTheaterName() {
        // given
        Theater theater = new Theater();
        theater.setTheaterName("Theater 1");
        theaterRepository.save(theater);
        // when
        Optional<Theater> theaterOptional = theaterRepository.findByTheaterName("Theater 1");
        // then
        assertTrue(theaterOptional.isPresent() && theaterOptional.get().equals(theater));
    }

    @Test
    void testFindByTheaterNumber() {
        // given
        Theater theater = new Theater();
        theater.setTheaterNumber(1);
        theaterRepository.save(theater);
        // when
        Optional<Theater> theaterOptional = theaterRepository.findByTheaterNumber(1);
        // then
        assertTrue(theaterOptional.isPresent() && theaterOptional.get().equals(theater));
    }

}