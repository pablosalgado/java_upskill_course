package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.repository.EventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("tickets")
@Profile("async")
public class AsyncTicketController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;
    private final EventRepository eventService;
    private final JmsTemplate jmsTemplate;


    public AsyncTicketController(BookingFacade bookingFacade, EventRepository eventRepository, JmsTemplate jmsTemplate) {
        this.bookingFacade = bookingFacade;
        this.eventService = eventRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listAllTickets(@RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<Ticket> ticketPage = bookingFacade.getBookedTickets(size, page);
        return new ResponseEntity<>(ticketPage.getContent(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> listTicketsByUser(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return bookingFacade.getUserById(userId)
                .map(user -> {
                    Page<Ticket> ticketPage = bookingFacade.getBookedTicketsByUser(user, size, page);
                    return new ResponseEntity<>(ticketPage.getContent(), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> createTicket(@RequestBody String ticket, @PathVariable("userId") Long userId) {
        jmsTemplate.convertAndSend("createTicketQueue", new Object[]{ticket, userId});
        return new ResponseEntity<>("Ticket creation request accepted", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        jmsTemplate.convertAndSend("deleteTicketQueue", id);
        return ResponseEntity.accepted().body("Event deletion request received");
    }

    @PostMapping("/seed")
    public ResponseEntity<Void> seedTickets() {
        bookingFacade.preloadTickets();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
