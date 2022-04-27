package com.ecinema.app.repositories;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ModeratorRoleDefRepositoryTest {

    @Autowired
    private ModeratorRoleDefRepository moderatorRoleDefRepository;

    @AfterEach
    void tearDown() {
        moderatorRoleDefRepository.deleteAll();
    }

}