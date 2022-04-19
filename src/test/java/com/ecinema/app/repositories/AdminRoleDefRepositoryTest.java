package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
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

    @AfterEach
    void tearDown() {
        adminRoleDefRepository.deleteAll();
    }

    @Test
    void test1FindDistinctByTheatersContains() {
        // given
        Theater theater = new Theater();
        theaterRepository.save(theater);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setTheaters(new HashSet<>() {{
            add(theater);
        }});
        theater.setAdmins(new HashSet<>() {{
            add(adminRoleDef);
        }});
        adminRoleDefRepository.save(adminRoleDef);
        // when
        List<AdminRoleDef> adminAuthorities = adminRoleDefRepository
                .findDistinctByTheatersContains(theater);
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
        AdminRoleDef adminRoleDef1 = new AdminRoleDef();
        adminRoleDef1.setTheaters(new HashSet<>() {{
            add(theaters.get(0));
            add(theaters.get(1));
        }});
        adminRoleDefRepository.save(adminRoleDef1);
        AdminRoleDef adminRoleDef2 = new AdminRoleDef();
        adminRoleDef2.setTheaters(new HashSet<>() {{
            add(theaters.get(1));
            add(theaters.get(2));
        }});
        adminRoleDefRepository.save(adminRoleDef2);
        // when
        List<AdminRoleDef> adminAuthorities1 = adminRoleDefRepository
                .findDistinctByTheatersContains(theaters.get(0));
        List<AdminRoleDef> adminAuthorities2 = adminRoleDefRepository
                .findDistinctByTheatersContains(theaters.get(1));
        List<AdminRoleDef> adminAuthorities3 = adminRoleDefRepository
                .findDistinctByTheatersContains(theaters.get(2));
        // then
        assertTrue(adminAuthorities1.contains(adminRoleDef1) && !adminAuthorities1.contains(adminRoleDef2));
        assertTrue(adminAuthorities2.contains(adminRoleDef1) && adminAuthorities2.contains(adminRoleDef2));
        assertTrue(!adminAuthorities3.contains(adminRoleDef1) && adminAuthorities3.contains(adminRoleDef2));
    }

    @Test
    void testFindDistinctByAdminTraineesContains() {
        // given
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDefRepository.save(adminTraineeRoleDef);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setTrainees(new HashSet<>() {{
            add(adminTraineeRoleDef);
        }});
        adminRoleDefRepository.save(adminRoleDef);
        // when
        List<AdminRoleDef> adminAuthorities = adminRoleDefRepository
                .findDistinctByTraineesContains(adminTraineeRoleDef);
        // then
        assertTrue(adminAuthorities.contains(adminRoleDef));
    }

}