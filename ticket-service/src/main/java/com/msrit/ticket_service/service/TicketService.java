package com.msrit.ticket_service.service;

import com.msrit.ticket_service.entity.Ticket;
import com.msrit.ticket_service.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Ticket bookTicket(Long userId, Long eventId) {
        // Use the service name (user-service) to call the user-service API via Eureka
        String userServiceUrl = "http://USER-SERVICE/users/" + userId;
        ResponseEntity<String> userResponse = restTemplate.getForEntity(userServiceUrl, String.class);

        // Use the service name (event-service) to call the event-service API via Eureka
        String eventServiceUrl = "http://EVENT-SERVICE/events/" + eventId;
        ResponseEntity<String> eventResponse = restTemplate.getForEntity(eventServiceUrl, String.class);

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setStatus("booked");

        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }
}
