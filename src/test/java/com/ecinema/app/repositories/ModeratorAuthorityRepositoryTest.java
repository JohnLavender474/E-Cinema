package com.ecinema.app.repositories;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ModeratorAuthorityRepositoryTest {

    @Autowired
    private ModeratorAuthorityRepository moderatorAuthorityRepository;

    @AfterEach
    void tearDown() {
        moderatorAuthorityRepository.deleteAll();
    }

}