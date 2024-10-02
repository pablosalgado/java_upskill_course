package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@Profile("sync")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;

    public EventController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents(@RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<Event> eventPage = bookingFacade.getAllEvents(size, page);
        return ResponseEntity.ok(eventPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return Optional.ofNullable(bookingFacade.getEventById(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event createdEvent = bookingFacade.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (Exception e) {
            logger.error("Failed to create event: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            Event updatedEvent = bookingFacade.updateEvent(id, event);
            return ResponseEntity.ok().body(updatedEvent);
        } catch (Exception e) {
            logger.error("Failed to update event: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            bookingFacade.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete event: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}