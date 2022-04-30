package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.User;
import com.ecinema.app.utils.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminTraineeRoleDefRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRoleDefRepository adminRoleDefRepository;

    @Autowired
    private AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;

    @AfterEach
    void tearDown() {
        adminTraineeRoleDefRepository.deleteAll();
    }

    @Test
    void testFindAllByMentor() {
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
        adminTraineeRoleDefRepository.save(adminTraineeRoleDef);
        // when
        List<AdminTraineeRoleDef> findAllByMentor = adminTraineeRoleDefRepository
                .findAllByMentor(adminRoleDef);
        // then
        assertTrue(findAllByMentor.contains(adminTraineeRoleDef));
    }

}