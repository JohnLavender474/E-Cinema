package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.TicketRefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRefundRequestRepository extends JpaRepository<TicketRefundRequest, Long> {
}
