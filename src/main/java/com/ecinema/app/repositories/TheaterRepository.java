package com.ecinema.app.repositories;

import com.ecinema.app.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByTheaterName(String theaterName);
    Optional<Theater> findByTheaterNumber(Integer theaterNumber);
}
