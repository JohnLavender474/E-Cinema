package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Address repository.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, AbstractRepository {}
