package com.ecinema.app.repositories;

import com.ecinema.app.entities.User;
import com.ecinema.app.utils.UtilMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final Random rand;

    UserRepositoryTest() {
        rand = new Random(System.currentTimeMillis());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testExistsByEmail() {
        // given
        User user = new User();
        user.setEmail("test@gmail.com");
        userRepository.save(user);
        // when
        boolean expected = userRepository.existsByEmail("test@gmail.com");
        // then
        assertTrue(expected);
    }

    @Test
    void findByEmail() {
        // given
        User user = new User();
        user.setEmail("test@gmail.com");
        userRepository.save(user);
        // when
        Optional<User> userOptional = userRepository.findByEmail("test@gmail.com");
        // then
        assertTrue(userOptional.isPresent() && userOptional.get().equals(user));
    }

    @Test
    void findAllByIsAccountLocked() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setIsAccountLocked(rand.nextBoolean());
            userRepository.save(user);
            users.add(i, user);
        }
        // when
        List<User> unlockedUsers = users
                .stream().filter(User::isAccountNonLocked)
                .collect(Collectors.toList());
        // then
        assertEquals(unlockedUsers, userRepository.findAllByIsAccountLocked(false));
    }

    @Test
    void findAllByCreationDateTimeBefore() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setCreationDateTime(UtilMethods.randomDateTime());
            users.add(user);
            userRepository.save(user);
        }
        LocalDateTime randomLDT = UtilMethods.randomDateTime();
        // when
        List<User> usersBefore = users.stream()
                .filter(user -> user.getCreationDateTime().isBefore(randomLDT))
                .collect(Collectors.toList());
        // then
        assertEquals(usersBefore, userRepository.findAllByCreationDateTimeBefore(randomLDT));
    }

    @Test
    void findAllByCreationDateTimeAfter() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setCreationDateTime(UtilMethods.randomDateTime());
            users.add(user);
            userRepository.save(user);
        }
        LocalDateTime randomLDT = UtilMethods.randomDateTime();
        // when
        List<User> usersBefore = users.stream()
                                      .filter(user -> user.getCreationDateTime().isAfter(randomLDT))
                                      .collect(Collectors.toList());
        // then
        assertEquals(usersBefore, userRepository.findAllByCreationDateTimeAfter(randomLDT));
    }

    @Test
    void findAllByLastActivityDateTimeBefore() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setLastActivityDateTime(UtilMethods.randomDateTime());
            users.add(user);
            userRepository.save(user);
        }
        LocalDateTime randomLDT = UtilMethods.randomDateTime();
        // when
        List<User> usersBefore = users.stream()
                                      .filter(user -> user.getLastActivityDateTime().isBefore(randomLDT))
                                      .collect(Collectors.toList());
        // then
        assertEquals(usersBefore, userRepository.findAllByLastActivityDateTimeBefore(randomLDT));

    }

    @Test
    void findAllByLastActivityDateTimeAfter() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setLastActivityDateTime(UtilMethods.randomDateTime());
            users.add(user);
            userRepository.save(user);
        }
        LocalDateTime randomLDT = UtilMethods.randomDateTime();
        // when
        List<User> usersBefore = users.stream()
                                      .filter(user -> user.getLastActivityDateTime().isAfter(randomLDT))
                                      .collect(Collectors.toList());
        // then
        assertEquals(usersBefore, userRepository.findAllByLastActivityDateTimeAfter(randomLDT));
    }

}