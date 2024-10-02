package com.epam.pablo.task01.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;

@RestController
@RequestMapping("/events")
@Profile("async")
public class AsyncEventController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;
    private final JmsTemplate jmsTemplate;

    public AsyncEventController(JmsTemplate jmsTemplate, BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
        this.jmsTemplate = jmsTemplate;
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
    public ResponseEntity<String> createEvent(@RequestBody String event) {
        jmsTemplate.convertAndSend("createEventQueue", event);
        return ResponseEntity.accepted().body("Event creation request received");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody String event) {
        jmsTemplate.convertAndSend("updateEventQueue", new Object[]{id, event});
        return ResponseEntity.accepted().body("Event update request received");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        jmsTemplate.convertAndSend("deleteEventQueue", id);
        return ResponseEntity.accepted().body("Event deletion request received");
    }

}