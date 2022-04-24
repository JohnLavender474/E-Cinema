package com.ecinema.app.services;

import com.ecinema.app.repositories.ScreeningRepository;
import com.ecinema.app.services.implementations.ScreeningServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    ScreeningService screeningService;
    MovieService movieService;
    TheaterService theaterService;

    @Mock
    ScreeningRepository screeningRepository;

    @BeforeEach
    void setUp() {
        screeningService = new ScreeningServiceImpl(screeningRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findAllByShowDateTimeLessThanEqual() {
    }

    @Test
    void findAllByShowDateTimeGreaterThanEqual() {
    }

    @Test
    void findAllByShowDateTimeBetween() {
    }

    @Test
    void findAllByMovie() {
    }

    @Test
    void findAllByTheater() {
    }

    @Test
    void findAllByShowroom() {
    }

    @Test
    void findByTicketsContains() {
    }
}