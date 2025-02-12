package com.msrit.ticket_service.repository;

import com.msrit.ticket_service.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Custom query method to get tickets by user ID
    List<Ticket> findByUserId(Long userId);
}
