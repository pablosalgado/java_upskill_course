package com.epam.pablo.task01.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

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

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;
    private final EventRepository eventService;

    public TicketController(BookingFacade bookingFacade, EventRepository eventRepository) {
        this.bookingFacade = bookingFacade;
        this.eventService = eventRepository;
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

    @GetMapping("/new/{userId}")
    public ResponseEntity<Ticket> newTicket(@PathVariable Long userId) {
        Ticket ticket = new Ticket();
        Optional<User> userOptional = bookingFacade.getUserById(userId);
        if (userOptional.isPresent()) {
            ticket.setUser(userOptional.get());
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket, @RequestParam("userId") Long userId) {
        Ticket createdTicket = bookingFacade.bookTicket(userId, ticket.getEvent().getId(), ticket.getPlace(), ticket.getCategory());
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        bookingFacade.cancelTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadDefaultTickets(@RequestParam("file") MultipartFile file) {
        bookingFacade.preloadTickets(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/seed")
    public ResponseEntity<Void> seedTickets() {
        bookingFacade.preloadTickets();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadAllTickets() {
        ByteArrayInputStream bis = bookingFacade.generateTicketsPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=tickets.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/download/user/{userId}")
    public ResponseEntity<InputStreamResource> downloadTicketsByUser(@PathVariable Long userId) {
        ByteArrayInputStream bis = bookingFacade.generateUserTicketsPdf(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=user_tickets.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}