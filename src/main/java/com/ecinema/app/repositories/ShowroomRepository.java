package com.ecinema.app.repositories;

import com.ecinema.app.entities.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowroomRepository extends JpaRepository<Showroom, Long> {
}
