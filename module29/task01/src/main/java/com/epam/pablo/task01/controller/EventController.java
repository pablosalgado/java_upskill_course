package com.epam.pablo.task01.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.pablo.task01.exception.EventNotFoundException;
import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;

@RestController
@RequestMapping("/events")
public class EventController {

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
        return new ResponseEntity<>(eventPage.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return Optional.ofNullable(bookingFacade.getEventById(id))
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
            return  Optional.ofNullable(event)
                    .map(this::safelyCreateEvent)
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<Event> safelyCreateEvent(Event event) {
        try {
            Event createdEvent = bookingFacade.createEvent(event);
            return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return Optional.ofNullable(event)
                .map(e -> safelyUpdateEvent(id, e))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<Event> safelyUpdateEvent(Long id, Event event) {
        try {
            Event updatedEvent = bookingFacade.updateEvent(id, event);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        bookingFacade.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}