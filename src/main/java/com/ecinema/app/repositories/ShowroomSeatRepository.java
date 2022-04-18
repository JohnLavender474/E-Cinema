package com.ecinema.app.repositories;

import com.ecinema.app.entities.ShowroomSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowroomSeatRepository extends JpaRepository<ShowroomSeat, Long> {
}
