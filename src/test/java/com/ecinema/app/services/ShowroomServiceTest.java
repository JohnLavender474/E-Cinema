package com.ecinema.app.services;

import com.ecinema.app.entities.Showroom;
import com.ecinema.app.entities.ShowroomSeat;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.services.implementations.ShowroomServiceImpl;
import com.ecinema.app.utils.constants.Letter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * The type Showroom service test.
 */
@ExtendWith(MockitoExtension.class)
class ShowroomServiceTest {

    private ShowroomService showroomService;
    @Mock
    private ShowroomRepository showroomRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        showroomService = new ShowroomServiceImpl(showroomRepository);
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        showroomService.deleteAll();
    }

    /**
     * Find by showroom letter.
     */
    @Test
    void findByShowroomLetter() {
        // given
        Map<Letter, Showroom> showrooms = new EnumMap<>(Letter.class);
        for (int i = 0; i < Letter.values().length; i++) {
            Showroom showroom = new Showroom();
            Letter showroomLetter = Letter.values()[i];
            showroom.setShowroomLetter(showroomLetter);
            showroomService.save(showroom);
            showrooms.put(showroomLetter, showroom);
        }
        Showroom control = showrooms.get(Letter.A);
        given(showroomRepository.findByShowroomLetter(Letter.A))
                .willReturn(Optional.of(showrooms.get(Letter.A)));
        // when
        Optional<Showroom> test = showroomRepository.findByShowroomLetter(Letter.A);
        // then
        assertTrue(test.isPresent());
        assertEquals(control, test.get());
    }

    /**
     * Find by showroom seats contains.
     */
    @Test
    void findByShowroomSeatsContains() {
        // given
        Showroom showroom = new Showroom();
        for (int i = 0; i < 5; i++) {
            Letter rowLetter = Letter.values()[i];
            for (int j = 0; j < 20; j++) {
                ShowroomSeat showroomSeat = new ShowroomSeat();
                showroomSeat.setRowLetter(rowLetter);
                showroomSeat.setSeatNumber(j);
                showroomSeat.setShowroom(showroom);
                showroom.getShowroomSeats().add(showroomSeat);
            }
        }
    }

    /**
     * Find by screenings contains.
     */
    @Test
    void findByScreeningsContains() {
    }

    /**
     * Find all by theater.
     */
    @Test
    void findAllByTheater() {
    }

}