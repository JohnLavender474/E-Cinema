package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.entities.User;
import com.ecinema.app.utils.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminRoleDefRepositoryTest {

    @Autowired
    private AdminRoleDefRepository adminRoleDefRepository;

    @Autowired
    private AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        adminRoleDefRepository.deleteAll();
    }

    @Test
    void test1FindDistinctByTheatersContains() {
        // given
        Theater theater = new Theater();
        theaterRepository.save(theater);
        User user = new User();
        userRepository.save(user);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        adminRoleDef.setTheatersBeingManaged(new HashSet<>() {{
            add(theater);
        }});
        theater.setAdmins(new HashSet<>() {{
            add(adminRoleDef);
        }});
        adminRoleDefRepository.save(adminRoleDef);
        // when
        List<AdminRoleDef> adminAuthorities = adminRoleDefRepository
                .findDistinctByTheatersBeingManagedContains(theater);
        // then
        assertTrue(adminAuthorities.contains(adminRoleDef));
    }

    @Test
    void test2FindDistinctByTheatersContains() {
        // given
        List<Theater> theaters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Theater theater = new Theater();
            theater.setTheaterNumber(i);
            theaterRepository.save(theater);
            theaters.add(i, theater);
        }
        User user1 = new User();
        userRepository.save(user1);
        AdminRoleDef adminRoleDef1 = new AdminRoleDef();
        adminRoleDef1.setUser(user1);
        user1.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef1);
        adminRoleDef1.setTheatersBeingManaged(new HashSet<>() {{
            add(theaters.get(0));
            add(theaters.get(1));
        }});
        adminRoleDefRepository.save(adminRoleDef1);
        User user2 = new User();
        userRepository.save(user2);
        AdminRoleDef adminRoleDef2 = new AdminRoleDef();
        adminRoleDef2.setUser(user2);
        user2.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef2);
        adminRoleDef2.setTheatersBeingManaged(new HashSet<>() {{
            add(theaters.get(1));
            add(theaters.get(2));
        }});
        adminRoleDefRepository.save(adminRoleDef2);
        // when
        List<AdminRoleDef> adminAuthorities1 = adminRoleDefRepository
                .findDistinctByTheatersBeingManagedContains(theaters.get(0));
        List<AdminRoleDef> adminAuthorities2 = adminRoleDefRepository
                .findDistinctByTheatersBeingManagedContains(theaters.get(1));
        List<AdminRoleDef> adminAuthorities3 = adminRoleDefRepository
                .findDistinctByTheatersBeingManagedContains(theaters.get(2));
        // then
        assertTrue(adminAuthorities1.contains(adminRoleDef1) && !adminAuthorities1.contains(adminRoleDef2));
        assertTrue(adminAuthorities2.contains(adminRoleDef1) && adminAuthorities2.contains(adminRoleDef2));
        assertTrue(!adminAuthorities3.contains(adminRoleDef1) && adminAuthorities3.contains(adminRoleDef2));
    }

    @Test
    void testFindDistinctByAdminTraineesContains() {
        // given
        // mentor
        User mentor = new User();
        userRepository.save(mentor);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setUser(mentor);
        mentor.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        adminRoleDefRepository.save(adminRoleDef);
        // trainee
        User trainee = new User();
        userRepository.save(trainee);
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDef.setUser(trainee);
        trainee.getUserRoleDefs().put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDef);
        adminTraineeRoleDef.setMentor(adminRoleDef);
        adminRoleDef.setTrainees(new HashSet<>() {{
            add(adminTraineeRoleDef);
        }});
        adminTraineeRoleDefRepository.save(adminTraineeRoleDef);
        // when
        List<AdminRoleDef> adminAuthorities = adminRoleDefRepository
                .findDistinctByTraineesContains(adminTraineeRoleDef);
        // then
        assertTrue(adminAuthorities.contains(adminRoleDef));
    }

}