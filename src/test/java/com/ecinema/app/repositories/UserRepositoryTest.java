package com.ecinema.app.repositories;

import com.ecinema.app.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final List<User> users;

    UserRepositoryTest() {
        users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();

        }
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findByEmail() {
    }

    @Test
    void findAllByUserRole() {
    }

    @Test
    void findAllByIsAccountLocked() {
    }

    @Test
    void findAllByIsAccountEnabled() {
    }

    @Test
    void findAllByIsAccountExpired() {
    }

    @Test
    void findAllByIsCredentialsExpired() {
    }

    @Test
    void findAllByCreationDateTimeBefore() {
    }

    @Test
    void findAllByCreationDateTimeAfter() {
    }

    @Test
    void findAllByLastActivityDateTimeBefore() {
    }

    @Test
    void findAllByLastActivityDateTimeAfter() {
    }

}