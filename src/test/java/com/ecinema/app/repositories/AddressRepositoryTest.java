package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void fetchAllIds() {
        // given
        for (int i = 0; i < 5; i++) {
            addressRepository.save(new Address());
        }
        // when
        List<Long> ids = addressRepository.findAllIds();
        // then
        assertEquals(5, ids.size());
        for (int i = 0; i < 5; i++) {
            assertTrue(addressRepository.findById(ids.get(i)).isPresent());
        }
    }

}