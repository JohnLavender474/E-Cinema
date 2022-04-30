package com.ecinema.app.repositories;

import com.ecinema.app.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Theater repository.
 */
@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    /**
     * Find by theater name optional.
     *
     * @param theaterName the theater name
     * @return the optional
     */
    Optional<Theater> findByTheaterName(String theaterName);

    /**
     * Find by theater number optional.
     *
     * @param theaterNumber the theater number
     * @return the optional
     */
    Optional<Theater> findByTheaterNumber(Integer theaterNumber);

}
