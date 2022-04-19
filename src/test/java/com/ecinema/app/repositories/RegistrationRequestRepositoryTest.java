package com.ecinema.app.repositories;

import com.ecinema.app.entities.RegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RegistrationRequestRepositoryTest {

    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    @AfterEach
    void tearDown() {
        registrationRequestRepository.deleteAll();
    }

    @Test
    void findByToken() {
        // given
        String token = UUID.randomUUID().toString();
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setToken(token);
        registrationRequestRepository.save(registrationRequest);
        // when
        Optional<RegistrationRequest> registrationRequestOptional =
                registrationRequestRepository.findByToken(token);
        // then
        assertTrue(registrationRequestOptional.isPresent() &&
                registrationRequestOptional.get().getToken().equals(token));
    }

    @Test
    void findAllByEmail() {
        // given
        String email = "test@gmail.com";
        for (int i = 0; i < 10; i++) {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            if (i % 2 == 0) {
                registrationRequest.setEmail(email);
            } else {
                registrationRequest.setEmail("NULL");
            }
            registrationRequestRepository.save(registrationRequest);
        }
        // when
        List<RegistrationRequest> registrationRequests =
                registrationRequestRepository.findAllByEmail(email);
        // then
        assertEquals(5, registrationRequests.size());
    }

    @Test
    void deleteAllByEmail() {
        // given
        String email = "test@gmail.com";
        for (int i = 0; i < 10; i++) {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            if (i % 2 == 0) {
                registrationRequest.setEmail(email);
            } else {
                registrationRequest.setEmail("NULL");
            }
            registrationRequestRepository.save(registrationRequest);
        }
        // when
        registrationRequestRepository.deleteAll();
        // then
        assertTrue(registrationRequestRepository.findAll().isEmpty());
    }

    @Test
    void deleteAllByCreationDateTimeBefore() {
        // given
        LocalDateTime time1 = LocalDateTime.of(2022, Month.APRIL, 1, 1, 1);
        LocalDateTime time2 = LocalDateTime.of(2022, Month.APRIL, 20, 20, 20);
        for (int i = 0; i < 10; i++) {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            if (i % 2 == 0) {
                registrationRequest.setCreationDateTime(time1);
            } else {
                registrationRequest.setCreationDateTime(time2);
            }
            registrationRequestRepository.save(registrationRequest);
        }
        // when
        LocalDateTime time3 = LocalDateTime.of(2022, Month.APRIL, 10, 10, 10);
        registrationRequestRepository.deleteAllByCreationDateTimeBefore(time3);
        // then
        assertEquals(5, registrationRequestRepository.findAll().size());
    }

}