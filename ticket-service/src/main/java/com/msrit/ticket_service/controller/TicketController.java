package com.msrit.ticket_service.controller;

import com.msrit.ticket_service.entity.Ticket;
import com.msrit.ticket_service.entity.TicketRequest;
import com.msrit.ticket_service.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RestTemplate restTemplate;

    // API to book a ticket for an event
    @PostMapping("/book")
    public ResponseEntity<String> bookTicket(@RequestBody TicketRequest request) {
        // Call event-service to get event details
        String eventUrl = "http://event-service/events/" + request.getEventId();
        ResponseEntity<String> eventResponse = restTemplate.getForEntity(eventUrl, String.class);
        if (eventResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        // Call user-service to check user details
        String userUrl = "http://user-service/users/" + request.getUserId();
        ResponseEntity<String> userResponse = restTemplate.getForEntity(userUrl, String.class);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Book ticket logic (saving ticket) goes here
        Ticket ticket = ticketService.bookTicket(request.getUserId(), request.getEventId());

        return new ResponseEntity<>("Ticket booked successfully. Ticket Id: " + ticket.getId(), HttpStatus.OK);
    }

    // API to get ticket details
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        return ticketService.getTicketById(ticketId)
                .map(ticket -> new ResponseEntity<>(ticket, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // API to get tickets for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(ticketService.getTicketsByUserId(userId), HttpStatus.OK);
    }
}

